package org.thirteen.authorization.interceptor;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.thirteen.authorization.common.utils.JwtUtil;
import org.thirteen.authorization.model.vo.SysPermissionVO;
import org.thirteen.authorization.service.AuthorityService;
import org.thirteen.authorization.service.SysPermissionService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.SignatureException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.thirteen.authorization.constant.GlobalConstants.*;

/**
 * @author Aaron.Sun
 * @description JWT token拦截器，拦截请求并校验token
 * @date Created in 15:38 2020/2/23
 * @modified By
 */
public class JwtInterceptor extends HandlerInterceptorAdapter {

    private final SysPermissionService sysPermissionService;
    private final AuthorityService authorityService;

    /** 所有可直接访问的地址 */
    private List<String> openUrlList;
    /** 所有登陆后可访问的地址 */
    private List<String> loginUrlList;
    /** 所有认证后可访问的地址 */
    private List<String> authUrlList;
    /** 所有授权后可访问的地址 */
    private List<String> permsUrlList;

    public JwtInterceptor(SysPermissionService sysPermissionService, AuthorityService authorityService) {
        this.sysPermissionService = sysPermissionService;
        this.authorityService = authorityService;
        this.initFilterChains();
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
        throws Exception {
        boolean flag = false;
        // 地址过滤
        String uri = request.getRequestURI();
        if (this.openUrlList.contains(uri)) {
            flag = true;
        }
        // TODO 需登录判断与需认证判断暂用同一逻辑，待后续拆分
        else if (this.loginUrlList.contains(uri) || this.authUrlList.contains(uri)) {
            // Token 验证
            String token = JwtUtil.getTokenFromRequest(request);
            try {
                JwtUtil.verify(token);
            } catch (Exception e) {
                throw new SignatureException("token失效，请重新登录");
            }
        } else if (this.permsUrlList.contains(uri)) {
            flag = this.authorityService.validate(uri);
        } else {
            flag = true;
        }
        return flag;
    }

    /**
     * 初始化过滤链
     */
    public void initFilterChains() {
        // 初始化地址集合
        this.openUrlList = Arrays.asList("/swagger-ui.html", "/login");
        this.loginUrlList = new ArrayList<>();
        this.authUrlList = new ArrayList<>();
        this.permsUrlList = new ArrayList<>();
        // 所有未删除的权限集合
        List<SysPermissionVO> allPermissionList = this.sysPermissionService.findAll().getList();
        for (SysPermissionVO permission : allPermissionList) {
            // 判断权限是否启用
            if (ACTIVE_ON.equals(permission.getActive())) {
                if (PERMISSION_LOGIN.equals(permission.getType())) {
                    this.loginUrlList.add(permission.getUrl());
                } else if (PERMISSION_AUTHOR.equals(permission.getType())) {
                    this.authUrlList.add(permission.getUrl());
                } else if (PERMISSION_PERMS.equals(permission.getType())) {
                    this.permsUrlList.add(permission.getUrl());
                } else {
                    this.openUrlList.add(permission.getUrl());
                }
            }
        }
    }

}
