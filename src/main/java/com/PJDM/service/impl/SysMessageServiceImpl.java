package com.PJDM.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.PJDM.dto.MessageQueryDTO;
import com.PJDM.exception.ResourceNotFoundException;
import com.PJDM.mapper.SysMessageMapper;
import com.PJDM.pojo.SysMessage;
import com.PJDM.service.ISysMessageService;
import com.PJDM.vo.MessageListVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class SysMessageServiceImpl extends ServiceImpl<SysMessageMapper, SysMessage>
        implements ISysMessageService {

    @Value("${user-module.page.default-size:10}")
    private int defaultPageSize;
    @Value("${user-module.page.max-size:100}")
    private int maxPageSize;

    @Override
    public IPage<MessageListVO> getMessagePage(MessageQueryDTO query) {
        int size = Math.min(query.getPageSize() == null ? defaultPageSize : query.getPageSize(), maxPageSize);
        return baseMapper.selectMessagePage(
                new Page<>(query.getPageNum() == null ? 1 : query.getPageNum(), size), query);
    }

    @Override
    public MessageListVO getMessageDetail(Long messageId) {
        SysMessage msg = getById(messageId);
        if (msg == null) throw new ResourceNotFoundException("消息", messageId);
        MessageListVO vo = toVO(msg);
        // 查看即标记已读
        if (msg.getStatus() != null && msg.getStatus() == 1) {
            markRead(messageId);
            vo.setStatus((byte) 2);
        }
        return vo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void markRead(Long messageId) {
        SysMessage msg = getById(messageId);
        if (msg == null) throw new ResourceNotFoundException("消息", messageId);
        update(new LambdaUpdateWrapper<SysMessage>()
                .eq(SysMessage::getId, messageId)
                .set(SysMessage::getStatus, (byte) 2)
                .set(SysMessage::getReadTime, LocalDateTime.now()));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void markAllRead(Long userId, String type) {
        LambdaUpdateWrapper<SysMessage> wrapper = new LambdaUpdateWrapper<SysMessage>()
                .eq(SysMessage::getReceiverId, userId)
                .eq(SysMessage::getStatus, (byte) 1)
                .set(SysMessage::getStatus, (byte) 2)
                .set(SysMessage::getReadTime, LocalDateTime.now());
        if (StringUtils.hasText(type)) wrapper.eq(SysMessage::getType, type);
        update(wrapper);
        log.info("[消息] 全部已读 userId={} type={}", userId, type);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteMessage(Long messageId) {
        if (getById(messageId) == null) throw new ResourceNotFoundException("消息", messageId);
        removeById(messageId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchDeleteMessages(Long userId, List<Long> messageIds) {
        remove(new LambdaQueryWrapper<SysMessage>()
                .eq(SysMessage::getReceiverId, userId)
                .in(SysMessage::getId, messageIds));
        log.info("[消息] 批量删除 userId={} count={}", userId, messageIds.size());
    }

    @Override
    public Long getUnreadCount(Long userId) {
        return baseMapper.countUnread(userId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void sendMessage(Long senderId, Long receiverId, String type, String title,
                            String content, Long relatedId, String relatedType) {
        SysMessage msg = new SysMessage();
        msg.setSenderId(senderId == null ? 0L : senderId);
        msg.setReceiverId(receiverId);
        msg.setType(type);
        msg.setTitle(title);
        msg.setContent(content);
        msg.setRelatedId(relatedId);
        msg.setRelatedType(relatedType);
        msg.setStatus((byte) 1);
        msg.setCreateTime(LocalDateTime.now());
        save(msg);
    }

    private MessageListVO toVO(SysMessage msg) {
        MessageListVO vo = new MessageListVO();
        vo.setId(msg.getId());
        vo.setReceiverId(msg.getReceiverId());
        vo.setSenderId(msg.getSenderId());
        vo.setType(msg.getType());
        vo.setTitle(msg.getTitle());
        vo.setContent(msg.getContent());
        vo.setRelatedId(msg.getRelatedId());
        vo.setRelatedType(msg.getRelatedType());
        vo.setStatus(msg.getStatus());
        vo.setReadTime(msg.getReadTime());
        vo.setCreateTime(msg.getCreateTime());
        return vo;
    }
}
