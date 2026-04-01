package com.PJDM.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

/**
 * 验证码工具类
 *
 * @author Alanine
 */
@Component
public class CaptchaHelper {

    /**
     * 验证码缓存前缀
     */
    public static final String CAPTCHA_CODE_KEY = "captcha_codes:";

    /**
     * 验证码有效期（分钟）
     */
    public static final int CAPTCHA_EXPIRATION = 2;

    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * 是否开启验证码
     */
    @Value("${PeacefulJourneyInMedicine.captcha.enabled}")
    private boolean captchaEnabled;

    /**
     * 验证验证码
     *
     * @param uuid 验证码UUID
     * @param code 用户输入的验证码
     * @return 验证结果
     */
    public boolean validate(String uuid, String code) {
        if (!captchaEnabled) {
            return true;
        }
        if (uuid == null || code == null || uuid.isEmpty() || code.isEmpty()) {
            return false;
        }
        String verifyKey = CaptchaHelper.CAPTCHA_CODE_KEY + uuid;
        System.out.println("前端传入的verifyKey: " + verifyKey);
        String captcha = redisTemplate.opsForValue().get(verifyKey);
        System.out.println("Redis拿到的验证码: " + captcha);
        System.out.println("用户输入的验证码: " + code);
        
        // 检查验证码是否存在
        if (captcha == null) {
            System.out.println("验证码不存在或已过期");
            return false;
        }
        
        // 验证后删除验证码
        redisTemplate.delete(verifyKey);
        boolean equalsIgnoreCase = code.equalsIgnoreCase(captcha);
        if (equalsIgnoreCase) {
            System.out.println("验证码验证成功");
        } else {
            System.out.println("验证码验证失败，期望: " + captcha + ", 实际: " + code);
        }
        return equalsIgnoreCase;
    }

    /**
     * 检查验证码是否启用
     *
     * @return 是否启用
     */
    public boolean isCaptchaEnabled() {
        return captchaEnabled;
    }
}
