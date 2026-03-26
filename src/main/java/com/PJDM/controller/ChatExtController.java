package com.PJDM.controller;

import com.PJDM.common.R;
import com.PJDM.mongo.document.ChatMessage;
import com.PJDM.mongo.repository.ChatMessageRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

/**
 * 聊天补充接口：会话列表、删除、撤回、客服等
 * 核心收发消息已由 ChatMessageController 处理
 */
@Slf4j
@RestController
@Tag(name = "聊天-补充接口", description = "聊天会话列表、删除、撤回、客服")
public class ChatExtController {

    @Autowired private ChatMessageRepository chatRepo;
    @Autowired private MongoTemplate mongoTemplate;

    /** GET /chat/list - 获取聊天会话列表（重定向到 /chat/sessions） */
    @GetMapping("/chat/list")
    @Operation(summary = "获取聊天会话列表")
    public R<Object> getChatList(
            @RequestParam(required = false) Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int pageSize) {
        // 查询该用户所有消息，提取会话列表
        Query q = new Query(
                new Criteria().orOperator(
                        Criteria.where("sender_id").is(userId),
                        Criteria.where("receiver_id").is(userId)
                )
        ).limit(500);
        List<ChatMessage> msgs = mongoTemplate.find(q, ChatMessage.class);
        // 提取唯一对端用户
        Map<Long, ChatMessage> sessions = new java.util.LinkedHashMap<>();
        for (ChatMessage m : msgs) {
            Long peer = m.getSenderId().equals(userId) ? m.getReceiverId() : m.getSenderId();
            sessions.putIfAbsent(peer, m);
        }
        return R.success(sessions.entrySet().stream().map(e -> {
            long unread = chatRepo.countByReceiverIdAndIsRead(userId, false);
            return Map.of(
                    "peerId", e.getKey(),
                    "lastMessage", e.getValue().getContent(),
                    "lastTime", e.getValue().getCreateTime(),
                    "unreadCount", unread
            );
        }).collect(java.util.stream.Collectors.toList()));
    }

    /** DELETE /chat/delete/{targetId} - 删除与目标用户的聊天记录 */
    @DeleteMapping("/chat/delete/{targetId}")
    @Operation(summary = "删除聊天记录")
    public R<String> deleteChatHistory(
            @PathVariable Long targetId,
            @RequestParam Long userId) {
        Query q = new Query(new Criteria().orOperator(
                Criteria.where("sender_id").is(userId).and("receiver_id").is(targetId),
                Criteria.where("sender_id").is(targetId).and("receiver_id").is(userId)
        ));
        mongoTemplate.remove(q, ChatMessage.class);
        log.info("[聊天] 删除记录 userId={} targetId={}", userId, targetId);
        return R.success("删除成功");
    }

    /** GET /chat/unread-total - 获取未读消息总数 */
    @GetMapping("/chat/unread-total")
    @Operation(summary = "获取未读消息总数")
    public R<Long> getTotalUnread(@RequestParam Long userId) {
        long count = chatRepo.countByReceiverIdAndIsRead(userId, false);
        return R.success(count);
    }

    /** POST /chat/session - 创建聊天会话（幂等，返回成功即可） */
    @PostMapping("/chat/session")
    @Operation(summary = "创建聊天会话")
    public R<String> createSession(@RequestBody Map<String, Object> body) {
        return R.success("会话已就绪");
    }

    /** GET /chat/session/{targetId} - 获取聊天会话信息 */
    @GetMapping("/chat/session/{targetId}")
    @Operation(summary = "获取聊天会话信息")
    public R<Map<String, Object>> getSession(
            @PathVariable Long targetId,
            @RequestParam Long userId) {
        long unread = chatRepo.countByReceiverIdAndIsRead(userId, false);
        return R.success(Map.of(
                "targetId", targetId,
                "unreadCount", unread
        ));
    }

    /** POST /chat/recall/{messageId} - 撤回消息 */
    @PostMapping("/chat/recall/{messageId}")
    @Operation(summary = "撤回消息")
    public R<String> recallMessage(
            @PathVariable String messageId,
            @RequestParam Long userId) {
        Query q = new Query(Criteria.where("_id").is(messageId)
                .and("sender_id").is(userId));
        Update u = new Update().set("content", "[消息已撤回]").set("recalled", true);
        mongoTemplate.updateFirst(q, u, ChatMessage.class);
        return R.success("撤回成功");
    }

    /** POST /chat/send-image - 图片消息上传占位（实际上传由 /upload 接口处理） */
    @PostMapping("/chat/send-image")
    @Operation(summary = "发送图片消息（占位，上传请使用 /upload 接口）")
    public R<String> sendImage() {
        return R.error("请先上传图片获取URL，再通过 /chat/send 发送 type=image 消息");
    }

    // ==================== 客服聊天 ====================

    @GetMapping("/chat/customer-service/info")
    @Operation(summary = "获取客服信息")
    public R<Map<String, Object>> getCustomerServiceInfo() {
        return R.success(Map.of(
                "available", true,
                "onlineCount", 1,
                "waitTime", "约1分钟",
                "serviceHours", "09:00-21:00"
        ));
    }

    @PostMapping("/chat/customer-service/connect")
    @Operation(summary = "连接客服")
    public R<String> connectCustomerService() {
        return R.success("已连接客服，请发送消息");
    }

    @PostMapping("/chat/customer-service/disconnect")
    @Operation(summary = "断开客服连接")
    public R<String> disconnectCustomerService() {
        return R.success("已断开客服连接");
    }

    @GetMapping("/chat/customer-service/faq")
    @Operation(summary = "获取常见问题列表")
    public R<List<Map<String, String>>> getFAQList() {
        return R.success(List.of(
                Map.of("q", "如何预约陪诊服务？", "a", "在首页点击预约，选择医院和时间即可"),
                Map.of("q", "陪诊师会说英语吗？", "a", "我们提供多语种陪诊师，预约时可指定语言"),
                Map.of("q", "如何取消订单？", "a", "在订单列表中找到订单，点击取消即可"),
                Map.of("q", "支持哪些支付方式？", "a", "支持微信支付、支付宝和现金支付")
        ));
    }
}
