package com.PJDM.controller.monitor;

import com.PJDM.common.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * 缓存监控
 *
 * @author Alanine
 */
@RestController
@RequestMapping("/monitor/cache")
@SuppressWarnings({"unchecked", "rawtypes"})
public class CacheController {

    @Autowired
    private RedisTemplate redisTemplate;

    @Value("${auth.token.key-prefix}")
    private String loginTokenKey;

    @Value("${auth.login.fail-key-prefix}")
    private String loginFailKey;

    /**
     * 获取 Redis 基本信息 + 命令统计
     */
    @GetMapping
    public R<Map<String, Object>> getInfo() {
        Properties info = (Properties) redisTemplate.execute(
                (RedisCallback<Object>) connection -> connection.serverCommands().info());
        Properties commandStats = (Properties) redisTemplate.execute(
                (RedisCallback<Object>) connection -> connection.serverCommands().info("commandstats"));
        Object dbSize = redisTemplate.execute(
                (RedisCallback<Object>) connection -> connection.serverCommands().dbSize());

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("info", info);
        result.put("dbSize", dbSize);

        List<Map<String, String>> pieList = new ArrayList<>();
        if (commandStats != null) {
            commandStats.stringPropertyNames().forEach(key -> {
                Map<String, String> data = new LinkedHashMap<>();
                String property = commandStats.getProperty(key);
                data.put("name", key.replace("cmdstat_", ""));
                String calls = "";
                if (property != null && property.contains("calls=")) {
                    int start = property.indexOf("calls=") + 6;
                    int end = property.indexOf(",", start);
                    calls = end > start ? property.substring(start, end) : property.substring(start);
                }
                data.put("value", calls);
                pieList.add(data);
            });
        }
        result.put("commandStats", pieList);
        return R.success(result);
    }

    /**
     * 获取项目已知缓存 Key 前缀列表
     */
    @GetMapping("/names")
    public R<List<Map<String, Object>>> getCacheNames() {
        List<Map<String, Object>> list = new ArrayList<>();
        list.add(buildCacheEntry(loginTokenKey, "用户登录 Token"));
        list.add(buildCacheEntry(loginFailKey, "登录失败计数"));
        list.add(buildCacheEntry("captcha:", "图形验证码"));
        return R.success(list);
    }

    /**
     * 获取指定前缀的所有 Key
     */
    @GetMapping("/keys/{cacheName}")
    public R<Set<String>> getCacheKeys(@PathVariable String cacheName) {
        Set<String> keys = redisTemplate.keys(cacheName + "*");
        return R.success(keys != null ? new TreeSet<>(keys) : Collections.emptySet());
    }

    /**
     * 清除指定前缀的所有 Key
     */
    @DeleteMapping("/clear/{cacheName}")
    public R<Void> clearByName(@PathVariable String cacheName) {
        Set<String> keys = redisTemplate.keys(cacheName + "*");
        if (keys != null && !keys.isEmpty()) {
            redisTemplate.delete(keys);
        }
        return R.success(null);
    }

    /**
     * 清除单个 Key
     */
    @DeleteMapping("/key/{cacheKey}")
    public R<Void> clearByKey(@PathVariable String cacheKey) {
        redisTemplate.delete(cacheKey);
        return R.success(null);
    }

    private Map<String, Object> buildCacheEntry(String prefix, String desc) {
        Set<String> keys = redisTemplate.keys(prefix + "*");
        Map<String, Object> entry = new LinkedHashMap<>();
        entry.put("prefix", prefix);
        entry.put("desc", desc);
        entry.put("count", keys != null ? keys.size() : 0);
        return entry;
    }
}
