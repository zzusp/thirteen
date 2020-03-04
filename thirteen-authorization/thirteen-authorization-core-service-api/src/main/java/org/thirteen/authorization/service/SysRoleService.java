package org.thirteen.authorization.service;

import org.thirteen.authorization.model.vo.SysRoleVO;
import org.thirteen.authorization.service.base.BaseRecordService;

/**
 * @author Aaron.Sun
 * @description 角色模块接口
 * @date Created in 21:44 2020/2/21
 * @modified By
 */
public interface SysRoleService extends BaseRecordService<SysRoleVO> {

    /**
     * 由角色ID获取角色信息包含拥有的模块及权限
     *
     * @param id 角色ID
     * @return 角色信息包含拥有的模块及权限
     */
    SysRoleVO findDetailById(String id);

    /**
     * 角色授权
     *
     * @param model 角色信息（包含应用权限信息）
     */
    void authorize(SysRoleVO model);

}
