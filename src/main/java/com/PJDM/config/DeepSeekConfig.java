package com.PJDM.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.net.http.HttpClient;
import java.time.Duration;

/**
 * DeepSeek AI 配置
 *
 * @author Alanine
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "deepseek")
public class DeepSeekConfig {

    private String apiKey;
    private String baseUrl = "https://api.deepseek.com";
    private String model = "deepseek-chat";
    private Double temperature = 0.7;
    private Integer maxTokens = 2000;

    /** 连接超时（秒） */
    private int connectTimeoutSeconds = 10;

    /** 读取超时（秒），流式场景建议设长 */
    private int readTimeoutSeconds = 120;

    /**
     * 共享 HttpClient 实例（Java 原生，支持 HTTP/2）
     * 连接池复用，避免每次请求重建
     */
    public HttpClient buildHttpClient() {
        return HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(connectTimeoutSeconds))
                .version(HttpClient.Version.HTTP_2)
                .build();
    }
}
