package com.PJDM.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.PJDM.common.CaptchaHelper;
import com.PJDM.dto.LoginDTO;
import com.PJDM.dto.RegisterDTO;
import com.PJDM.mapper.UserUserMapper;
import com.PJDM.pojo.UserUser;
import com.PJDM.service.IAuthService;
import com.PJDM.untils.JwtUtil;
import com.PJDM.untils.RedisCache;
import com.PJDM.untils.RegexUtils;
import com.PJDM.vo.LoginVO;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 用户认证 Service 实现类
 *
 * @author Alanine
 */
@Slf4j
@Service
public class AuthServiceImpl extends ServiceImpl<UserUserMapper, UserUser> implements IAuthService {

    @Value("${auth.token.key-prefix}")
    private String tokenKeyPrefix;

    @Value("${auth.token.ttl-hours}")
    private int tokenTtlHours;

    @Value("${auth.login.fail-key-prefix}")
    private String loginFailPrefix;

    @Value("${auth.login.max-fail-count}")
    private int maxFailCount;

    @Value("${auth.login.lock-minutes}")
    private int lockMinutes;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private CaptchaHelper captchaHelper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // ==================== 登录 ====================

    @Override
    public LoginVO login(LoginDTO dto, HttpServletRequest request) {
        if (!StringUtils.hasText(dto.getUsername())) {
            throw new RuntimeException("用户名不能为空");
        }
        if (!StringUtils.hasText(dto.getPassword())) {
            throw new RuntimeException("密码不能为空");
        }

        // 验证码校验
        if (!captchaHelper.validate(dto.getCaptchaKey(), dto.getCaptcha())) {
            throw new RuntimeException("验证码错误或已过期");
        }

        // 检查账号是否被锁定
        String lockKey = loginFailPrefix + dto.getUsername();
        Integer failCount = redisCache.getCacheObject(lockKey);
        if (failCount != null && failCount >= maxFailCount) {
            throw new RuntimeException("账号已被锁定，请 " + lockMinutes + " 分钟后重试");
        }

        // 查询用户
        UserUser user = getOne(new LambdaQueryWrapper<UserUser>()
                .eq(UserUser::getUsername, dto.getUsername()));
        if (user == null) {
            recordFailAndThrow(lockKey, "用户名或密码错误");
            return null; // unreachable, 消除编译器 null 警告
        }

        // 校验账号状态
        if (user.getStatus() != null && user.getStatus() != 1) {
            throw new RuntimeException("账号已被停用，请联系管理员");
        }

        // 校验密码
        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            recordFailAndThrow(lockKey, "用户名或密码错误");
        }

        // 登录成功，清除失败次数
        redisCache.deleteObject(lockKey);

        // 生成 JWT Token
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", user.getId());
        claims.put("username", user.getUsername());
        claims.put("userType", user.getUserType());
        // 存入客户端信息，供在线用户监控使用
        String ipaddr = getClientIp(request);
        String ua = request.getHeader("User-Agent");
        claims.put("ipaddr", ipaddr);
        claims.put("browser", parseBrowser(ua));
        claims.put("os", parseOs(ua));
        claims.put("loginTime", System.currentTimeMillis());
        String token = jwtUtil.createJWT(claims);

        // Token 存入 Redis
        redisCache.setCacheObject(tokenKeyPrefix + user.getId(), token, tokenTtlHours, TimeUnit.HOURS);

        // 更新最后登录时间
        UserUser update = new UserUser();
        update.setId(user.getId());
        update.setLastLoginTime(LocalDateTime.now());
        updateById(update);

        log.info("用户 [{}] 登录成功", user.getUsername());

        return LoginVO.builder()
                .token(token)
                .userId(user.getId())
                .username(user.getUsername())
                .nickName(user.getNickName())
                .userType(user.getUserType() != null ? user.getUserType().intValue() : null)
                .avatarUrl(user.getAvatarUrl())
                .build();
    }

    // ==================== 注册 ====================

    @Override
    public void register(RegisterDTO dto) {
        if (!StringUtils.hasText(dto.getUsername())) {
            throw new RuntimeException("用户名不能为空");
        }
        if (dto.getUsername().length() < 4 || dto.getUsername().length() > 20) {
            throw new RuntimeException("用户名长度须在 4-20 位之间");
        }
        if (!dto.getUsername().matches("^[a-zA-Z0-9_]+$")) {
            throw new RuntimeException("用户名只能包含字母、数字和下划线");
        }
        if (!StringUtils.hasText(dto.getPassword())) {
            throw new RuntimeException("密码不能为空");
        }
        if (dto.getPassword().length() < 8) {
            throw new RuntimeException("密码长度不能少于 8 位");
        }
        if (StringUtils.hasText(dto.getEmail()) && !RegexUtils.isEmail(dto.getEmail())) {
            throw new RuntimeException("邮箱格式不正确");
        }

        // 验证码校验
        if (!captchaHelper.validate(dto.getCaptchaKey(), dto.getCaptcha())) {
            throw new RuntimeException("验证码错误或已过期");
        }

        // 检查用户名是否已存在
        if (count(new LambdaQueryWrapper<UserUser>().eq(UserUser::getUsername, dto.getUsername())) > 0) {
            throw new RuntimeException("用户名已存在");
        }

        // 检查邮箱是否已存在
        if (StringUtils.hasText(dto.getEmail()) &&
                count(new LambdaQueryWrapper<UserUser>().eq(UserUser::getEmail, dto.getEmail())) > 0) {
            throw new RuntimeException("邮箱已被注册");
        }

        // 保存用户
        UserUser user = new UserUser();
        user.setUsername(dto.getUsername());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setEmail(dto.getEmail());
        user.setNickName(dto.getUsername());
        user.setPhone(dto.getPhone());
        user.setUserType((byte) 1); // 默认注册为患者，陪诊师/管理员由后台设置
        user.setStatus((byte) 1);
        user.setCreateTime(LocalDateTime.now());
        user.setCreateBy(dto.getUsername());
        save(user);

        log.info("用户 [{}] 注册成功", dto.getUsername());
    }

    // ==================== 退出 ====================

    @Override
    public void logout(String token) {
        if (!StringUtils.hasText(token)) return;
        try {
            Map<String, Object> claims = jwtUtil.parseJWTToMap(token);
            Object userIdObj = claims.get("userId");
            if (userIdObj != null) {
                Long userId = Long.parseLong(userIdObj.toString());
                redisCache.deleteObject(tokenKeyPrefix + userId);
                log.info("用户 [{}] 退出登录", userId);
            }
        } catch (Exception e) {
            log.warn("退出登录解析 token 失败: {}", e.getMessage());
        }
    }

    // ==================== 私有方法 ====================

    private void recordFailAndThrow(String lockKey, String msg) {
        Integer count = redisCache.getCacheObject(lockKey);
        int newCount = (count == null ? 0 : count) + 1;
        redisCache.setCacheObject(lockKey, newCount, lockMinutes, TimeUnit.MINUTES);
        if (newCount >= maxFailCount) {
            throw new RuntimeException("密码错误次数过多，账号已锁定 " + lockMinutes + " 分钟");
        }
        throw new RuntimeException(msg + "，还可尝试 " + (maxFailCount - newCount) + " 次");
    }

    private String getClientIp(HttpServletRequest request) {
        String[] headers = {"X-Forwarded-For", "Proxy-Client-IP", "WL-Proxy-Client-IP",
                "HTTP_CLIENT_IP", "HTTP_X_FORWARDED_FOR", "X-Real-IP"};
        for (String header : headers) {
            String ip = request.getHeader(header);
            if (ip != null && !ip.isEmpty() && !"unknown".equalsIgnoreCase(ip)) {
                return ip.split(",")[0].trim();
            }
        }
        return request.getRemoteAddr();
    }

    private String parseBrowser(String ua) {
        if (ua == null) return "Unknown";
        if (ua.contains("Edg")) return "Edge";
        if (ua.contains("Chrome")) return "Chrome";
        if (ua.contains("Firefox")) return "Firefox";
        if (ua.contains("Safari")) return "Safari";
        if (ua.contains("MSIE") || ua.contains("Trident")) return "IE";
        return "Other";
    }

    private String parseOs(String ua) {
        if (ua == null) return "Unknown";
        if (ua.contains("Windows NT")) return "Windows";
        if (ua.contains("Mac OS X")) return "macOS";
        if (ua.contains("Android")) return "Android";
        if (ua.contains("iPhone") || ua.contains("iPad")) return "iOS";
        if (ua.contains("Linux")) return "Linux";
        return "Other";
    }
}
