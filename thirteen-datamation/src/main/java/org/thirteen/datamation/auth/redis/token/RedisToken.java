package org.thirteen.datamation.auth.redis.token;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Objects;

/**
 * @author Aaron.Sun
 * @description redis的token，实现token自动续签功能
 * @date Created in 14:31 2020/3/7
 * @modified By
 */
public class RedisToken implements Serializable {

    /**
     * 当前有效的token
     */
    private String token;
    /**
     * 用户账号，用于token续签
     */
    private String account;
    /**
     * 来源IP，可用于异地登录提示
     */
    private String ip;
    /**
     * 首次签发token时间
     */
    private LocalDateTime signTime;
    /**
     * 续签token时间
     */
    private LocalDateTime reSignTime;
    /**
     * 最后一个活跃时间
     */
    private LocalDateTime lastAccessTime;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public LocalDateTime getSignTime() {
        return signTime;
    }

    public void setSignTime(Date signTime) {
        this.signTime = signTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    public void setSignTime(LocalDateTime signTime) {
        this.signTime = signTime;
    }

    public LocalDateTime getReSignTime() {
        return reSignTime;
    }

    public void setReSignTime(Date reSignTime) {
        this.reSignTime = reSignTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    public void setReSignTime(LocalDateTime reSignTime) {
        this.reSignTime = reSignTime;
    }

    public LocalDateTime getLastAccessTime() {
        return lastAccessTime;
    }

    public void setLastAccessTime(LocalDateTime lastAccessTime) {
        this.lastAccessTime = lastAccessTime;
    }

    @Override
    public String toString() {
        return "RedisToken{" +
            "token='" + token + '\'' +
            ", account='" + account + '\'' +
            ", ip='" + ip + '\'' +
            ", signTime=" + signTime +
            ", reSignTime=" + reSignTime +
            ", lastAccessTime=" + lastAccessTime +
            '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RedisToken that = (RedisToken) o;
        return Objects.equals(token, that.token) &&
            Objects.equals(account, that.account) &&
            Objects.equals(ip, that.ip) &&
            Objects.equals(signTime, that.signTime) &&
            Objects.equals(reSignTime, that.reSignTime) &&
            Objects.equals(lastAccessTime, that.lastAccessTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(token, account, ip, signTime, reSignTime, lastAccessTime);
    }
}
