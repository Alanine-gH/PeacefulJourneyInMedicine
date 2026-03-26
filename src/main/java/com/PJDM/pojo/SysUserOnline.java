package com.PJDM.pojo;

import lombok.Data;

/**
 * 在线用户信息
 *
 * @author Alanine
 */
@Data
public class SysUserOnline {

    /** Token ID (Redis key 后缀) */
    private String tokenId;

    /** 用户名 */
    private String userName;

    /** 登录IP */
    private String ipaddr;

    /** 登录地点 */
    private String loginLocation;

    /** 浏览器 */
    private String browser;

    /** 操作系统 */
    private String os;

    /** 登录时间 (时间戳) */
    private Long loginTime;

    /** 用户类型 (1患者 2陪诊师 3客服 4管理员) */
    private Integer userType;
}
