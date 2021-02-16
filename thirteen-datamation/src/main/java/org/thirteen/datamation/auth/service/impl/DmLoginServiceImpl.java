package org.thirteen.datamation.auth.service.impl;

import io.jsonwebtoken.Claims;
import org.springframework.stereotype.Service;
import org.thirteen.datamation.auth.constant.DmAuthCodes;
import org.thirteen.datamation.auth.redis.service.RedisTokenService;
import org.thirteen.datamation.auth.redis.token.RedisToken;
import org.thirteen.datamation.auth.service.DmAuthService;
import org.thirteen.datamation.auth.service.DmLoginService;
import org.thirteen.datamation.core.criteria.DmCriteria;
import org.thirteen.datamation.core.criteria.DmLookup;
import org.thirteen.datamation.core.criteria.DmSort;
import org.thirteen.datamation.core.criteria.DmSpecification;
import org.thirteen.datamation.exception.BusinessException;
import org.thirteen.datamation.service.DmService;
import org.thirteen.datamation.util.CollectionUtils;
import org.thirteen.datamation.util.JwtUtil;
import org.thirteen.datamation.util.Md5Util;
import org.thirteen.datamation.util.WebUtil;
import org.thirteen.datamation.web.PagerResult;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    private final RedisTokenService redisTokenService;
    @Resource
    private HttpServletRequest httpServletRequest;

    public DmLoginServiceImpl(DmService dmService, DmAuthService dmAuthService, RedisTokenService redisTokenService) {
        this.dmService = dmService;
        this.dmAuthService = dmAuthService;
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
        String account = this.dmAuthService.getCurrentAccount();
        DmSpecification specification = DmSpecification.of(DmAuthCodes.AUTH_USER)
            .add(DmCriteria.equal(DmAuthCodes.ACCOUNT, account))
            .add(DmLookup.of().localField(DmAuthCodes.DEPT_CODE).from(DmAuthCodes.AUTH_DEPT)
                .foreignField(DmAuthCodes.CODE).as(DmAuthCodes.DEPT_KEY).unwind(true))
            .add(DmLookup.of().localField(DmAuthCodes.GROUP_CODE).from(DmAuthCodes.AUTH_GROUP)
                .foreignField(DmAuthCodes.CODE).as(DmAuthCodes.GROUP_KEY).unwind(true))
            .add(DmLookup.of().localField(DmAuthCodes.ACCOUNT).from(DmAuthCodes.AUTH_USER_ROLE)
                .foreignField(DmAuthCodes.ACCOUNT).as(DmAuthCodes.ROLE_KEY));
        // 获取用户信息
        Map<String, Object> user = this.dmService.findOneBySpecification(specification);
        if (user != null) {
            // 查询用户角色、权限、应用
            List<Map<String, Object>> userRoles = (List<Map<String, Object>>) user.get(DmAuthCodes.ROLE_KEY);
            if (CollectionUtils.isNotEmpty(userRoles)) {
                List<String> roleCodes = userRoles.stream().map(v -> v.get(DmAuthCodes.ROLE_CODE).toString())
                    .collect(Collectors.toList());
                DmSpecification roleSpecification;
                // 判断是否拥有超管角色
                if (roleCodes.contains(DmAuthCodes.ADMIN_CODE)) {
                    roleSpecification = DmSpecification.of(DmAuthCodes.AUTH_ROLE)
                        .add(DmCriteria.in(DmAuthCodes.CODE, roleCodes));
                    // 查询关联角色信息
                    PagerResult<Map<String, Object>> rolePage = this.dmService.findAllBySpecification(roleSpecification);
                    user.put(DmAuthCodes.ROLE_KEY, rolePage.getList());
                    // 查询所有应用、权限
                    user.put(DmAuthCodes.APP_KEY, this.dmService.findAllBySpecification(
                        DmSpecification.of(DmAuthCodes.AUTH_APP).add(DmSort.asc(DmAuthCodes.ORDER_NUM))).getList());
                    user.put(DmAuthCodes.PERMISSION_KEY, this.dmService.findAll(DmAuthCodes.AUTH_PERMISSION).getList());
                } else {
                    roleSpecification = DmSpecification.of(DmAuthCodes.AUTH_ROLE)
                        .add(DmCriteria.in(DmAuthCodes.CODE, roleCodes))
                        .add(DmLookup.of().localField(DmAuthCodes.ROLE_CODE).from(DmAuthCodes.AUTH_ROLE_APP)
                            .foreignField(DmAuthCodes.ROLE_CODE).as(DmAuthCodes.APP_KEY))
                        .add(DmLookup.of().localField(DmAuthCodes.ROLE_CODE).from(DmAuthCodes.AUTH_ROLE_PERMISSION)
                            .foreignField(DmAuthCodes.ROLE_CODE).as(DmAuthCodes.PERMISSION_KEY));
                    // 查询关联角色信息
                    PagerResult<Map<String, Object>> rolePage = this.dmService.findAllBySpecification(roleSpecification);
                    if (CollectionUtils.isNotEmpty(rolePage.getList())) {
                        List<Map<String, Object>> roleApps = new ArrayList<>();
                        List<Map<String, Object>> rolePermissions = new ArrayList<>();
                        for (Map<String, Object> role : rolePage.getList()) {
                            roleApps.addAll((List<Map<String, Object>>) role.remove(DmAuthCodes.APP_KEY));
                            rolePermissions.addAll((List<Map<String, Object>>) role.remove(DmAuthCodes.PERMISSION_KEY));
                        }
                        if (CollectionUtils.isNotEmpty(roleApps)) {
                            // 查询关联应用信息
                            PagerResult<Map<String, Object>> appPage = this.dmService.findAllBySpecification(
                                DmSpecification.of(DmAuthCodes.AUTH_APP).add(DmCriteria.in(DmAuthCodes.CODE,
                                    roleApps.stream().map(v -> v.get(DmAuthCodes.APP_CODE).toString())
                                        .collect(Collectors.toList()))).add(DmSort.asc(DmAuthCodes.ORDER_NUM)));
                            user.put(DmAuthCodes.APP_KEY, appPage.getList());
                        }
                        if (CollectionUtils.isNotEmpty(rolePermissions)) {
                            // 查询关联权限信息
                            PagerResult<Map<String, Object>> permissionPage = this.dmService.findAllBySpecification(
                                DmSpecification.of(DmAuthCodes.AUTH_PERMISSION).add(DmCriteria.in(DmAuthCodes.CODE,
                                    rolePermissions.stream().map(v -> v.get(DmAuthCodes.PERMISSION_CODE).toString())
                                        .collect(Collectors.toList()))));
                            user.put(DmAuthCodes.PERMISSION_KEY, permissionPage.getList());
                        }
                        user.put(DmAuthCodes.ROLE_KEY, rolePage.getList());
                    }
                }
            }
        }
        return user;
    }
}
