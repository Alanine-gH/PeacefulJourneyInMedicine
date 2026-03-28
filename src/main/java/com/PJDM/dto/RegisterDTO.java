package com.PJDM.dto;

import lombok.Data;

/**
 * 注册请求 DTO
 *
 * @author Alanine
 */
@Data
public class RegisterDTO {

    /**
     * 用户名（4-20位字母数字下划线）
     */
    private String username;

    /**
     * 密码（8位以上）
     */
    private String password;

    /**
     * 邮箱（可选）
     */
    private String email;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 验证码答案
     */
    private String captcha;

    /**
     * 验证码 UUID（对应 Redis key）
     */
    private String captchaKey;

    /**
     * 用户类型（1：患者；2：陪诊师），默认为1
     */
    private Byte userType;
}
