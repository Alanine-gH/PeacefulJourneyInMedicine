package com.PJDM.vo;

import lombok.Builder;
import lombok.Data;

/**
 * 登录响应 VO
 *
 * @author Alanine
 */
@Data
@Builder
public class LoginVO {

    /**
     * JWT Token
     */
    private String token;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 用户名
     */
    private String username;

    /**
     * 昵称
     */
    private String nickName;

    /**
     * 用户类型（1患者 2陪诊师 3客服 4管理员）
     */
    private Integer userType;

    /**
     * 头像URL
     */
    private String avatarUrl;
}
