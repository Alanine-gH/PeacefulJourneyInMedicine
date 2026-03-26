package com.PJDM.vo;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 消息列表 VO
 */
@Data
public class MessageListVO {
    private Long id;
    private Long receiverId;
    private Long senderId;
    private String senderName;
    private String type;
    private String title;
    private String content;
    private Long relatedId;
    private String relatedType;
    /** 状态（1：未读；2：已读） */
    private Byte status;
    private LocalDateTime readTime;
    private LocalDateTime createTime;
}
