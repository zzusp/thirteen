package org.thirteen.datamation.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * @author Aaron.Sun
 * @description redis配置
 * @date Created in 13:06 2018/4/15
 * @modified by
 */
@Configuration
public class RedisConfig {

    /**
     * redis 模板
     *
     * @return redisTemplate
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    @Bean
    public RedisTemplate redisTemplate(JedisConnectionFactory factory) {
        RedisTemplate redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(factory);
        redisTemplate.setKeySerializer(stringRedisSerializer());
        // 解决读取int类型value值报错的问题
        redisTemplate.setValueSerializer(stringRedisSerializer());
        redisTemplate.setHashKeySerializer(stringRedisSerializer());
        return redisTemplate;
    }

    /**
     * 字符串序列化
     *
     * @return redis字符串序列化
     */
    private StringRedisSerializer stringRedisSerializer() {
        return new StringRedisSerializer();
    }

}
