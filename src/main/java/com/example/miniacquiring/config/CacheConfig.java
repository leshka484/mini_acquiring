package com.example.miniacquiring.config;

import com.example.miniacquiring.storage.entity.CommissionTypeEntity;
import com.example.miniacquiring.storage.entity.MerchantStatusEntity;
import com.example.miniacquiring.storage.entity.OperationStatusEntity;
import com.example.miniacquiring.storage.entity.OperationTypeEntity;
import java.time.Duration;
import java.util.Map;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.JacksonJsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import tools.jackson.databind.ObjectMapper;

@Configuration
@EnableCaching
public class CacheConfig {

    @Bean
    public RedisCacheManager cacheManager(
            RedisConnectionFactory connectionFactory,
            ObjectMapper objectMapper) {
        var defaultConfig = RedisCacheConfiguration
                .defaultCacheConfig()
                .entryTtl(Duration.ofHours(24));
        var configurations = Map.of(
                "operationTypes",
                cacheConfig(defaultConfig, objectMapper, OperationTypeEntity.class),
                "operationStatuses",
                cacheConfig(defaultConfig, objectMapper, OperationStatusEntity.class),
                "commissionTypes",
                cacheConfig(defaultConfig, objectMapper, CommissionTypeEntity.class),
                "merchantStatuses",
                cacheConfig(defaultConfig, objectMapper, MerchantStatusEntity.class)
        );
        return RedisCacheManager.builder(connectionFactory)
                .cacheDefaults(defaultConfig)
                .withInitialCacheConfigurations(configurations)
                .build();
    }

    private <T> RedisCacheConfiguration cacheConfig(
            RedisCacheConfiguration defaultConfig,
            ObjectMapper objectMapper,
            Class<T> type) {
        var serializer = new JacksonJsonRedisSerializer<>(objectMapper, type);
        return defaultConfig.serializeValuesWith(
                RedisSerializationContext.SerializationPair
                        .fromSerializer(serializer));
    }

}
