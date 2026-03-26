package com.PJDM.dto;

import lombok.Data;

/**
 * 实名认证提交 DTO（患者端）
 */
@Data
public class RealNameAuthDTO {
    /** 真实姓名 */
    private String realName;
    /** 身份证号 */
    private String idCard;
    /** 认证类型（1：护照；2：身份证；3：其他） */
    private Byte authType;
    /** 身份证正面照片URL */
    private String idCardFront;
    /** 身份证背面照片URL */
    private String idCardBack;
    /** 护照号 */
    private String passportNumber;
    /** 护照正面URL */
    private String passportPhotoFront;
    /** 护照背面URL */
    private String passportPhotoBack;
    /** 拒绝原因（审核时使用） */
    private String rejectReason;
}
