package com.PJDM.dto;

import lombok.Data;

/**
 * 陪诊师资格认证提交 DTO（前端 certification.vue 使用）
 */
@Data
public class CompanionCertificationDTO {
    /** 用户ID（前端传 userId，由后端映射到 user_accompanist.user_id） */
    private Long accompanistId;
    /** 真实姓名 */
    private String realName;
    /** 手机号 */
    private String phone;
    /** 性别（1：男；2：女；3：未知） */
    private Byte gender;
    /** 年龄 */
    private Integer age;
    /** 职称 */
    private String professionalTitle;
    /** 医学背景（1：有；2：无） */
    private Byte medicalBackground;
    /** 证书编号 */
    private String certificateNumber;
    /** 证书照片URL */
    private String certificatePhoto;
    /** 无犯罪记录证明URL */
    private String criminalRecordCert;
    /** 营业执照URL */
    private String businessLicense;
    /** 语言能力 */
    private String languageAbility;
    /** 专长 */
    private String specialties;
    /** 备注 */
    private String remark;
}
