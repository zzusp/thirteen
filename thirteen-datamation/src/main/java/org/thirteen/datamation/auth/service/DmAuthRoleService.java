package org.thirteen.datamation.auth.service;

import java.util.Map;

/**
 * @author Aaron.Sun
 * @description 角色模块服务
 * @date Created in 10:36 2021/2/20
 * @modified by
 */
public interface DmAuthRoleService {

    /**
     * 角色授权
     *
     * @param model 角色及关联的应用、权限信息
     */
    void authorize(Map<String, Object> model);

}
