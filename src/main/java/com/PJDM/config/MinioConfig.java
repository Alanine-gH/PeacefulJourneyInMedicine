package com.PJDM.config;

import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.SetBucketPolicyArgs;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Minio配置类
 *
 * @author Alanine
 */
@Data
@Slf4j
@Configuration
public class MinioConfig {
    /**
     * 访问地址
     */
    @Value("${minio.endpoint}")
    private String endpoint;

    @Value("${minio.port}")
    private int port;

    /**
     * accessKey类似于用户ID，用于唯一标识你的账户
     */
    @Value("${minio.accessKey}")
    private String accessKey;

    /**
     * secretKey是你账户的密码
     */
    @Value("${minio.secretKey}")
    private String secretKey;
    /**
     * 默认存储桶
     */
    @Value("${minio.bucketName}")
    private String bucketName;

    /** 公开读的桶名列表（头像、评价图片等需要前端直接访问的桶） */
    private static final String[] PUBLIC_BUCKETS = {"user-avatars", "evaluation-images"};

    @Bean
    public MinioClient minioClient() {
        MinioClient minioClient = MinioClient.builder()
                .endpoint(endpoint, port, false)
                .credentials(accessKey, secretKey).build();
        // 自动创建公开读桶
        for (String bucket : PUBLIC_BUCKETS) {
            try {
                boolean exists = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucket).build());
                if (!exists) {
                    minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucket).build());
                    log.info("[MinIO] 创建桶: {}", bucket);
                }
                // 设置公开读策略
                String policy = "{\"Version\":\"2012-10-17\",\"Statement\":[{\"Effect\":\"Allow\",\"Principal\":{\"AWS\":[\"*\"]},\"Action\":[\"s3:GetObject\"],\"Resource\":[\"arn:aws:s3:::\" + bucket + \"/*\"]}]}";
                policy = "{\"Version\":\"2012-10-17\",\"Statement\":[{\"Effect\":\"Allow\",\"Principal\":{\"AWS\":[\"*\"]},\"Action\":[\"s3:GetObject\"],\"Resource\":[\"arn:aws:s3:::" + bucket + "/*\"]}]}";
                minioClient.setBucketPolicy(SetBucketPolicyArgs.builder().bucket(bucket).config(policy).build());
                log.info("[MinIO] 桶 {} 已设置为公开读", bucket);
            } catch (Exception e) {
                log.warn("[MinIO] 设置桶 {} 公开读策略失败: {}", bucket, e.getMessage());
            }
        }
        return minioClient;
    }
}
