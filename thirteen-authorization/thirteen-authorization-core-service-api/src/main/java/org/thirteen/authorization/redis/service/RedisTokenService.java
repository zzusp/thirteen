package org.thirteen.authorization.redis.service;

import org.thirteen.authorization.redis.token.RedisToken;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * @author Aaron.Sun
 * @description redisToken服务接口
 * @date Created in 15:45 2020/3/7
 * @modified By
 */
public interface RedisTokenService {

    /**
     * 设置redisToken
     *
     * @param token      token
     * @param redisToken redisToken
     */
    void put(String token, RedisToken redisToken);

    /**
     * 由token获取redisToken
     *
     * @param token token
     * @return redisToken
     */
    RedisToken get(String token);

    /**
     * 由token删除redisToken
     *
     * @param token token
     */
    void delete(String token);

    /**
     * 当前所有redisToken的token，总数即为当前总在线人数（可能部分用户未正常退出，所以并不准确）
     *
     * @return 所有redisToken的token
     */
    Set<Object> keys();

    /**
     * 判断最后一次访问时间加上过期时间，是否在当前时间之后
     *
     * @param now            当前时间
     * @param lastAccessTime 后一次访问时间
     * @return 是否未过期，即是否可以续签token
     */
    boolean isNotExpired(LocalDateTime now, LocalDateTime lastAccessTime);

}
