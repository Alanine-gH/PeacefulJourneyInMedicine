package com.PJDM.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;

@Data
@Schema(description = "预约查询参数")
public class AppointmentQueryDTO {
    @Schema(description = "预约编号")
    private String appointmentNo;
    @Schema(description = "患者姓名/手机（模糊）")
    private String userName;
    @Schema(description = "医院ID")
    private Long hospitalId;
    @Schema(description = "专家ID")
    private Long expertId;
    @Schema(description = "预约状态 1待确认 2已确认 3已取消 4已完成 5已过期")
    private Byte appointmentStatus;
    @Schema(description = "预约日期")
    private LocalDate appointmentDate;
    @Schema(description = "页码")
    private Integer pageNum = 1;
    @Schema(description = "每页条数")
    private Integer pageSize = 10;
}
