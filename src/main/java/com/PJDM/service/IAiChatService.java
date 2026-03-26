package com.PJDM.service;

import com.PJDM.dto.AiChatDTO;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

/**
 * AI 聊天服务接口
 * 提供同步聊天、流式SSE聊天、单次补全等多种调用方式
 *
 * @author Alanine
 */
public interface IAiChatService {

    /**
     * 同步聊天：等待AI完整回复后一次性返回
     * 适合短文本、快速响应场景
     *
     * @param dto 聊天请求（含前缀提示词、用户输入、后缀提示词、历史）
     * @return AI 完整回复文本
     */
    String chat(AiChatDTO dto);

    /**
     * 流式聊天（SSE）：逐 token 推送给前端
     * 适合长文本生成、实时打字机效果
     *
     * @param dto     聊天请求
     * @param emitter SseEmitter，由 Controller 创建并传入
     */
    void chatStream(AiChatDTO dto, SseEmitter emitter);

    /**
     * 简单单次补全：只传入文本，使用默认医疗助手角色
     * 内部自动注入系统提示词，适合快速接入场景
     *
     * @param userInput 用户输入
     * @return AI 回复
     */
    String simpleChat(String userInput);

    /**
     * 带角色预设的单次补全
     *
     * @param systemPrompt 系统提示词（角色定义）
     * @param userInput    用户输入
     * @return AI 回复
     */
    String chatWithSystem(String systemPrompt, String userInput);

    /**
     * 智能分诊辅助：根据症状描述推荐科室
     *
     * @param symptoms 症状描述
     * @return 推荐科室及建议
     */
    String triageAssist(String symptoms);

    /**
     * 医疗建议辅助：生成就诊前准备建议
     *
     * @param department 科室名称
     * @param complaint  主诉
     * @return 就诊建议文本
     */
    String medicalAdvice(String department, String complaint);
}
