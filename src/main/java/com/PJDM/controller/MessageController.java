package com.PJDM.controller;

import com.PJDM.common.R;
import com.PJDM.dto.MessageQueryDTO;
import com.PJDM.service.ISysMessageService;
import com.PJDM.vo.MessageListVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

/**
 * 消息接口
 */
@Slf4j
@RestController
@Tag(name = "消息管理", description = "用户消息查询与管理")
public class MessageController {

    @Autowired private ISysMessageService messageService;

    @GetMapping("/message/list")
    @Operation(summary = "获取消息列表")
    public R<IPage<MessageListVO>> getMessageList(MessageQueryDTO query) {
        return R.success(messageService.getMessagePage(query));
    }

    @GetMapping("/message/detail/{messageId}")
    @Operation(summary = "获取消息详情")
    public R<MessageListVO> getMessageDetail(@PathVariable Long messageId) {
        return R.success(messageService.getMessageDetail(messageId));
    }

    @PostMapping("/message/read/{messageId}")
    @Operation(summary = "标记消息已读")
    public R<String> markRead(@PathVariable Long messageId) {
        messageService.markRead(messageId);
        return R.success("已读");
    }

    @PostMapping("/message/read-all")
    @Operation(summary = "标记所有消息已读")
    public R<String> markAllRead(
            @RequestParam Long userId,
            @RequestBody(required = false) Map<String, String> body) {
        String type = body != null ? body.get("type") : null;
        messageService.markAllRead(userId, type);
        return R.success("已全部标为已读");
    }

    @DeleteMapping("/message/delete/{messageId}")
    @Operation(summary = "删除消息")
    public R<String> deleteMessage(@PathVariable Long messageId) {
        messageService.deleteMessage(messageId);
        return R.success("删除成功");
    }

    @PostMapping("/message/batch-delete")
    @Operation(summary = "批量删除消息")
    public R<String> batchDelete(
            @RequestParam Long userId,
            @RequestBody Map<String, List<Long>> body) {
        List<Long> ids = body.get("message_ids");
        if (ids == null || ids.isEmpty()) return R.error("消息ID列表不能为空");
        messageService.batchDeleteMessages(userId, ids);
        return R.success("批量删除成功");
    }

    @GetMapping("/message/unread-count")
    @Operation(summary = "获取未读消息数量")
    public R<Long> getUnreadCount(@RequestParam Long userId) {
        return R.success(messageService.getUnreadCount(userId));
    }

    @PostMapping("/message/send")
    @Operation(summary = "发送消息")
    public R<String> sendMessage(
            @RequestParam Long senderId,
            @RequestBody Map<String, Object> body) {
        Long receiverId = Long.valueOf(body.get("receiver_id").toString());
        String content = (String) body.get("content");
        String type = (String) body.getOrDefault("type", "chat");
        messageService.sendMessage(senderId, receiverId, type, null, content, null, null);
        return R.success("发送成功");
    }
}
