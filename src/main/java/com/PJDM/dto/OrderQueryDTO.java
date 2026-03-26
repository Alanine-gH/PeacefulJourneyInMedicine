package com.PJDM.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 订单列表查询参数
 *
 * @author Alanine
 */
@Data
@Schema(description = "订单查询参数")
public class OrderQueryDTO {

    @Schema(description = "订单号")
    private String orderNo;

    @Schema(description = "患者姓名/手机号（模糊）")
    private String userName;

    @Schema(description = "陪诊师姓名（模糊）")
    private String accompanistName;

    @Schema(description = "订单类型 1全程陪诊 2代办手续 3接机 4送机")
    private Byte orderType;

    @Schema(description = "订单状态 1待付款 2已付款 3已确认 4服务中 5已完成 6已取消 7退款中 8已退款")
    private Byte orderStatus;

    @Schema(description = "货币类型")
    private String currency;

    @Schema(description = "页码", example = "1")
    private Integer pageNum = 1;

    @Schema(description = "每页条数", example = "10")
    private Integer pageSize = 10;
}
