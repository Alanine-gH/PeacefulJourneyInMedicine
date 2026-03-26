package com.PJDM.vo;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class DispatchListVO {
    private Long id;
    private String dispatchNo;
    private Long orderId;
    private String orderNo;
    private Long accompanistId;
    private String accompanistName;
    private String accompanistPhone;
    private String accompanistAvatar;
    private Byte dispatchType;
    private Byte dispatchStatus;
    private LocalDateTime pushTime;
    private LocalDateTime receiveTime;
    private LocalDateTime rejectTime;
    private String rejectReason;
    private LocalDateTime createTime;
}
