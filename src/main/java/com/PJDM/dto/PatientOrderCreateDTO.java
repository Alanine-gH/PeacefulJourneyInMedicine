package com.PJDM.dto;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 患者端创建订单 DTO
 */
@Data
public class PatientOrderCreateDTO {
    /** 服务类型（1：全程陪诊；2：代办手续；3：接机服务；4：送机服务） */
    private Byte serviceType;
    /** 医院ID */
    private Long hospitalId;
    /** 医院名称 */
    private String hospitalName;
    /** 科室ID */
    private Long departmentId;
    /** 科室名称 */
    private String departmentName;
    /** 预约时间 */
    private LocalDateTime appointmentTime;
    /** 患者姓名 */
    private String patientName;
    /** 患者电话 */
    private String patientPhone;
    /** 患者身份证 */
    private String patientIdCard;
    /** 服务描述 */
    private String description;
    /** 套餐ID */
    private Long packageId;
    /** 备注 */
    private String remark;
}
