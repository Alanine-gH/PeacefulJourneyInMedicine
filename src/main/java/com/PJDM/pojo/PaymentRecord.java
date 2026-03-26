package com.PJDM.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

/**
 * 支付记录表
 *
 * @author Alanine
 * @since 2026-03-24
 */
@Getter
@Setter
@TableName("payment_record")
public class PaymentRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 主键ID，自增 */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /** 支付订单号，唯一索引 */
    private String paymentNo;

    /** 业务订单号 */
    private String orderNo;

    /** 用户ID */
    private Long userId;

    /** 支付类型（wechat：微信支付；alipay：支付宝；cash：现金） */
    private String paymentType;

    /** 支付金额 */
    private BigDecimal amount;

    /** 货币类型 */
    private String currency;

    /** 支付描述 */
    private String description;

    /** 第三方交易号 */
    private String transactionId;

    /** 支付状态（1：待支付；2：已支付；3：已取消；4：退款中；5：已退款） */
    private Byte status;

    /** 退款金额 */
    private BigDecimal refundAmount;

    /** 退款原因 */
    private String refundReason;

    /** 退款时间 */
    private LocalDateTime refundTime;

    /** 支付时间 */
    private LocalDateTime payTime;

    /** 创建时间 */
    private LocalDateTime createTime;

    /** 修改时间 */
    private LocalDateTime updateTime;
}
