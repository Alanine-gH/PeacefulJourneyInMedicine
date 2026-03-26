package com.PJDM.service.impl;

import com.PJDM.config.DeepSeekConfig;
import com.PJDM.dto.AiChatDTO;
import com.PJDM.dto.AiMessageDTO;
import com.PJDM.service.IAiChatService;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * AI 聊天服务实现
 * 基于 DeepSeek API，使用 Java 原生 HttpClient
 * 支持：同步聊天、SSE 流式聊天、场景化方法
 *
 * @author Alanine
 */
@Slf4j
@Service
public class AiChatServiceImpl implements IAiChatService {

    @Autowired
    private DeepSeekConfig config;

    /** 默认医疗助手系统提示词 */
    private static final String DEFAULT_SYSTEM_PROMPT =
            "你是医路安心（PJDM）平台的智能医疗助手，专业、温和、负责。" +
            "你能帮助用户解答就医疑问、推荐科室、提供就诊建议。" +
            "如遇紧急情况请提醒用户立即拨打120。请始终用中文回答。";

    /** 敏感词列表（命中则拒绝调用 DeepSeek，返回固定提示） */
    private static final List<String> SENSITIVE_WORDS = java.util.Arrays.asList(
            "政治", "习近平", "毛泽东", "政府", "共产党", "民主", "台湾独立", "法轮功",
            "暴力", "杀人", "自杀", "爆炸", "恐怖", "黄色", "色情", "赌博", "毒品",
            "博彩", "洗钱", "诈骗", "枪支", "弹药"
    );

    /** 简单问候 → 固定回复（避免为闲聊消耗 API 额度） */
    private static final Map<String, String> QUICK_REPLIES;
    static {
        QUICK_REPLIES = new java.util.LinkedHashMap<>();
        QUICK_REPLIES.put("你好", "您好！我是医路安心智能助手，有什么就医问题可以告诉我~");
        QUICK_REPLIES.put("hello", "您好！我是医路安心智能助手，有什么就医问题可以告诉我~");
        QUICK_REPLIES.put("hi", "您好！我是医路安心智能助手，有什么就医问题可以告诉我~");
        QUICK_REPLIES.put("谢谢", "不客气，祝您早日康复！如还有问题随时告诉我。");
        QUICK_REPLIES.put("感谢", "不客气，祝您早日康复！如还有问题随时告诉我。");
        QUICK_REPLIES.put("再见", "再见，保重身体，有需要随时来找我！");
        QUICK_REPLIES.put("bye", "再见，保重身体，有需要随时来找我！");
        QUICK_REPLIES.put("你是谁", "我是医路安心（PJDM）平台的智能医疗助手，可以帮您推荐科室、解答就医问题。");
        QUICK_REPLIES.put("你叫什么", "我是医路安心智能助手，专注于陪诊、就医指导服务。");
        QUICK_REPLIES.put("好的", "好的，如有其他问题请随时提问！");
        QUICK_REPLIES.put("ok", "好的，如有其他问题请随时提问！");
        QUICK_REPLIES.put("是的", "好的，如有其他问题请随时提问！");
        QUICK_REPLIES.put("120", "紧急情况请立即拨打120急救电话！如需了解更多就医信息，请告诉我您的症状。");
    }

    /**
     * 敏感词检测：命中返回拒绝提示，否则返回 null
     */
    private String checkSensitive(String input) {
        if (!StringUtils.hasText(input)) return null;
        String lower = input.toLowerCase();
        for (String word : SENSITIVE_WORDS) {
            if (lower.contains(word.toLowerCase())) {
                log.warn("[AI安全] 敏感词命中：{} input={}", word,
                        input.length() > 30 ? input.substring(0, 30) + "..." : input);
                return "抱歉，您的问题涉及到我无法回答的内容，请咨询相关专业人员。";
            }
        }
        return null;
    }

    /**
     * 快速回复检测：命中返回固定回复，否则返回 null
     */
    private String checkQuickReply(String input) {
        if (!StringUtils.hasText(input)) return null;
        String trimmed = input.trim();
        for (Map.Entry<String, String> entry : QUICK_REPLIES.entrySet()) {
            // 完全匹配或以关键词开头的短消息（≤10字）
            if (trimmed.equalsIgnoreCase(entry.getKey()) ||
                (trimmed.length() <= 10 && trimmed.contains(entry.getKey()))) {
                log.info("[AI快速回复] key={}", entry.getKey());
                return entry.getValue();
            }
        }
        return null;
    }

    /** 流式响应使用独立线程池，避免阻塞 Tomcat 线程 */
    private static final ExecutorService SSE_EXECUTOR =
            Executors.newCachedThreadPool();

    /** 共享 HttpClient（连接池复用） */
    private volatile HttpClient httpClient;

    private HttpClient getHttpClient() {
        if (httpClient == null) {
            synchronized (this) {
                if (httpClient == null) {
                    httpClient = config.buildHttpClient();
                }
            }
        }
        return httpClient;
    }

    // ==================== 公开接口实现 ====================

    @Override
    public String chat(AiChatDTO dto) {
        // 1. 敏感词过滤
        String sensitive = checkSensitive(dto.getUserInput());
        if (sensitive != null) return sensitive;
        // 2. 简单问题固定回复
        String quick = checkQuickReply(dto.getUserInput());
        if (quick != null) return quick;

        List<JSONObject> messages = buildMessages(dto);
        String body = buildRequestBody(messages, dto, false);
        log.info("[AI同步] model={} messages={}", resolveModel(dto), messages.size());
        return doSyncRequest(body);
    }

    @Override
    public void chatStream(AiChatDTO dto, SseEmitter emitter) {
        // 1. 敏感词过滤
        String sensitive = checkSensitive(dto.getUserInput());
        if (sensitive != null) {
            try {
                emitter.send(SseEmitter.event().name("message").data(sensitive));
                emitter.send(SseEmitter.event().name("done").data(sensitive));
                emitter.complete();
            } catch (Exception e) { emitter.completeWithError(e); }
            return;
        }
        // 2. 简单问题固定回复（直接推流，无需调 DeepSeek）
        String quick = checkQuickReply(dto.getUserInput());
        if (quick != null) {
            try {
                emitter.send(SseEmitter.event().name("message").data(quick));
                emitter.send(SseEmitter.event().name("done").data(quick));
                emitter.complete();
            } catch (Exception e) { emitter.completeWithError(e); }
            return;
        }

        List<JSONObject> messages = buildMessages(dto);
        String body = buildRequestBody(messages, dto, true);
        log.info("[AI流式] model={} messages={}", resolveModel(dto), messages.size());
        SSE_EXECUTOR.submit(() -> doStreamRequest(body, emitter));
    }

    @Override
    public String simpleChat(String userInput) {
        // 敏感词 + 快速回复
        String sensitive = checkSensitive(userInput);
        if (sensitive != null) return sensitive;
        String quick = checkQuickReply(userInput);
        if (quick != null) return quick;

        AiChatDTO dto = new AiChatDTO();
        dto.setPrePrompt(DEFAULT_SYSTEM_PROMPT);
        dto.setUserInput(userInput);
        return chat(dto);
    }

    @Override
    public String chatWithSystem(String systemPrompt, String userInput) {
        AiChatDTO dto = new AiChatDTO();
        dto.setPrePrompt(systemPrompt);
        dto.setUserInput(userInput);
        return chat(dto);
    }

    @Override
    public String triageAssist(String symptoms) {
        String pre = "你是专业的医疗分诊助手，根据患者描述的症状，推荐最合适的就诊科室，并简要说明理由。" +
                     "如有紧急症状（如胸痛、意识丧失、大出血），请优先提醒患者立即就医或拨打120。";
        String post = "请按以下格式回答：\n推荐科室：XXX\n理由：XXX\n注意事项：XXX";
        AiChatDTO dto = new AiChatDTO();
        dto.setPrePrompt(pre);
        dto.setUserInput("患者症状：" + symptoms);
        dto.setPostPrompt(post);
        return chat(dto);
    }

    @Override
    public String medicalAdvice(String department, String complaint) {
        String pre = "你是专业的就医指导助手，根据患者即将就诊的科室和主诉，提供就诊前的准备建议。";
        String post = "请简洁列出：1.需要携带的资料 2.检查前注意事项 3.就诊时的沟通建议。字数不超过300字。";
        AiChatDTO dto = new AiChatDTO();
        dto.setPrePrompt(pre);
        dto.setUserInput("科室：" + department + "，主诉：" + complaint);
        dto.setPostPrompt(post);
        return chat(dto);
    }

    // ==================== 消息构建 ====================

    /**
     * 按照 DeepSeek messages 格式构建消息列表：<br>
     * [system(prePrompt)] + [history...] + [user(userInput)] + [system(postPrompt)]
     */
    private List<JSONObject> buildMessages(AiChatDTO dto) {
        List<JSONObject> messages = new ArrayList<>();

        // 1. 前置 system 提示词
        if (StringUtils.hasText(dto.getPrePrompt())) {
            messages.add(msg("system", dto.getPrePrompt()));
        } else {
            // 无前缀则注入默认医疗助手角色
            messages.add(msg("system", DEFAULT_SYSTEM_PROMPT));
        }

        // 2. 历史对话上下文（多轮）
        if (dto.getHistory() != null && !dto.getHistory().isEmpty()) {
            for (AiMessageDTO h : dto.getHistory()) {
                if (StringUtils.hasText(h.getRole()) && StringUtils.hasText(h.getContent())) {
                    messages.add(msg(h.getRole(), h.getContent()));
                }
            }
        }

        // 3. 用户当前输入
        String userContent = dto.getUserInput();
        // 若有后缀提示词，拼接到用户消息末尾（避免 system 夹在 user 中间导致某些模型报错）
        if (StringUtils.hasText(dto.getPostPrompt())) {
            userContent = userContent + "\n\n[要求]" + dto.getPostPrompt();
        }
        messages.add(msg("user", userContent));

        return messages;
    }

    private JSONObject msg(String role, String content) {
        JSONObject o = new JSONObject();
        o.put("role", role);
        o.put("content", content);
        return o;
    }

    // ==================== 请求体构建 ====================

    private String buildRequestBody(List<JSONObject> messages, AiChatDTO dto, boolean stream) {
        JSONObject body = new JSONObject();
        body.put("model", resolveModel(dto));
        body.put("messages", messages);
        body.put("temperature", resolveTemperature(dto));
        body.put("max_tokens", resolveMaxTokens(dto));
        body.put("stream", stream);
        // 开启流式时加 stream_options 以获取 usage
        if (stream) {
            JSONObject streamOptions = new JSONObject();
            streamOptions.put("include_usage", true);
            body.put("stream_options", streamOptions);
        }
        return body.toJSONString();
    }

    private String resolveModel(AiChatDTO dto) {
        return (dto != null && StringUtils.hasText(dto.getModel())) ? dto.getModel() : config.getModel();
    }

    private double resolveTemperature(AiChatDTO dto) {
        return (dto != null && dto.getTemperature() != null) ? dto.getTemperature() : config.getTemperature();
    }

    private int resolveMaxTokens(AiChatDTO dto) {
        return (dto != null && dto.getMaxTokens() != null) ? dto.getMaxTokens() : config.getMaxTokens();
    }

    // ==================== HTTP 请求执行 ====================

    /** 同步请求，等待完整响应 */
    private String doSyncRequest(String requestBody) {
        try {
            HttpRequest request = buildHttpRequest(requestBody);
            HttpResponse<String> response = getHttpClient().send(
                    request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));

            if (response.statusCode() != 200) {
                log.error("[AI同步] 请求失败 status={} body={}", response.statusCode(), response.body());
                throw new RuntimeException("AI服务请求失败，状态码：" + response.statusCode());
            }

            return parseContent(response.body());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("AI请求被中断", e);
        } catch (Exception e) {
            log.error("[AI同步] 请求异常: {}", e.getMessage(), e);
            throw new RuntimeException("AI服务暂时不可用，请稍后重试", e);
        }
    }

    /** 流式请求，逐行解析 SSE data 并推送给 SseEmitter */
    private void doStreamRequest(String requestBody, SseEmitter emitter) {
        try {
            HttpRequest request = buildHttpRequest(requestBody);
            // 使用 InputStream 逐行读取，适合 SSE 长连接
            HttpResponse<java.io.InputStream> response = getHttpClient().send(
                    request, HttpResponse.BodyHandlers.ofInputStream());

            if (response.statusCode() != 200) {
                String errBody = new String(response.body().readAllBytes(), StandardCharsets.UTF_8);
                log.error("[AI流式] 请求失败 status={} body={}", response.statusCode(), errBody);
                emitter.send(SseEmitter.event().name("error").data("AI服务请求失败：" + response.statusCode()));
                emitter.complete();
                return;
            }

            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(response.body(), StandardCharsets.UTF_8))) {

                String line;
                StringBuilder fullContent = new StringBuilder();

                while ((line = reader.readLine()) != null) {
                    if (!line.startsWith("data: ")) continue;
                    String data = line.substring(6).trim();

                    // 流式结束标志
                    if ("[DONE]".equals(data)) {
                        // 发送完成事件，携带完整内容
                        emitter.send(SseEmitter.event()
                                .name("done")
                                .data(fullContent.toString()));
                        emitter.complete();
                        log.info("[AI流式] 完成，总字符数={}", fullContent.length());
                        return;
                    }

                    // 解析 delta content
                    try {
                        JSONObject json = JSON.parseObject(data);
                        JSONArray choices = json.getJSONArray("choices");
                        if (choices != null && !choices.isEmpty()) {
                            JSONObject delta = choices.getJSONObject(0).getJSONObject("delta");
                            if (delta != null) {
                                String content = delta.getString("content");
                                if (StringUtils.hasText(content)) {
                                    fullContent.append(content);
                                    // 推送 token 片段给前端
                                    emitter.send(SseEmitter.event()
                                            .name("message")
                                            .data(content));
                                }
                            }
                        }
                    } catch (Exception parseEx) {
                        log.warn("[AI流式] 解析行失败，跳过: {}", data);
                    }
                }
                // 正常读完（有些实现不发[DONE]）
                emitter.complete();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            safeCompleteWithError(emitter, "AI请求被中断");
        } catch (Exception e) {
            log.error("[AI流式] 请求异常: {}", e.getMessage(), e);
            safeCompleteWithError(emitter, "AI服务暂时不可用，请稍后重试");
        }
    }

    private HttpRequest buildHttpRequest(String requestBody) {
        return HttpRequest.newBuilder()
                .uri(URI.create(config.getBaseUrl() + "/v1/chat/completions"))
                .header("Content-Type", "application/json; charset=UTF-8")
                .header("Authorization", "Bearer " + config.getApiKey())
                .header("Accept", "text/event-stream")
                .timeout(Duration.ofSeconds(config.getReadTimeoutSeconds()))
                .POST(HttpRequest.BodyPublishers.ofString(requestBody, StandardCharsets.UTF_8))
                .build();
    }

    /** 解析同步响应中的 content 字段 */
    private String parseContent(String responseBody) {
        try {
            JSONObject json = JSON.parseObject(responseBody);
            JSONArray choices = json.getJSONArray("choices");
            if (choices == null || choices.isEmpty()) {
                log.warn("[AI同步] 响应中 choices 为空: {}", responseBody);
                return "AI暂无回复，请重试";
            }
            JSONObject message = choices.getJSONObject(0).getJSONObject("message");
            String content = message != null ? message.getString("content") : null;
            return StringUtils.hasText(content) ? content : "AI暂无回复，请重试";
        } catch (Exception e) {
            log.error("[AI同步] 解析响应失败: {} body={}", e.getMessage(), responseBody);
            throw new RuntimeException("AI响应解析失败", e);
        }
    }

    private void safeCompleteWithError(SseEmitter emitter, String message) {
        try {
            emitter.send(SseEmitter.event().name("error").data(message));
            emitter.complete();
        } catch (Exception ex) {
            emitter.completeWithError(ex);
        }
    }
}
