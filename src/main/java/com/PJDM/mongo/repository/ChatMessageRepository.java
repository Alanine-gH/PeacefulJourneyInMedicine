package com.PJDM.mongo.repository;

import com.PJDM.mongo.document.ChatMessage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 聊天消息 MongoDB Repository
 *
 * @author Alanine
 */
@Repository
public interface ChatMessageRepository extends MongoRepository<ChatMessage, String> {

    /**
     * 查询两人之间的聊天记录（双向），按时间倒序分页
     */
    @Query("{ $or: [ { sender_id: ?0, receiver_id: ?1 }, { sender_id: ?1, receiver_id: ?0 } ] }")
    Page<ChatMessage> findConversation(Long userA, Long userB, Pageable pageable);

    /**
     * 查询某用户收到的未读消息数
     */
    long countByReceiverIdAndIsRead(Long receiverId, Boolean isRead);

    /**
     * 标记某会话的所有未读消息为已读
     */
    @Query("{ receiver_id: ?0, sender_id: ?1, is_read: false }")
    List<ChatMessage> findUnreadMessages(Long receiverId, Long senderId);
}
