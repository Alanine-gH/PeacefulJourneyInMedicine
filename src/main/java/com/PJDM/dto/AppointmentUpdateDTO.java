package com.PJDM.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Schema(description = "预约新增/修改参数")
public class AppointmentUpdateDTO {
    private Long userId;
    private Long hospitalId;
    private String hospitalName;
    private Long departmentId;
    private String departmentName;
    private Long expertId;
    private String expertName;
    private LocalDate appointmentDate;
    private LocalTime appointmentTime;
    private Byte appointmentType;
    private String diseaseDescription;
    private String visitCardNo;
    private String remark;
}
