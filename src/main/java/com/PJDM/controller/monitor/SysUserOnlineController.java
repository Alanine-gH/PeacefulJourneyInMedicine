package com.PJDM.controller.monitor;

import com.PJDM.common.R;
import com.PJDM.pojo.SysUserOnline;
import com.PJDM.service.ISysUserOnlineService;
import com.PJDM.untils.JwtUtil;
import com.PJDM.untils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * 在线用户监控
 *
 * @author Alanine
 */
@RestController
@RequestMapping("/monitor/online")
public class SysUserOnlineController {

    @Value("${auth.token.key-prefix}")
    private String tokenKeyPrefix;

    @Autowired
    private ISysUserOnlineService userOnlineService;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private JwtUtil jwtUtil;

    /**
     * 获取在线用户列表
     * @param ipaddr   可选：按IP过滤
     * @param userName 可选：按用户名过滤
     */
    @GetMapping("/list")
    public R<List<SysUserOnline>> list(
            @RequestParam(required = false) String ipaddr,
            @RequestParam(required = false) String userName) {

        Collection<String> keys = redisCache.keys(tokenKeyPrefix + "*");
        List<SysUserOnline> list = new ArrayList<>();

        for (String key : keys) {
            String token = redisCache.getCacheObject(key);
            if (!StringUtils.hasText(token)) continue;
            try {
                Map<String, Object> claims = jwtUtil.parseJWTToMap(token);
                // 补充 tokenId（Redis key 后缀，用于强退）
                claims.put("tokenId", key.replace(tokenKeyPrefix, ""));
                SysUserOnline online;
                if (StringUtils.hasText(ipaddr) && StringUtils.hasText(userName)) {
                    online = userOnlineService.selectOnlineByInfo(ipaddr, userName, claims);
                } else if (StringUtils.hasText(ipaddr)) {
                    online = userOnlineService.selectOnlineByIpaddr(ipaddr, claims);
                } else if (StringUtils.hasText(userName)) {
                    online = userOnlineService.selectOnlineByUserName(userName, claims);
                } else {
                    online = userOnlineService.claimsToUserOnline(claims);
                }
                if (online != null) list.add(online);
            } catch (Exception ignored) {
                // token 已过期或解析失败，跳过
            }
        }
        Collections.reverse(list);
        return R.success(list);
    }

    /**
     * 强制退出指定用户
     * @param tokenId Redis key 后缀（即 userId）
     */
    @DeleteMapping("/{tokenId}")
    public R<Void> forceLogout(@PathVariable String tokenId) {
        redisCache.deleteObject(tokenKeyPrefix + tokenId);
        return R.success(null);
    }
}
