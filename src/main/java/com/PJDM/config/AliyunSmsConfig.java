package com.PJDM.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 阿里云短信配置类
 *
 * @author Alanine
 */
@Component
@Data
@ConfigurationProperties(prefix = "aliyun.sms")
public class AliyunSmsConfig {

    private String accessKeyId;

    private String accessKeySecret;

    private String signName;

    private String templateCode;

    private String endpoint;
}
