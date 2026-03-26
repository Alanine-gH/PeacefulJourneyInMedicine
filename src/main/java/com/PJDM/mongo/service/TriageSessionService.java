package com.PJDM.mongo.service;

import com.PJDM.dto.AiChatDTO;
import com.PJDM.dto.AiMessageDTO;
import com.PJDM.mongo.document.AiConversation;
import com.PJDM.mongo.document.AiConversation.AiMessage;
import com.PJDM.mongo.repository.AiConversationRepository;
import com.PJDM.pojo.MedicalTriageRecord;
import com.PJDM.service.IAiChatService;
import com.PJDM.service.IMedicalTriageService;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/*
 * AI 分诊会话服务
 * 职责：
 *   1. 创建/获取会话（MongoDB 存储完整对话历史）
 *   2. 转发用户消息给 DeepSeek，保存 AI 回复
 *   3. 对话结束后异步生成总结，回写 MySQL medical_triage_record
 */

/**
 * @author Alanine
 */
@Slf4j
@Service
public class TriageSessionService {

    @Autowired
    private AiConversationRepository conversationRepo;
    @Autowired
    private IAiChatService aiChatService;
    @Autowired
    private IMedicalTriageService triageService;

    private static final String TRIAGE_SYSTEM_PROMPT =
            "你是医路安心（PJDM）平台的专业智能分诊助手。" +
                    "请通过友好的对话方式，逐步收集患者的症状信息（症状、部位、持续时间、程度、既往病史等），" +
                    "并在信息充足时给出推荐科室和就诊建议。" +
                    "如遇紧急症状（胸痛、意识丧失、大出血等）请立即提示拨打120。" +
                    "请始终用中文回答，语气温和专业。";

    private static final String SUMMARY_PROMPT =
            "请根据以上完整的分诊对话，按以下JSON格式输出结构化总结（只输出JSON，不要多余文字）:\n" +
                    "{\n" +
                    "  \"chiefComplaint\": \"主诉（一句话）\",\n" +
                    "  \"symptomDescription\": \"症状详细描述\",\n" +
                    "  \"painLocation\": \"疼痛/不适部位\",\n" +
                    "  \"painLevel\": 疼痛等级1-10数字,\n" +
                    "  \"painDuration\": \"持续时间\",\n" +
                    "  \"medicalHistory\": \"既往病史\",\n" +
                    "  \"allergyHistory\": \"过敏史\",\n" +
                    "  \"currentMedication\": \"当前用药\",\n" +
                    "  \"recommendedDepartment\": \"推荐科室\",\n" +
                    "  \"triageResult\": \"分诊结论与就诊建议（详细）\",\n" +
                    "  \"aiConfidence\": 置信度0.0-1.0小数\n" +
                    "}";

    // ==================== 公开接口 ====================

    public String createSession(Long userId, String userName, Long triageRecordId) {
        AiConversation conv = new AiConversation();
        conv.setSessionId(UUID.randomUUID().toString());
        conv.setUserId(userId);
        conv.setUserName(userName);
        conv.setTriageRecordId(triageRecordId);
        conv.setStatus("ACTIVE");
        conv.setCreateTime(LocalDateTime.now());
        conv.setLastMessageTime(LocalDateTime.now());
        conv.getMessages().add(AiMessage.of("system", TRIAGE_SYSTEM_PROMPT));
        conversationRepo.save(conv);
        log.info("[分诊会话] 创建 sessionId={} userId={}", conv.getSessionId(), userId);
        return conv.getSessionId();
    }

    public String sendMessage(String sessionId, String userInput) {
        AiConversation conv = getActiveConversation(sessionId);
        conv.getMessages().add(AiMessage.of("user", userInput));
        conv.setLastMessageTime(LocalDateTime.now());
        conversationRepo.save(conv);

        AiChatDTO dto = buildChatDTO(conv, userInput);
        String reply = aiChatService.chat(dto);

        conv.getMessages().add(AiMessage.of("assistant", reply));
        conv.setLastMessageTime(LocalDateTime.now());
        conversationRepo.save(conv);
        log.info("[分诊会话] 消息交互 sessionId={}", sessionId);
        return reply;
    }

    public void sendMessageStream(String sessionId, String userInput, SseEmitter emitter) {
        AiConversation conv = getActiveConversation(sessionId);
        conv.getMessages().add(AiMessage.of("user", userInput));
        conv.setLastMessageTime(LocalDateTime.now());
        conversationRepo.save(conv);
        // 流式完成后，通过 onCompletion 回调不能拿到内容
        // 采用同步方式补存：前端收到 done 事件后调 /finish 或传回 fullContent
        // 此处直接调用 chatStream，AI 回复在 done 事件的 data 中
        AiChatDTO dto = buildChatDTO(conv, userInput);
        aiChatService.chatStream(dto, emitter);
    }

    /**
     * 保存流式 AI 回复（前端 done 事件回传内容后调用）
     */
    public void saveAssistantMessage(String sessionId, String content) {
        conversationRepo.findBySessionId(sessionId).ifPresent(conv -> {
            conv.getMessages().add(AiMessage.of("assistant", content));
            conv.setLastMessageTime(LocalDateTime.now());
            conversationRepo.save(conv);
            log.info("[分诊会话] 流式回复已存储 sessionId={}", sessionId);
        });
    }

    public void finishSession(String sessionId) {
        AiConversation conv = getActiveConversation(sessionId);
        conv.setStatus("SUMMARIZING");
        conversationRepo.save(conv);
        log.info("[分诊会话] 结束会话 sessionId={}, 触发异步总结", sessionId);
        generateSummaryAsync(conv);
    }

    public AiConversation getSession(String sessionId) {
        return conversationRepo.findBySessionId(sessionId)
                .orElseThrow(() -> new RuntimeException("会话不存在: " + sessionId));
    }

    // ==================== 私有方法 ====================

    private AiConversation getActiveConversation(String sessionId) {
        AiConversation conv = conversationRepo.findBySessionId(sessionId)
                .orElseThrow(() -> new RuntimeException("会话不存在: " + sessionId));
        if ("DONE".equals(conv.getStatus()) || "SUMMARIZING".equals(conv.getStatus())) {
            throw new RuntimeException("会话已结束，无法继续发送消息");
        }
        return conv;
    }

    private AiChatDTO buildChatDTO(AiConversation conv, String currentInput) {
        AiChatDTO dto = new AiChatDTO();
        dto.setPrePrompt(TRIAGE_SYSTEM_PROMPT);
        dto.setUserInput(currentInput);
        List<AiMessageDTO> history = conv.getMessages().stream()
                .filter(m -> !"system".equals(m.getRole()))
                .filter(m -> !("user".equals(m.getRole()) && m.getContent().equals(currentInput)))
                .map(m -> {
                    AiMessageDTO msg = new AiMessageDTO();
                    msg.setRole(m.getRole());
                    msg.setContent(m.getContent());
                    return msg;
                })
                .collect(Collectors.toList());
        if (history.size() > 40) history = history.subList(history.size() - 40, history.size());
        dto.setHistory(history);
        return dto;
    }

    @Async
    public void generateSummaryAsync(AiConversation conv) {
        try {
            log.info("[分诊总结] 开始生成 sessionId={}", conv.getSessionId());
            AiChatDTO dto = new AiChatDTO();
            dto.setPrePrompt(TRIAGE_SYSTEM_PROMPT);
            List<AiMessageDTO> history = conv.getMessages().stream()
                    .filter(m -> !"system".equals(m.getRole()))
                    .map(m -> {
                        AiMessageDTO msg = new AiMessageDTO();
                        msg.setRole(m.getRole());
                        msg.setContent(m.getContent());
                        return msg;
                    })
                    .collect(Collectors.toList());
            dto.setHistory(history);
            dto.setUserInput("请对以上分诊对话生成结构化总结。");
            dto.setPostPrompt(SUMMARY_PROMPT);
            dto.setTemperature(0.3);
            dto.setMaxTokens(1000);

            String summaryJson = aiChatService.chat(dto);
            conv.setSummary(summaryJson);
            conv.setSummaryTime(LocalDateTime.now());
            conv.setStatus("DONE");
            conversationRepo.save(conv);

            if (conv.getTriageRecordId() != null) {
                writeSummaryToMysql(conv.getTriageRecordId(), summaryJson);
            }
            log.info("[分诊总结] 完成 sessionId={}", conv.getSessionId());
        } catch (Exception e) {
            log.error("[分诊总结] 生成失败 sessionId={} err={}", conv.getSessionId(), e.getMessage(), e);
            conv.setStatus("ERROR");
            conversationRepo.save(conv);
        }
    }

    private void writeSummaryToMysql(Long triageRecordId, String summaryJson) {
        try {
            String json = summaryJson.trim();
            int start = json.indexOf('{');
            int end = json.lastIndexOf('}');
            if (start >= 0 && end > start) json = json.substring(start, end + 1);

            JSONObject obj = JSON.parseObject(json);
            MedicalTriageRecord update = new MedicalTriageRecord();
            update.setId(triageRecordId);
            if (obj.getString("chiefComplaint") != null) update.setChiefComplaint(obj.getString("chiefComplaint"));
            if (obj.getString("symptomDescription") != null)
                update.setSymptomDescription(obj.getString("symptomDescription"));
            if (obj.getString("painLocation") != null) update.setPainLocation(obj.getString("painLocation"));
            if (obj.getInteger("painLevel") != null) update.setPainLevel(obj.getInteger("painLevel").byteValue());
            if (obj.getString("painDuration") != null) update.setPainDuration(obj.getString("painDuration"));
            if (obj.getString("medicalHistory") != null) update.setMedicalHistory(obj.getString("medicalHistory"));
            if (obj.getString("allergyHistory") != null) update.setAllergyHistory(obj.getString("allergyHistory"));
            if (obj.getString("currentMedication") != null)
                update.setCurrentMedication(obj.getString("currentMedication"));
            if (obj.getString("recommendedDepartment") != null)
                update.setRecommendedDepartment(obj.getString("recommendedDepartment"));
            if (obj.getString("triageResult") != null) update.setTriageResult(obj.getString("triageResult"));
            if (obj.getBigDecimal("aiConfidence") != null) update.setAiConfidence(obj.getBigDecimal("aiConfidence"));
            update.setTriageStatus((byte) 3); // AI分诊完成
            update.setAiAnalysisTime(LocalDateTime.now());
            triageService.updateById(update);
            log.info("[分诊总结] 已写回MySQL triageRecordId={}", triageRecordId);
        } catch (Exception e) {
            log.error("[分诊总结] 写回MySQL失败 triageRecordId={} err={}", triageRecordId, e.getMessage(), e);
        }
    }
}
