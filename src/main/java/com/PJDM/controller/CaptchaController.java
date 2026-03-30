package com.PJDM.controller;

import com.google.code.kaptcha.Producer;
import com.PJDM.common.CaptchaHelper;
import com.PJDM.common.R;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.FastByteArrayOutputStream;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * 验证码操作处理
 *
 * @author Alanine
 */
@RestController
public class CaptchaController {

    private static final Logger log = LoggerFactory.getLogger(CaptchaController.class);

    @Resource(name = "captchaProducer")
    private Producer captchaProducer;

    @Resource(name = "captchaProducerMath")
    private Producer captchaProducerMath;

    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * 验证码类型 math:数学运算 char:字符
     */
    @Value("${PeacefulJourneyInMedicine.captcha.type:math}")
    private String captchaType;

    /**
     * 是否开启验证码
     */
    @Value("${slimcamp.captcha.enabled:true}")
    private boolean captchaEnabled;

    /**
     * 生成验证码
     */
    @GetMapping("/captcha")
    public R getCode() throws IOException {
        Map<String, Object> result = new HashMap<>();
        result.put("captchaEnabled", captchaEnabled);

        if (!captchaEnabled) {
            return R.success(result);
        }

        // 生成UUID
        String uuid = UUID.randomUUID().toString().replace("-", "");
        String verifyKey = CaptchaHelper.CAPTCHA_CODE_KEY + uuid;
        System.out.println("生成的verifyKey: " + verifyKey);

        String capStr, code;
        BufferedImage image;

        // 根据类型生成验证码
        if ("math".equals(captchaType)) {
            // 数学运算验证码
            String capText = captchaProducerMath.createText();
            capStr = capText.substring(0, capText.lastIndexOf("@"));
            code = capText.substring(capText.lastIndexOf("@") + 1);
            image = captchaProducerMath.createImage(capStr);
        } else {
            // 字符验证码
            capStr = code = captchaProducer.createText();
            image = captchaProducer.createImage(capStr);
        }


        // 保存验证码到Redis
        redisTemplate.opsForValue().set(verifyKey, code, CaptchaHelper.CAPTCHA_EXPIRATION, TimeUnit.MINUTES);

        // 转换图片为Base64
        FastByteArrayOutputStream os = new FastByteArrayOutputStream();
        try {
            ImageIO.write(image, "jpg", os);
        } catch (IOException e) {
            log.error("验证码图片生成失败", e);
            return R.error("验证码生成失败");
        }

        result.put("uuid", uuid);
        result.put("img", Base64.getEncoder().encodeToString(os.toByteArray()));
        System.out.println("验证码: " + code);
        return R.success(result);
    }
}
