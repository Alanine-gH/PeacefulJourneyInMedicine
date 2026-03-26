package com.PJDM.controller;

import com.PJDM.common.BaseContext;
import com.PJDM.common.R;
import com.PJDM.mongo.document.ChatMessage;
import com.PJDM.mongo.repository.ChatMessageRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 用户聊天消息控制器
 * 消息存储于 MongoDB chat_message 集合
 * 三个核心字段：发送者ID、接收者ID、消息内容
 *
 * @author Alanine
 */
@Slf4j
@RestController
@RequestMapping("/chat")
@Tag(name = "聊天消息", description = "用户间聊天消息，基于 MongoDB 存储")
public class ChatMessageController {

    @Autowired
    private ChatMessageRepository chatMessageRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     * 发送消息
     * 请求体：{ receiverId, content, type(可选,默认text) }
     */
    @PostMapping("/send")
    @Operation(summary = "发送消息")
    public R<ChatMessage> send(@RequestBody Map<String, Object> body) {
        Long senderId = BaseContext.getUserId();
        Object receiverObj = body.get("receiverId");
        String content = (String) body.get("content");
        String type = body.containsKey("type") ? (String) body.get("type") : "text";

        if (receiverObj == null) return R.error("receiverId 不能为空");
        if (!StringUtils.hasText(content)) return R.error("content 不能为空");

        Long receiverId = Long.parseLong(receiverObj.toString());
        if (senderId.equals(receiverId)) return R.error("不能给自己发消息");

        ChatMessage msg = new ChatMessage();
        msg.setSenderId(senderId);
        msg.setReceiverId(receiverId);
        msg.setContent(content);
        msg.setType(type);
        msg.setIsRead(false);
        msg.setCreateTime(LocalDateTime.now());

        ChatMessage saved = chatMessageRepository.save(msg);
        log.info("[聊天] 发送消息 from={} to={} type={}", senderId, receiverId, type);
        return R.success(saved);
    }

    /**
     * 获取与指定用户的聊天记录（分页，按时间正序）
     * GET /chat/history/{targetUserId}?page=0&pageSize=20
     */
    @GetMapping("/history/{targetUserId}")
    @Operation(summary = "获取聊天记录")
    public R<Map<String, Object>> history(
            @PathVariable Long targetUserId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int pageSize) {

        Long myId = BaseContext.getUserId();
        PageRequest pageable = PageRequest.of(page, pageSize, Sort.by(Sort.Direction.ASC, "create_time"));
        Page<ChatMessage> result = chatMessageRepository.findConversation(myId, targetUserId, pageable);

        // 标记对方发给我的消息为已读
        markAsRead(myId, targetUserId);

        Map<String, Object> data = new LinkedHashMap<>();
        data.put("total", result.getTotalElements());
        data.put("pages", result.getTotalPages());
        data.put("current", page);
        data.put("records", result.getContent());
        return R.success(data);
    }

    /**
     * 获取会话列表（消息中心）
     * 返回当前用户所有有过对话的联系人，每人取最新一条消息
     */
    @GetMapping("/sessions")
    @Operation(summary = "获取会话列表")
    public R<List<Map<String, Object>>> sessions() {
        Long myId = BaseContext.getUserId();

        // 查询我发出或收到的所有消息，按时间倒序
        Query query = new Query(
                new Criteria().orOperator(
                        Criteria.where("sender_id").is(myId),
                        Criteria.where("receiver_id").is(myId)
                )
        ).with(Sort.by(Sort.Direction.DESC, "create_time"));

        List<ChatMessage> allMessages = mongoTemplate.find(query, ChatMessage.class);

        // 按对方ID聚合，取最新一条
        Map<Long, ChatMessage> latestByPeer = new LinkedHashMap<>();
        for (ChatMessage m : allMessages) {
            Long peerId = m.getSenderId().equals(myId) ? m.getReceiverId() : m.getSenderId();
            latestByPeer.putIfAbsent(peerId, m);
        }

        List<Map<String, Object>> sessions = latestByPeer.entrySet().stream().map(e -> {
            Long peerId = e.getKey();
            ChatMessage latest = e.getValue();
            long unread = chatMessageRepository.countByReceiverIdAndIsRead(myId, false);

            Map<String, Object> session = new LinkedHashMap<>();
            session.put("peerId", peerId);
            session.put("lastMessage", latest.getContent());
            session.put("lastTime", latest.getCreateTime());
            session.put("unreadCount", unread);
            return session;
        }).collect(Collectors.toList());

        return R.success(sessions);
    }

    /**
     * 标记某会话消息为已读
     * POST /chat/read/{senderId}
     */
    @PostMapping("/read/{senderId}")
    @Operation(summary = "标记消息已读")
    public R<String> markRead(@PathVariable Long senderId) {
        Long myId = BaseContext.getUserId();
        markAsRead(myId, senderId);
        return R.success("已标记为已读");
    }

    /**
     * 获取未读消息总数
     */
    @GetMapping("/unread-count")
    @Operation(summary = "未读消息数")
    public R<Long> unreadCount() {
        Long myId = BaseContext.getUserId();
        long count = chatMessageRepository.countByReceiverIdAndIsRead(myId, false);
        return R.success(count);
    }

    // ==================== 私有方法 ====================

    private void markAsRead(Long receiverId, Long senderId) {
        Query q = new Query(
                Criteria.where("receiver_id").is(receiverId)
                        .and("sender_id").is(senderId)
                        .and("is_read").is(false)
        );
        Update update = new Update().set("is_read", true);
        mongoTemplate.updateMulti(q, update, ChatMessage.class);
    }
}
