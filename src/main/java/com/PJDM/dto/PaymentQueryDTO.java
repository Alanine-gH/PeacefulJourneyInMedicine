package com.PJDM.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 支付记录查询参数
 *
 * @author Alanine
 */
@Data
@Schema(description = "支付记录查询参数")
public class PaymentQueryDTO {

    @Schema(description = "订单号")
    private String orderNo;

    @Schema(description = "支付流水号")
    private String paymentId;

    @Schema(description = "用户姓名（模糊）")
    private String userName;

    @Schema(description = "支付状态 1待支付 2支付中 3成功 4失败 5已退款")
    private Byte paymentStatus;

    @Schema(description = "支付方式 1信用卡 2支付宝 3微信 4Stripe 5Adyen 6银行转账")
    private Byte paymentMethod;

    @Schema(description = "页码", example = "1")
    private Integer pageNum = 1;

    @Schema(description = "每页条数", example = "10")
    private Integer pageSize = 10;
}
