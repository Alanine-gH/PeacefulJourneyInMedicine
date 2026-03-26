package com.PJDM.vo;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 支付记录 VO
 */
@Data
public class PaymentRecordVO {
    private Long id;
    private String paymentNo;
    private String orderNo;
    private Long userId;
    private String paymentType;
    private BigDecimal amount;
    private String currency;
    private String description;
    private String transactionId;
    /** 支付状态（1：待支付；2：已支付；3：已取消；4：退款中；5：已退款） */
    private Byte status;
    private BigDecimal refundAmount;
    private String refundReason;
    private LocalDateTime refundTime;
    private LocalDateTime payTime;
    private LocalDateTime createTime;
}
