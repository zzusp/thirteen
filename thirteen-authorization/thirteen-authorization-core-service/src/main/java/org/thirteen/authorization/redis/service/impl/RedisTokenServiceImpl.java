package org.thirteen.authorization.redis.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.thirteen.authorization.redis.keys.RedisKey;
import org.thirteen.authorization.redis.service.RedisTokenService;
import org.thirteen.authorization.redis.token.RedisToken;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author Aaron.Sun
 * @description redisToken服务接口实现类
 * @date Created in 15:18 2020/3/7
 * @modified By
 */
@Service
public class RedisTokenServiceImpl implements RedisTokenService {

    @Value("${redis-token-expire}")
    private Integer redisTokenExpire;

    private final RedisTemplate<String, RedisToken> redisTemplate;

    @Autowired
    public RedisTokenServiceImpl(RedisTemplate<String, RedisToken> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * 设置redisToken
     *
     * @param token      token
     * @param redisToken redisToken
     */
    @Override
    public void put(String token, RedisToken redisToken) {
        this.redisTemplate.boundHashOps(RedisKey.REDIS_TKONE_GROUP).put(token, redisToken);
        // 设置失效时间为1小时
        this.redisTemplate.expire(redisToken.getToken(), redisTokenExpire, TimeUnit.SECONDS);
    }

    /**
     * 重新设置redisToken，先删除后插入
     *
     * @param token      token
     * @param redisToken redisToken
     */
    @Override
    public void rePut(String token, RedisToken redisToken) {
        this.delete(token);
        this.put(token, redisToken);
    }

    /**
     * 由token获取redisToken
     *
     * @param token token
     * @return redisToken
     */
    @Override
    public RedisToken get(String token) {
        return (RedisToken) this.redisTemplate.boundHashOps(RedisKey.REDIS_TKONE_GROUP).get(token);
    }

    /**
     * 由token删除redisToken
     *
     * @param token token
     */
    @Override
    public void delete(String token) {
        this.redisTemplate.boundHashOps(RedisKey.REDIS_TKONE_GROUP).delete(token);
    }

    /**
     * 当前所有redisToken的token，总数即为当前总在线人数（可能部分用户未正常退出，所以并不准确）
     *
     * @return 所有redisToken的token
     */
    @Override
    public Set<Object> keys() {
        return this.redisTemplate.boundHashOps(RedisKey.REDIS_TKONE_GROUP).keys();
    }

    /**
     * 判断最后一次访问时间加上过期时间，是否在当前时间之后
     *
     * @param now            当前时间
     * @param lastAccessTime 后一次访问时间
     * @return 是否未过期，即是否可以续签token
     */
    @Override
    public boolean isNotExpired(LocalDateTime now, LocalDateTime lastAccessTime) {
        return now.isBefore(lastAccessTime.plusSeconds(this.redisTokenExpire));
    }
}
