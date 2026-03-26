package com.PJDM.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * AI 聊天请求 DTO
 * 支持：系统前缀提示词、历史消息上下文、用户输入、系统后缀提示词
 *
 * @author Alanine
 */
@Data
@Schema(description = "AI聊天请求")
public class AiChatDTO {

    /** 前置系统提示词（system role，定义AI身份/角色/行为规范） */
    @Schema(description = "前置系统提示词，定义AI角色行为", example = "你是医路安心的智能导诊助手，请用专业、温和的语气回答医疗问题。")
    private String prePrompt;

    /** 用户输入内容（必填） */
    @Schema(description = "用户输入内容", required = true, example = "我最近头痛，应该挂哪个科？")
    private String userInput;

    /** 后置系统提示词（附加约束，如：请用中文回答，回答不超过200字） */
    @Schema(description = "后置提示词，附加约束或格式要求", example = "请用中文回答，回答简洁不超过200字。")
    private String postPrompt;

    /** 历史消息列表（多轮对话上下文） */
    @Schema(description = "历史对话消息列表（多轮上下文）")
    private List<AiMessageDTO> history;

    /** 模型覆盖（可选，不填则用配置默认值） */
    @Schema(description = "模型名称，可选覆盖默认配置", example = "deepseek-chat")
    private String model;

    /** 温度覆盖（可选，0.0~2.0，越大越随机） */
    @Schema(description = "温度参数 0.0~2.0，可选覆盖默认配置")
    private Double temperature;

    /** 最大 token 数覆盖（可选） */
    @Schema(description = "最大输出 token 数，可选覆盖默认配置")
    private Integer maxTokens;

    /** 是否流式输出（true=SSE流式，false=同步一次性返回） */
    @Schema(description = "是否启用流式SSE输出", example = "true")
    private boolean stream = false;
}
