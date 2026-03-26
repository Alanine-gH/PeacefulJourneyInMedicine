package com.PJDM.service;

import com.PJDM.pojo.SysUserOnline;

import java.util.Map;

/**
 * 在线用户 服务接口
 *
 * @author Alanine
 */
public interface ISysUserOnlineService {

    /**
     * 通过登录IP查询在线用户
     */
    SysUserOnline selectOnlineByIpaddr(String ipaddr, Map<String, Object> claims);

    /**
     * 通过用户名查询在线用户
     */
    SysUserOnline selectOnlineByUserName(String userName, Map<String, Object> claims);

    /**
     * 通过IP和用户名查询在线用户
     */
    SysUserOnline selectOnlineByInfo(String ipaddr, String userName, Map<String, Object> claims);

    /**
     * 将 JWT claims 转换为在线用户对象
     */
    SysUserOnline claimsToUserOnline(Map<String, Object> claims);
}
