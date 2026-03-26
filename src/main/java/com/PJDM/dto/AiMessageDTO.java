package com.PJDM.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * AI 多轮对话历史消息
 *
 * @author Alanine
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "AI对话历史消息")
public class AiMessageDTO {

    /** 角色：system / user / assistant */
    @Schema(description = "消息角色：system/user/assistant", example = "user")
    private String role;

    /** 消息内容 */
    @Schema(description = "消息内容", example = "我最近头痛")
    private String content;
}
