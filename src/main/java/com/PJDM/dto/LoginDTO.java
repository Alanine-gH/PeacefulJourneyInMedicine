package com.PJDM.dto;

import lombok.Data;

/**
 * 登录请求 DTO
 *
 * @author Alanine
 */
@Data
public class LoginDTO {

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码（明文，后端加密校验）
     */
    private String password;

    /**
     * 验证码答案
     */
    private String captcha;

    /**
     * 验证码 UUID（对应 Redis key）
     */
    private String captchaKey;
}
