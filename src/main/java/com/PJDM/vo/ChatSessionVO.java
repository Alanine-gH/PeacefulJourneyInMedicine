package com.PJDM.vo;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 聊天会话列表 VO
 */
@Data
public class ChatSessionVO {
    /** 对话目标用户ID */
    private Long targetId;
    /** 对话目标用户昵称 */
    private String targetName;
    /** 头像URL */
    private String targetAvatar;
    /** 最新一条消息内容 */
    private String lastMessage;
    /** 最新消息时间 */
    private LocalDateTime lastMessageTime;
    /** 未读数量 */
    private Integer unreadCount;
}
