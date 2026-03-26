package com.PJDM.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 订单详情 VO（含服务项目列表）
 *
 * @author Alanine
 */
@Data
@Schema(description = "订单详情")
public class OrderDetailVO {

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

    @Schema(description = "服务天数")
    private Integer serviceDays;

    @Schema(description = "服务小时数")
    private Integer serviceHours;

    @Schema(description = "患者就诊姓名")
    private String patientName;

    @Schema(description = "患者性别")
    private Byte patientGender;

    @Schema(description = "患者年龄")
    private Integer patientAge;

    @Schema(description = "患者护照号")
    private String patientPassport;

    @Schema(description = "病情描述")
    private String diseaseDescription;

    @Schema(description = "预约医院")
    private String appointmentHospital;

    @Schema(description = "预约科室")
    private String appointmentDepartment;

    @Schema(description = "预约日期")
    private LocalDate appointmentDate;

    @Schema(description = "订单总金额")
    private BigDecimal totalAmount;

    @Schema(description = "定金金额")
    private BigDecimal depositAmount;

    @Schema(description = "尾款金额")
    private BigDecimal balanceAmount;

    @Schema(description = "实际支付金额")
    private BigDecimal actualPaidAmount;

    @Schema(description = "货币类型")
    private String currency;

    @Schema(description = "航班号")
    private String flightNumber;

    @Schema(description = "酒店名称")
    private String hotelName;

    @Schema(description = "取消原因")
    private String cancelReason;

    @Schema(description = "退款金额")
    private BigDecimal refundAmount;

    @Schema(description = "退款原因")
    private String refundReason;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "服务项目列表")
    private List<ServiceItemVO> serviceItems;

    @Data
    @Schema(description = "服务项目")
    public static class ServiceItemVO {
        private Long id;
        private String serviceCode;
        private String serviceName;
        private Byte serviceType;
        private BigDecimal unitPrice;
        private Integer quantity;
        private String unit;
        private BigDecimal subtotal;
    }
}
