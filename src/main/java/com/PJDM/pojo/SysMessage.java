package com.PJDM.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

/**
 * 系统消息表
 *
 * @author Alanine
 * @since 2026-03-24
 */
@Getter
@Setter
@TableName("sys_message")
public class SysMessage implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 主键ID，自增 */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /** 接收用户ID */
    private Long receiverId;

    /** 发送用户ID（0表示系统） */
    private Long senderId;

    /** 消息类型（system：系统消息；order：订单消息；chat：聊天消息） */
    private String type;

    /** 消息标题 */
    private String title;

    /** 消息内容 */
    private String content;

    /** 关联业务ID（如订单ID） */
    private Long relatedId;

    /** 关联业务类型 */
    private String relatedType;

    /** 消息状态（1：未读；2：已读） */
    private Byte status;

    /** 读取时间 */
    private LocalDateTime readTime;

    /** 创建时间 */
    private LocalDateTime createTime;
}
