package com.PJDM.service.impl;

import com.PJDM.pojo.SysUserOnline;
import com.PJDM.service.ISysUserOnlineService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Map;

/**
 * 在线用户 服务实现
 * 基于 Redis 中存储的 JWT claims 构建在线用户信息
 *
 * @author Alanine
 */
@Service
public class SysUserOnlineServiceImpl implements ISysUserOnlineService {

    @Override
    public SysUserOnline selectOnlineByIpaddr(String ipaddr, Map<String, Object> claims) {
        String storedIp = getStr(claims, "ipaddr");
        if (StringUtils.hasText(ipaddr) && ipaddr.equals(storedIp)) {
            return claimsToUserOnline(claims);
        }
        return null;
    }

    @Override
    public SysUserOnline selectOnlineByUserName(String userName, Map<String, Object> claims) {
        String storedName = getStr(claims, "username");
        if (StringUtils.hasText(userName) && userName.equals(storedName)) {
            return claimsToUserOnline(claims);
        }
        return null;
    }

    @Override
    public SysUserOnline selectOnlineByInfo(String ipaddr, String userName, Map<String, Object> claims) {
        String storedIp = getStr(claims, "ipaddr");
        String storedName = getStr(claims, "username");
        if (ipaddr.equals(storedIp) && userName.equals(storedName)) {
            return claimsToUserOnline(claims);
        }
        return null;
    }

    @Override
    public SysUserOnline claimsToUserOnline(Map<String, Object> claims) {
        if (claims == null) return null;
        SysUserOnline online = new SysUserOnline();
        online.setTokenId(getStr(claims, "tokenId"));
        online.setUserName(getStr(claims, "username"));
        online.setIpaddr(getStr(claims, "ipaddr"));
        online.setLoginLocation(getStr(claims, "loginLocation"));
        online.setBrowser(getStr(claims, "browser"));
        online.setOs(getStr(claims, "os"));
        Object loginTime = claims.get("loginTime");
        if (loginTime != null) {
            online.setLoginTime(Long.parseLong(loginTime.toString()));
        }
        Object userType = claims.get("userType");
        if (userType != null) {
            online.setUserType(Integer.parseInt(userType.toString()));
        }
        // loginLocation 若为空，用 IP 简单填充
        if (!StringUtils.hasText(online.getLoginLocation())) {
            String ip = online.getIpaddr();
            if ("127.0.0.1".equals(ip) || "0:0:0:0:0:0:0:1".equals(ip)) {
                online.setLoginLocation("本地");
            } else if (ip != null && !ip.isEmpty()) {
                online.setLoginLocation("未知地点");
            }
        }
        return online;
    }

    private String getStr(Map<String, Object> claims, String key) {
        Object val = claims.get(key);
        return val != null ? val.toString() : "";
    }
}
