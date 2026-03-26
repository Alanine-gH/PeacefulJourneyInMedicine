package com.PJDM.interceptor;

import com.PJDM.common.BaseContext;
import com.PJDM.common.PathMatcher;
import com.PJDM.common.R;
import com.PJDM.untils.JwtUtil;
import com.PJDM.untils.RedisCache;
import com.alibaba.fastjson2.JSON;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.PrintWriter;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/*
 * JWT 登录拦截器
 * - 白名单路径直接放行
 * - 校验 Authorization: Bearer token
 * - 对比 Redis 中存储的 token（支持踢出）
 * - 每次请求刷新 Redis TTL（活跃续期）
 * - 超过 inactive.timeout-minutes 无操作自动登出
 */
 /**
 * @author Alanine
 */
@Slf4j
@Component
public class LoginInterceptor implements HandlerInterceptor {

    @Value("${auth.token.key-prefix}")
    private String tokenKeyPrefix;

    @Value("${auth.token.ttl-hours}")
    private int tokenTtlHours;

    @Value("${auth.inactive.timeout-minutes:20}")
    private int inactiveTimeoutMinutes;

    /** 活跃时间 key 前缀，记录最后活跃时间戳 */
    private static final String ACTIVE_KEY_PREFIX = "user_active:";

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private RedisCache redisCache;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        // 1. 白名单放行
        String requestURI = request.getRequestURI();
        if (PathMatcher.check(requestURI)) {
            return true;
        }

        // 2. 提取 Token
        String token = extractToken(request);
        if (!StringUtils.hasText(token)) {
            writeUnauthorized(response, "未携带 Token，请先登录");
            return false;
        }

        // 3. 解析 JWT
        Map<String, Object> claims;
        try {
            claims = jwtUtil.parseJWTToMap(token);
        } catch (JwtException e) {
            log.warn("Token 解析失败: {}", e.getMessage());
            writeUnauthorized(response, "Token 无效或已过期，请重新登录");
            return false;
        }

        // 4. 获取 userId
        Object userIdObj = claims.get("userId");
        if (userIdObj == null) {
            writeUnauthorized(response, "Token 载荷异常");
            return false;
        }
        Long userId = Long.parseLong(userIdObj.toString());

        // 5. 对比 Redis 中存储的 token（防止退出后 token 仍可用）
        String redisToken = redisCache.getCacheObject(tokenKeyPrefix + userId);
        if (!token.equals(redisToken)) {
            writeUnauthorized(response, "Token 已失效，请重新登录");
            return false;
        }

        // 6. 检查无操作超时
        String activeKey = ACTIVE_KEY_PREFIX + userId;
        Long lastActive = redisCache.getCacheObject(activeKey);
        long now = System.currentTimeMillis();
        if (lastActive != null) {
            long idleMs = now - lastActive;
            long timeoutMs = (long) inactiveTimeoutMinutes * 60 * 1000;
            if (idleMs > timeoutMs) {
                redisCache.deleteObject(tokenKeyPrefix + userId);
                redisCache.deleteObject(activeKey);
                log.info("用户 [{}] 因无操作超过 {} 分钟自动登出", userId, inactiveTimeoutMinutes);
                writeUnauthorized(response, "长时间未操作，已自动登出，请重新登录");
                return false;
            }
        }

        // 7. 刷新活跃时间（滑动窗口续期）
        redisCache.setCacheObject(activeKey, now, inactiveTimeoutMinutes, TimeUnit.MINUTES);
        // 同时给 token 续期
        String tokenVal = redisCache.getCacheObject(tokenKeyPrefix + userId);
        redisCache.setCacheObject(tokenKeyPrefix + userId, tokenVal, tokenTtlHours, TimeUnit.HOURS);

        // 8. 将用户信息存入 ThreadLocal
        BaseContext.setUserId(userId);
        Object usernameObj = claims.get("username");
        if (usernameObj != null) {
            BaseContext.setUsername(usernameObj.toString());
        }

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
                                Object handler, Exception ex) {
        BaseContext.removeUserId();
        BaseContext.removeUsername();
    }

    private String extractToken(HttpServletRequest request) {
        // 优先从 Authorization header 取
        String bearer = request.getHeader("Authorization");
        if (StringUtils.hasText(bearer) && bearer.startsWith("Bearer ")) {
            return bearer.substring(7);
        }
        // 兼容 query param ?token=xxx（用于浏览器直接下载场景）
        String paramToken = request.getParameter("token");
        if (StringUtils.hasText(paramToken)) {
            return paramToken;
        }
        return null;
    }

    private void writeUnauthorized(HttpServletResponse response, String msg) throws Exception {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter writer = response.getWriter();
        writer.write(JSON.toJSONString(R.error(msg)));
        writer.flush();
    }
}
