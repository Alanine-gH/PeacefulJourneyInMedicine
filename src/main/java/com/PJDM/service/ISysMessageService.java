package com.PJDM.service;

import com.PJDM.dto.MessageQueryDTO;
import com.PJDM.pojo.SysMessage;
import com.PJDM.vo.MessageListVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * 系统消息 Service 接口
 */
public interface ISysMessageService extends IService<SysMessage> {

    IPage<MessageListVO> getMessagePage(MessageQueryDTO query);

    MessageListVO getMessageDetail(Long messageId);

    void markRead(Long messageId);

    void markAllRead(Long userId, String type);

    void deleteMessage(Long messageId);

    void batchDeleteMessages(Long userId, List<Long> messageIds);

    Long getUnreadCount(Long userId);

    void sendMessage(Long senderId, Long receiverId, String type, String title, String content, Long relatedId, String relatedType);
}
