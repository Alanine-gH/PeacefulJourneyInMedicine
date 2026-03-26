package com.PJDM.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@Schema(description = "预约列表VO")
public class AppointmentListVO {
    private Long id;
    private String appointmentNo;
    private Long userId;
    private String userName;
    private String userPhone;
    private String userAvatar;
    private Long hospitalId;
    private String hospitalName;
    private Long departmentId;
    private String departmentName;
    private Long expertId;
    private String expertName;
    private LocalDate appointmentDate;
    private LocalTime appointmentTime;
    private Byte appointmentType;
    private Byte appointmentStatus;
    private String visitCardNo;
    private String registrationNo;
    private LocalDateTime checkInTime;
    private Byte followUpNeeded;
    private String diseaseDescription;
    private String remark;
    private LocalDateTime createTime;
}
