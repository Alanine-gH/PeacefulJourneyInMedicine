package com.PJDM.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 订单新增/修改参数
 *
 * @author Alanine
 */
@Data
@Schema(description = "订单新增/修改参数")
public class OrderUpdateDTO {

    @Schema(description = "患者用户ID（新增必填）")
    private Long userId;

    @Schema(description = "陪诊师ID")
    private Long accompanistId;

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

    @Schema(description = "货币类型")
    private String currency;

    @Schema(description = "航班号")
    private String flightNumber;

    @Schema(description = "航班到达时间")
    private LocalDateTime flightArrivalTime;

    @Schema(description = "酒店名称")
    private String hotelName;

    @Schema(description = "酒店地址")
    private String hotelAddress;

    @Schema(description = "接机地址")
    private String pickUpAddress;

    @Schema(description = "送机地址")
    private String dropOffAddress;

    @Schema(description = "备注")
    private String remark;
}
