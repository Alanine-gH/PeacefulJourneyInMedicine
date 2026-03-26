package com.PJDM.dto;

import lombok.Data;

/**
 * 就诊卡新增 DTO
 */
@Data
public class PatientMedicalCardDTO {
    /** 医院ID */
    private Long hospitalId;
    /** 医院名称 */
    private String hospitalName;
    /** 就诊卡号 */
    private String cardNo;
}
