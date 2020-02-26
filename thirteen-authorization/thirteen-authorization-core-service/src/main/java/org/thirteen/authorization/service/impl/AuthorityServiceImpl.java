package org.thirteen.authorization.service.impl;

import org.springframework.stereotype.Service;
import org.thirteen.authorization.common.utils.JwtUtil;
import org.thirteen.authorization.exceptions.BusinessException;
import org.thirteen.authorization.model.vo.SysPermissionVO;
import org.thirteen.authorization.model.vo.SysUserVO;
import org.thirteen.authorization.service.AuthorityService;
import org.thirteen.authorization.service.SysUserService;

/**
 * @author Aaron.Sun
 * @description 权限校验服务接口实现类
 * @date Created in 16:11 2020/2/23
 * @modified By
 */
@Service
public class AuthorityServiceImpl implements AuthorityService {

    private final SysUserService sysUserService;

    public AuthorityServiceImpl(SysUserService sysUserService) {
        this.sysUserService = sysUserService;
    }

    /**
     * 权限验证
     *
     * @param url 需验证的请求路径
     * @return 是否拥有权限
     */
    @Override
    public boolean validate(String url) {
        boolean flag = false;
        try {
            // 获取用户详细信息（包含用户角色、用户权限等信息）
            SysUserVO user = this.sysUserService.findDetailByAccount(JwtUtil.getAccount());
            if (user != null && user.getPermissions() != null && user.getPermissions().size() > 0) {
                for (SysPermissionVO perm : user.getPermissions()) {
                    // 判断请求路径是否与权限中的路径匹配
                    if (url.equals(perm.getUrl())) {
                        flag = true;
                        break;
                    }
                }
            }
        } catch (Exception e) {
            throw new BusinessException("鉴权失败");
        }
        return flag;
    }
}
