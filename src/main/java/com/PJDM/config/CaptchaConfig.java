package com.PJDM.config;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

/**
 * 验证码配置
 *
 * @author Alanine
 */
@Configuration
public class CaptchaConfig {

    /**
     * 字符验证码生成器
     */
    @Bean(name = "captchaProducer")
    public DefaultKaptcha captchaProducer() {
        DefaultKaptcha defaultKaptcha = new DefaultKaptcha();
        Properties properties = new Properties();
        // 是否有边框
        properties.setProperty("kaptcha.border", "yes");
        // 边框颜色
        properties.setProperty("kaptcha.border.color", "105,179,90");
        // 字体颜色
        properties.setProperty("kaptcha.textproducer.font.color", "blue");
        // 图片宽度
        properties.setProperty("kaptcha.image.width", "160");
        // 图片高度
        properties.setProperty("kaptcha.image.height", "60");
        // 字体大小
        properties.setProperty("kaptcha.textproducer.font.size", "38");
        // session key
        properties.setProperty("kaptcha.session.key", "code");
        // 验证码长度
        properties.setProperty("kaptcha.textproducer.char.length", "4");
        // 字体
        properties.setProperty("kaptcha.textproducer.font.names", "Arial,Courier");
        // 干扰实现类
        properties.setProperty("kaptcha.noise.impl", "com.google.code.kaptcha.impl.DefaultNoise");
        // 干扰颜色
        properties.setProperty("kaptcha.noise.color", "black");
        // 图片样式 水纹com.google.code.kaptcha.impl.WaterRipple 鱼眼com.google.code.kaptcha.impl.FishEyeGimpy 阴影com.google.code.kaptcha.impl.ShadowGimpy
        properties.setProperty("kaptcha.obscurificator.impl", "com.google.code.kaptcha.impl.ShadowGimpy");
        Config config = new Config(properties);
        defaultKaptcha.setConfig(config);
        return defaultKaptcha;
    }

    /**
     * 数学计算验证码生成器
     */
    @Bean(name = "captchaProducerMath")
    public DefaultKaptcha captchaProducerMath() {
        DefaultKaptcha defaultKaptcha = new DefaultKaptcha();
        Properties properties = new Properties();
        // 是否有边框
        properties.setProperty("kaptcha.border", "yes");
        // 边框颜色
        properties.setProperty("kaptcha.border.color", "105,179,90");
        // 字体颜色
        properties.setProperty("kaptcha.textproducer.font.color", "blue");
        // 图片宽度
        properties.setProperty("kaptcha.image.width", "160");
        // 图片高度
        properties.setProperty("kaptcha.image.height", "60");
        // 字体大小
        properties.setProperty("kaptcha.textproducer.font.size", "35");
        // session key
        properties.setProperty("kaptcha.session.key", "code");
        // 验证码长度（数学表达式如 1+2=? 中的操作数位数）
        properties.setProperty("kaptcha.textproducer.char.length", "6");
        // 字体
        properties.setProperty("kaptcha.textproducer.font.names", "Arial,Courier");
        // 干扰实现类
        properties.setProperty("kaptcha.noise.impl", "com.google.code.kaptcha.impl.NoNoise");
        // 图片样式
        properties.setProperty("kaptcha.obscurificator.impl", "com.google.code.kaptcha.impl.ShadowGimpy");
        // 使用数学表达式文本生成器
        properties.setProperty("kaptcha.textproducer.impl", "com.PJDM.common.MathKaptchaTextCreator");
        Config config = new Config(properties);
        defaultKaptcha.setConfig(config);
        return defaultKaptcha;
    }
}
