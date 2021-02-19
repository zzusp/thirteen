package org.thirteen.datamation.auth.service.impl;

import io.jsonwebtoken.Claims;
import org.springframework.stereotype.Service;
import org.thirteen.datamation.auth.constant.DmAuthCodes;
import org.thirteen.datamation.auth.redis.service.RedisTokenService;
import org.thirteen.datamation.auth.redis.token.RedisToken;
import org.thirteen.datamation.auth.service.DmAuthService;
import org.thirteen.datamation.auth.service.DmAuthUserService;
import org.thirteen.datamation.auth.service.DmLoginService;
import org.thirteen.datamation.core.criteria.DmCriteria;
import org.thirteen.datamation.core.criteria.DmSpecification;
import org.thirteen.datamation.exception.BusinessException;
import org.thirteen.datamation.service.DmService;
import org.thirteen.datamation.util.JwtUtil;
import org.thirteen.datamation.util.Md5Util;
import org.thirteen.datamation.util.WebUtil;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author Aaron.Sun
 * @description 登录模块服务
 * @date Created in 18:12 2021/2/13
 * @modified by
 */
@Service
public class DmLoginServiceImpl implements DmLoginService {

    private final DmService dmService;
    private final DmAuthService dmAuthService;
    private final DmAuthUserService dmAuthUserService;
    private final RedisTokenService redisTokenService;
    @Resource
    private HttpServletRequest httpServletRequest;

    public DmLoginServiceImpl(DmService dmService, DmAuthService dmAuthService, DmAuthUserService dmAuthUserService,
                              RedisTokenService redisTokenService) {
        this.dmService = dmService;
        this.dmAuthService = dmAuthService;
        this.dmAuthUserService = dmAuthUserService;
        this.redisTokenService = redisTokenService;
    }

    @Override
    public String login(String account, String password) {
        // 校验账号密码
        Map<String, Object> user = this.dmService.findOneBySpecification(DmSpecification.of(DmAuthCodes.AUTH_USER)
            .add(DmCriteria.equal(DmAuthCodes.ACCOUNT, account)));
        if (null == user) {
            throw new BusinessException("账号不存在");
        }
        if (DmAuthCodes.STATUS_OFF.equals(user.get(DmAuthCodes.STATUS))) {
            throw new BusinessException("账号已被冻结，请联系管理员");
        }
        if (!Md5Util.encrypt(account, password, user.get(DmAuthCodes.SALT).toString())
            .equals(user.get(DmAuthCodes.PASSWORD).toString())) {
            throw new BusinessException("账号或密码错误");
        }
        // 分配token
        String token = JwtUtil.sign(account);
        // 存储token到redis
        try {
            RedisToken redisToken = new RedisToken();
            Claims claims = JwtUtil.getTokenClaim(token);
            redisToken.setToken(token);
            redisToken.setAccount(account);
            redisToken.setIp(WebUtil.getIpAddr(httpServletRequest));
            redisToken.setSignTime(claims.getIssuedAt());
            redisToken.setLastAccessTime(redisToken.getSignTime());
            this.redisTokenService.put(token, redisToken);
        } catch (Exception e) {
            throw new BusinessException("登录失败，服务器繁忙", e);
        }
        return token;
    }

    @Override
    public Map<String, Object> me() {
        return this.dmAuthUserService.getDetailByAccount(this.dmAuthService.getCurrentAccount());
    }
}
