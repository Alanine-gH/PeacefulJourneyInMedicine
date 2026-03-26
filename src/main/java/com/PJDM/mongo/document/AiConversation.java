package com.PJDM.mongo.document;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/*
 * AI 分诊对话记录（MongoDB 存储）
 * 每个分诊会话对应一条文档，包含完整多轮对话历史。
 * 对话结束后异步生成总结，写回 MySQL medical_triage_record.triage_result。
 */
 /**
 * @author Alanine
 */
@Data
@Document(collection = "ai_conversation")
public class AiConversation {

    @Id
    private String id;

    /**
     * 会话ID（前端用于多轮对话标识，UUID生成）
     */
    @Indexed(unique = true)
    @Field("session_id")
    private String sessionId;

    /**
     * 关联的 MySQL medical_triage_record.id
     * 对话结束后总结写回此记录
     */
    @Indexed
    @Field("triage_record_id")
    private Long triageRecordId;

    /**
     * 患者用户ID（user_user.id）
     */
    @Field("user_id")
    private Long userId;

    /**
     * 患者姓名（冗余，方便查询）
     */
    @Field("user_name")
    private String userName;

    /**
     * 会话状态：ACTIVE=进行中, SUMMARIZING=总结中, DONE=已完成, ERROR=出错
     */
    @Field("status")
    private String status = "ACTIVE";

    /**
     * 多轮对话消息列表
     */
    @Field("messages")
    private List<AiMessage> messages = new ArrayList<>();

    /**
     * AI 生成的总结文本（对话结束后填充）
     */
    @Field("summary")
    private String summary;

    /**
     * 总结生成时间
     */
    @Field("summary_time")
    private LocalDateTime summaryTime;

    /**
     * 会话创建时间
     */
    @Field("create_time")
    private LocalDateTime createTime;

    /**
     * 最后消息时间
     */
    @Field("last_message_time")
    private LocalDateTime lastMessageTime;

    // ==================== 内部消息类 ====================

    @Data
    public static class AiMessage {
        /** 角色：user / assistant / system */
        private String role;
        /** 消息内容 */
        private String content;
        /** 消息时间 */
        private LocalDateTime time;

        public static AiMessage of(String role, String content) {
            AiMessage m = new AiMessage();
            m.role = role;
            m.content = content;
            m.time = LocalDateTime.now();
            return m;
        }
    }
}
