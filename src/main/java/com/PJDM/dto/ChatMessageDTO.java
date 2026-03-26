package com.PJDM.dto;

import lombok.Data;

/**
 * 聊天消息发送 DTO
 */
@Data
public class ChatMessageDTO {
    /** 聊天对象用户ID */
    private Long targetId;
    /** 消息内容 */
    private String content;
    /** 消息类型（text：文本；image：图片） */
    private String type;
    /** 额外数据（JSON字符串） */
    private String extra;
}
