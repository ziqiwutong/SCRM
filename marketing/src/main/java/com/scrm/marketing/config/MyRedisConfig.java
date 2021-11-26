package com.scrm.marketing.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.DefaultStringRedisConnection;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Component;

/**
 * 配置一个RedisTemplate<String,String>
 *
 * @author fzk
 * @date 2021-11-26 17:11
 */
@Configuration
public class MyRedisConfig {

    @Bean(name = "redisTemplate")
//    @ConditionalOnSingleCandidate(RedisConnectionFactory.class)
    @SuppressWarnings("all")
    public RedisTemplate<String, String> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, String> redisTemplate = new RedisTemplate<>() {
            @Override
            protected RedisConnection preProcessConnection(RedisConnection connection, boolean existingConnection) {
                return new DefaultStringRedisConnection(connection);
            }
        };
        // 配置序列化器
        redisTemplate.setKeySerializer(RedisSerializer.string());
        redisTemplate.setValueSerializer(RedisSerializer.string());
        redisTemplate.setHashKeySerializer(RedisSerializer.string());
        redisTemplate.setHashValueSerializer(RedisSerializer.string());
        // 配置连接工厂
        redisTemplate.setConnectionFactory(connectionFactory);
        redisTemplate.afterPropertiesSet();

        return redisTemplate;
    }
}
