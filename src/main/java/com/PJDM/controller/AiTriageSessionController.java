package com.PJDM.controller;

import com.PJDM.common.BaseContext;
import com.PJDM.common.R;
import com.PJDM.mongo.document.AiConversation;
import com.PJDM.mongo.service.TriageSessionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;

/**
 * AI 分诊会话控制器
 * 对话历史存 MongoDB，对话结束后异步生成结构化总结写回 MySQL medical_triage_record。
 *
 * 典型使用流程：
 *   1. POST /ai/triage/session/start                     创建会话，获取 sessionId
 *   2. POST /ai/triage/session/{sessionId}/message       同步发送消息
 *      或  POST /ai/triage/session/{sessionId}/stream    流式发送（SSE）
 *      +   POST /ai/triage/session/{sessionId}/save-reply 流式完成后保存AI回复
 *   3. 重复步骤2直到对话完成
 *   4. POST /ai/triage/session/{sessionId}/finish        结束会话，异步生成总结
 *   5. GET  /ai/triage/session/{sessionId}               查询会话详情和总结
 *
 * @author Alanine
 */
@Slf4j
@RestController
@RequestMapping("/ai/triage/session")
@Tag(name = "AI分诊会话", description = "多轮对话分诊：历史存MongoDB，总结异步写回MySQL medical_triage_record，需登录")
public class AiTriageSessionController {

    @Autowired
    private TriageSessionService sessionService;

    /**
     * 创建分诊会话
     * userId 优先从 JWT Token 自动获取，body 中可覆盖（管理端代患者创建时使用）
     */
    @PostMapping("/start")
    @Operation(summary = "创建分诊会话",
            description = "返回 sessionId。userId 自动从登录Token获取，可在body中覆盖。" +
                    "triageRecordId 可选，绑定后总结生成完毕会自动写回对应分诊记录。")
    public R<String> start(@RequestBody(required = false) Map<String, Object> body) {
        Long userId = BaseContext.getUserId();
        String userName = null;
        Long triageRecordId = null;
        if (body != null) {
            if (body.get("userId") != null) userId = Long.parseLong(body.get("userId").toString());
            userName = (String) body.get("userName");
            if (body.get("triageRecordId") != null)
                triageRecordId = Long.parseLong(body.get("triageRecordId").toString());
        }
        String sessionId = sessionService.createSession(userId, userName, triageRecordId);
        log.info("[分诊会话] 创建 userId={} triageRecordId={} sessionId={}", userId, triageRecordId, sessionId);
        return R.success(sessionId);
    }

    /**
     * 同步发送消息，返回 AI 完整回复
     */
    @PostMapping("/{sessionId}/message")
    @Operation(summary = "发送消息（同步）",
            description = "发送用户消息，等待AI完整回复后返回。消息和AI回复自动持久化到MongoDB。")
    public R<String> sendMessage(
            @Parameter(description = "会话ID") @PathVariable String sessionId,
            @RequestBody Map<String, String> body) {
        String content = body.get("content");
        if (content == null || content.isBlank()) return R.error("content 不能为空");
        try {
            String reply = sessionService.sendMessage(sessionId, content);
            return R.success(reply);
        } catch (RuntimeException e) {
            log.warn("[分诊会话] 发送消息失败 sessionId={} err={}", sessionId, e.getMessage());
            return R.error(e.getMessage());
        }
    }

    /**
     * 流式发送消息（SSE），逐 token 推送 AI 回复
     * 前端收到 done 事件后，需调用 /save-reply 将完整回复保存到 MongoDB
     */
    @PostMapping(value = "/{sessionId}/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @Operation(summary = "发送消息（流式SSE）",
            description = "逐token推送AI回复。事件：message=token片段, done=完整回复, error=错误。" +
                    "收到done事件后需调用 /save-reply 保存AI回复到MongoDB。")
    public SseEmitter sendMessageStream(
            @Parameter(description = "会话ID") @PathVariable String sessionId,
            @RequestBody Map<String, String> body) {
        SseEmitter emitter = new SseEmitter(0L);
        String content = body.get("content");
        if (content == null || content.isBlank()) {
            try {
                emitter.send(SseEmitter.event().name("error").data("content 不能为空"));
                emitter.complete();
            } catch (Exception e) { emitter.completeWithError(e); }
            return emitter;
        }
        try {
            sessionService.sendMessageStream(sessionId, content, emitter);
        } catch (RuntimeException e) {
            log.warn("[分诊会话] 流式发送失败 sessionId={} err={}", sessionId, e.getMessage());
            try {
                emitter.send(SseEmitter.event().name("error").data(e.getMessage()));
                emitter.complete();
            } catch (Exception ex) { emitter.completeWithError(ex); }
        }
        return emitter;
    }

    /**
     * 保存流式 AI 回复到 MongoDB
     * 流式接口无法在服务端捕获完整内容，前端收到 done 事件后调用此接口补存
     */
    @PostMapping("/{sessionId}/save-reply")
    @Operation(summary = "保存流式AI回复",
            description = "前端收到SSE done事件后调用，将完整AI回复存入MongoDB会话历史，确保多轮上下文完整。")
    public R<String> saveReply(
            @Parameter(description = "会话ID") @PathVariable String sessionId,
            @RequestBody Map<String, String> body) {
        String content = body.get("content");
        if (content == null || content.isBlank()) return R.error("content 不能为空");
        sessionService.saveAssistantMessage(sessionId, content);
        return R.success("已保存");
    }

    /**
     * 结束会话，触发异步总结生成
     * 立即返回，后台异步：
     *   1. 向 DeepSeek 发送完整对话 + JSON 格式约束提示词
     *   2. 解析总结 JSON，写回 MySQL medical_triage_record 各字段
     *   3. triage_status 更新为 3（AI分诊完成）
     */
    @PostMapping("/{sessionId}/finish")
    @Operation(summary = "结束会话并生成总结",
            description = "立即返回，总结在后台异步生成。完成后自动写回MySQL分诊记录各字段，状态改为AI分诊完成(3)。")
    public R<String> finish(@Parameter(description = "会话ID") @PathVariable String sessionId) {
        try {
            sessionService.finishSession(sessionId);
            return R.success("会话已结束，总结正在后台生成");
        } catch (RuntimeException e) {
            log.warn("[分诊会话] 结束会话失败 sessionId={} err={}", sessionId, e.getMessage());
            return R.error(e.getMessage());
        }
    }

    /**
     * 查询会话详情（含全部消息历史和总结）
     */
    @GetMapping("/{sessionId}")
    @Operation(summary = "查询会话详情",
            description = "返回会话状态（ACTIVE/SUMMARIZING/DONE/ERROR）、完整消息历史、总结内容。")
    public R<AiConversation> getSession(
            @Parameter(description = "会话ID") @PathVariable String sessionId) {
        try {
            return R.success(sessionService.getSession(sessionId));
        } catch (RuntimeException e) {
            return R.error(e.getMessage());
        }
    }
}
