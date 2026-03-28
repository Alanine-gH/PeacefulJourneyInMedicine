package com.PJDM.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 订单列表 VO
 *
 * @author Alanine
 */
@Data
@Schema(description = "订单列表返回数据")
public class OrderListVO {

    @Schema(description = "订单ID")
    private Long id;

    @Schema(description = "订单号")
    private String orderNo;

    @Schema(description = "患者ID")
    private Long userId;

    @Schema(description = "患者姓名")
    private String userName;

    @Schema(description = "患者手机号")
    private String userPhone;

    @Schema(description = "患者头像")
    private String userAvatar;

    @Schema(description = "陪诊师ID")
    private Long accompanistId;

    @Schema(description = "陪诊师姓名")
    private String accompanistName;

    @Schema(description = "陪诊师手机号")
    private String accompanistPhone;

    @Schema(description = "订单类型")
    private Byte orderType;

    @Schema(description = "订单状态")
    private Byte orderStatus;

    @Schema(description = "服务开始时间")
    private LocalDateTime serviceStartTime;

    @Schema(description = "服务结束时间")
    private LocalDateTime serviceEndTime;

    @Schema(description = "预约医院")
    private String appointmentHospital;

    @Schema(description = "预约日期")
    private LocalDate appointmentDate;

    @Schema(description = "订单总金额")
    private BigDecimal totalAmount;

    @Schema(description = "实际支付金额")
    private BigDecimal actualPaidAmount;

    @Schema(description = "货币类型")
    private String currency;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;
}
