package com.PJDM.dto;

import lombok.Data;

/**
 * 消息查询 DTO
 */
@Data
public class MessageQueryDTO {
    /** 接收用户ID */
    private Long receiverId;
    /** 消息类型（system/order/chat） */
    private String type;
    /** 消息状态（1：未读；2：已读） */
    private Byte status;
    /** 兼容页码参数（等同 pageNum） */
    private Integer page;
    /** 页码 */
    private Integer pageNum;
    /** 每页数量 */
    private Integer pageSize;
}
