package com.PJDM.controller;

import com.PJDM.common.BaseContext;
import com.PJDM.common.R;
import com.PJDM.dto.AiChatDTO;
import com.PJDM.service.IAiChatService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;

/**
 * AI 聊天控制器
 * 提供同步聊天、SSE 流式聊天、场景化接口
 * 所有接口需携带 JWT Token（由 LoginInterceptor 校验）
 *
 * @author Alanine
 */
@Slf4j
@RestController
@RequestMapping("/ai")
@Tag(name = "AI智能助手", description = "DeepSeek AI 聊天接口，支持同步和流式SSE，需登录")
public class AiChatController {

    @Autowired
    private IAiChatService aiChatService;

    /**
     * 同步聊天：完整回复后一次性返回
     * 适合短文本、快速响应
     */
    @PostMapping("/chat")
    @Operation(summary = "同步聊天", description = "发送消息，等待AI完整回复后返回。支持前/后提示词和历史上下文。")
    public R<String> chat(@RequestBody AiChatDTO dto) {
        if (dto.getUserInput() == null || dto.getUserInput().isBlank()) {
            return R.error("userInput 不能为空");
        }
        Long userId = BaseContext.getUserId();
        log.info("[AI聊天] 同步请求 userId={} input={}", userId,
                dto.getUserInput().length() > 50 ? dto.getUserInput().substring(0, 50) + "..." : dto.getUserInput());
        String reply = aiChatService.chat(dto);
        return R.success(reply);
    }

    /**
     * 流式聊天（SSE）：逐 token 推送，前端实时显示打字机效果
     * 事件类型：
     *   message - 每个 token 片段
     *   done    - 完成，data 为完整内容
     *   error   - 错误信息
     */
    @PostMapping(value = "/chat/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @Operation(summary = "流式聊天（SSE）", description = "逐token推送AI回复，前端用fetch stream接收")
    public SseEmitter chatStream(@RequestBody AiChatDTO dto) {
        SseEmitter emitter = new SseEmitter(0L);
        if (dto.getUserInput() == null || dto.getUserInput().isBlank()) {
            try {
                emitter.send(SseEmitter.event().name("error").data("userInput 不能为空"));
                emitter.complete();
            } catch (Exception e) {
                emitter.completeWithError(e);
            }
            return emitter;
        }
        Long userId = BaseContext.getUserId();
        log.info("[AI聊天] 流式请求 userId={} input={}", userId,
                dto.getUserInput().length() > 50 ? dto.getUserInput().substring(0, 50) + "..." : dto.getUserInput());
        dto.setStream(true);
        aiChatService.chatStream(dto, emitter);
        return emitter;
    }

    /**
     * 简单单次聊天：只传 userInput，使用默认医疗助手角色
     */
    @PostMapping("/simple")
    @Operation(summary = "简单聊天", description = "只需传入用户消息，使用默认医疗助手角色")
    public R<String> simpleChat(@RequestBody Map<String, String> body) {
        String input = body.get("userInput");
        if (input == null || input.isBlank()) return R.error("userInput 不能为空");
        log.info("[AI简单聊天] userId={} input={}", BaseContext.getUserId(),
                input.length() > 50 ? input.substring(0, 50) + "..." : input);
        return R.success(aiChatService.simpleChat(input));
    }

    /**
     * 智能分诊（单次）：根据症状推荐科室，不保存对话历史
     * 如需多轮对话分诊，请使用 /ai/triage/session/* 系列接口
     */
    @PostMapping("/triage")
    @Operation(summary = "智能分诊（单次）", description = "根据患者症状描述推荐就诊科室，单次调用不保存历史。多轮对话请用 /ai/triage/session/* 接口")
    public R<String> triage(@RequestBody Map<String, String> body) {
        String symptoms = body.get("symptoms");
        if (symptoms == null || symptoms.isBlank()) return R.error("symptoms 不能为空");
        log.info("[AI分诊-单次] userId={} symptoms={}", BaseContext.getUserId(),
                symptoms.length() > 80 ? symptoms.substring(0, 80) + "..." : symptoms);
        return R.success(aiChatService.triageAssist(symptoms));
    }

    /**
     * 就诊建议：生成就诊前准备
     */
    @PostMapping("/advice")
    @Operation(summary = "就诊建议", description = "根据科室和主诉，生成就诊前准备建议")
    public R<String> advice(@RequestBody Map<String, String> body) {
        String department = body.get("department");
        String complaint = body.get("complaint");
        if (department == null || department.isBlank()) return R.error("department 不能为空");
        if (complaint == null || complaint.isBlank()) return R.error("complaint 不能为空");
        log.info("[AI就诊建议] userId={} dept={} complaint={}", BaseContext.getUserId(), department, complaint);
        return R.success(aiChatService.medicalAdvice(department, complaint));
    }
}
