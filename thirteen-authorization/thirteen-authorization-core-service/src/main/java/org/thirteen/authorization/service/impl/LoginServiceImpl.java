package org.thirteen.authorization.service.impl;

import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thirteen.authorization.common.utils.JwtUtil;
import org.thirteen.authorization.common.utils.Md5Util;
import org.thirteen.authorization.common.utils.WebUtil;
import org.thirteen.authorization.exceptions.BusinessException;
import org.thirteen.authorization.model.po.SysUserPO;
import org.thirteen.authorization.model.vo.SysUserVO;
import org.thirteen.authorization.redis.service.RedisTokenService;
import org.thirteen.authorization.redis.token.RedisToken;
import org.thirteen.authorization.repository.SysUserRepository;
import org.thirteen.authorization.service.LoginService;
import org.thirteen.authorization.service.SysUserService;

import javax.servlet.http.HttpServletRequest;

import static org.thirteen.authorization.constant.GlobalConstants.ACTIVE_ON;

/**
 * @author Aaron.Sun
 * @description 登录模块接口实现类
 * @date Created in 14:15 2020/2/23
 * @modified By
 */
@Service
public class LoginServiceImpl implements LoginService {

    private final SysUserService sysUserService;
    private final SysUserRepository sysUserRepository;
    private final RedisTokenService redisTokenService;
    private final HttpServletRequest httpServletRequest;

    @Autowired
    public LoginServiceImpl(SysUserService sysUserService, SysUserRepository sysUserRepository,
                            RedisTokenService redisTokenService, HttpServletRequest httpServletRequest) {
        this.sysUserService = sysUserService;
        this.sysUserRepository = sysUserRepository;
        this.redisTokenService = redisTokenService;
        this.httpServletRequest = httpServletRequest;
    }

    /**
     * 登录
     *
     * @param account  用户账号
     * @param password 用户密码
     */
    @Override
    public String login(String account, String password) {
        // 校验账号密码
        SysUserPO user = this.sysUserRepository.findByAccount(account);
        if (null == user) {
            throw new BusinessException("账号不存在");
        }
        if (!ACTIVE_ON.equals(user.getActive())) {
            throw new BusinessException("账号已被冻结，请联系管理员");
        }
        if (!Md5Util.encrypt(user.getAccount(), password, user.getSalt()).equals(user.getPassword())) {
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

    /**
     * 获取当前登录用户信息
     *
     * @return 当前登录用户信息
     */
    @Override
    public SysUserVO getCurrentUser() {
        return this.sysUserService.findDetailByAccount(this.sysUserService.getCurrentAccount());
    }
}
