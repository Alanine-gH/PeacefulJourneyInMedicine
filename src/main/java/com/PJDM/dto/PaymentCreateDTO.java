package com.PJDM.dto;

import lombok.Data;
import java.math.BigDecimal;

/**
 * 创建支付订单 DTO
 */
@Data
public class PaymentCreateDTO {
    /** 业务订单号 */
    private String orderNo;
    /** 支付类型（wechat/alipay/cash） */
    private String paymentType;
    /** 支付金额 */
    private BigDecimal amount;
    /** 货币类型 */
    private String currency;
    /** 支付描述 */
    private String description;
}
