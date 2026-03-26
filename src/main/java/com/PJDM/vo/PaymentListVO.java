package com.PJDM.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 支付记录列表 VO
 *
 * @author Alanine
 */
@Data
@Schema(description = "支付记录列表")
public class PaymentListVO {

    @Schema(description = "ID")
    private Long id;

    @Schema(description = "支付流水号")
    private String paymentId;

    @Schema(description = "订单ID")
    private Long orderId;

    @Schema(description = "订单号")
    private String orderNo;

    @Schema(description = "用户ID")
    private Long userId;

    @Schema(description = "用户姓名")
    private String userName;

    @Schema(description = "支付类型 1定金 2尾款 3全额 4退款")
    private Byte paymentType;

    @Schema(description = "支付方式 1信用卡 2支付宝 3微信 4Stripe 5Adyen 6银行转账")
    private Byte paymentMethod;

    @Schema(description = "支付状态 1待支付 2支付中 3成功 4失败 5已退款")
    private Byte paymentStatus;

    @Schema(description = "货币类型")
    private String currency;

    @Schema(description = "支付金额")
    private BigDecimal amount;

    @Schema(description = "人民币金额")
    private BigDecimal rmbAmount;

    @Schema(description = "汇率")
    private BigDecimal exchangeRate;

    @Schema(description = "支付渠道")
    private String paymentChannel;

    @Schema(description = "渠道交易号")
    private String channelTransactionId;

    @Schema(description = "付款人邮箱")
    private String payerEmail;

    @Schema(description = "支付时间")
    private LocalDateTime paymentTime;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;
}
