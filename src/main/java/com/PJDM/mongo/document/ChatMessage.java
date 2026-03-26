package com.PJDM.mongo.document;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

/*
 * 聊天消息（MongoDB 存储）
 * 每条消息一个文档，三个核心字段：发送者ID、接收者ID、消息内容
 */
 /**
 *
 * @author Alanine
 */
@Data
@Document(collection = "chat_message")
@CompoundIndex(name = "idx_sender_receiver", def = "{'sender_id': 1, 'receiver_id': 1, 'create_time': -1}")
@CompoundIndex(name = "idx_receiver_sender", def = "{'receiver_id': 1, 'sender_id': 1, 'create_time': -1}")
public class ChatMessage {

    @Id
    private String id;

    /**
     * 发送者用户ID
     */
    @Indexed
    @Field("sender_id")
    private Long senderId;

    /**
     * 接收者用户ID
     */
    @Indexed
    @Field("receiver_id")
    private Long receiverId;

    /**
     * 消息内容
     */
    @Field("content")
    private String content;

    /**
     * 消息类型：text / image / voice
     */
    @Field("type")
    private String type = "text";

    /**
     * 是否已读
     */
    @Field("is_read")
    private Boolean isRead = false;

    /**
     * 发送时间
     */
    @Indexed
    @Field("create_time")
    private LocalDateTime createTime;

    public static ChatMessage of(Long senderId, Long receiverId, String content) {
        ChatMessage msg = new ChatMessage();
        msg.senderId = senderId;
        msg.receiverId = receiverId;
        msg.content = content;
        msg.type = "text";
        msg.isRead = false;
        msg.createTime = LocalDateTime.now();
        return msg;
    }
}
