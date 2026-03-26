package com.PJDM.config;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;

import java.util.Collections;
import java.util.concurrent.TimeUnit;

/**
 * MongoDB 手动配置
 * 绕过 Spring Boot 自动配置的版本检查，兼容本地低版本 MongoDB（3.x / 4.x）
 * 通过 serverSelectionTimeout 缩短连接失败等待时间
 *
 * @author Alanine
 */
@Slf4j
@Configuration
public class MongoConfig {

    @Value("${spring.data.mongodb.host:localhost}")
    private String host;

    @Value("${spring.data.mongodb.port:27017}")
    private int port;

    @Value("${spring.data.mongodb.database:PeacefulJourneyInMedicine}")
    private String database;

    /**
     * 手动创建 MongoClient，禁用 wire version 兼容性检查
     * serverSelectionTimeout=3s 避免长时间等待
     */
    @Bean
    @ConditionalOnMissingBean(MongoClient.class)
    public MongoClient mongoClient() {
        String uri = String.format("mongodb://%s:%d/%s", host, port, database);
        log.info("[MongoDB] 连接地址: {}", uri);

        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(new ConnectionString(uri))
                // 3秒内找不到可用服务器则快速失败，避免启动卡住
                .applyToClusterSettings(b ->
                        b.serverSelectionTimeout(3, TimeUnit.SECONDS))
                .build();

        return MongoClients.create(settings);
    }

    @Bean
    @ConditionalOnMissingBean(MongoDatabaseFactory.class)
    public MongoDatabaseFactory mongoDatabaseFactory(MongoClient mongoClient) {
        return new SimpleMongoClientDatabaseFactory(mongoClient, database);
    }

    /**
     * MongoTemplate：去掉文档中自动写入的 _class 字段
     */
    @Bean
    @ConditionalOnMissingBean(MongoTemplate.class)
    public MongoTemplate mongoTemplate(MongoDatabaseFactory factory) {
        MongoMappingContext mappingContext = new MongoMappingContext();
        MongoCustomConversions conversions = new MongoCustomConversions(Collections.emptyList());
        mappingContext.setSimpleTypeHolder(conversions.getSimpleTypeHolder());
        mappingContext.afterPropertiesSet();

        MappingMongoConverter converter = new MappingMongoConverter(
                new org.springframework.data.mongodb.core.convert.DefaultDbRefResolver(factory),
                mappingContext
        );
        converter.setCustomConversions(conversions);
        // 不写入 _class 字段
        converter.setTypeMapper(new DefaultMongoTypeMapper(null));
        converter.afterPropertiesSet();

        return new MongoTemplate(factory, converter);
    }
}
