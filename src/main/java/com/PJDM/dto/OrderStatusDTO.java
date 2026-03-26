package com.PJDM.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 订单状态变更参数
 *
 * @author Alanine
 */
@Data
@Schema(description = "订单状态变更参数")
public class OrderStatusDTO {

    @Schema(description = "目标状态", required = true)
    private Byte orderStatus;

    @Schema(description = "取消/退款原因（取消或退款时必填）")
    private String reason;
}
