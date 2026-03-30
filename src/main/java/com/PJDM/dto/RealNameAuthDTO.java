package com.PJDM.dto;

import lombok.Data;

/**
 * 实名认证提交 DTO（患者端，三步合并提交）
 */
@Data
public class RealNameAuthDTO {
    /** 用户ID（从token中取，可不传，由后端从token解析） */
    private Long userId;
    /** 真实姓名 */
    private String realName;
    /** 认证类型（1：护照；2：身份证） */
    private Byte authType;
    /** 身份证号（authType=2时必填） */
    private String idCardNumber;
    /** 护照号（authType=1时必填） */
    private String passportNumber;
    /** 证件正面照片URL */
    private String photoFront;
    /** 证件背面照片URL */
    private String photoBack;
    /** 性别 */
    private String gender;
    /** 出生日期 */
    private String birthDate;
    /** 地址 */
    private String address;
    /** 手机号 */
    private String phone;
    /** 银行卡号 */
    private String bankCard;
    /** 银行卡类型 */
    private String bankType;
    /** 紧急联系人 */
    private String emergencyContact;
    /** 紧急联系人电话 */
    private String emergencyPhone;
    /** 短信验证码（用于step3校验） */
    private String verificationCode;
}
