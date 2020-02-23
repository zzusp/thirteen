package org.thirteen.authorization.service;

import org.thirteen.authorization.model.vo.SysUserVO;
import org.thirteen.authorization.service.base.BaseRecordService;

/**
 * @author Aaron.Sun
 * @description 用户模块接口
 * @date Created in 21:28 2020/2/21
 * @modified By
 */
public interface SysUserService extends BaseRecordService<SysUserVO> {

    /**
     * 检查用户账号是否已存在
     *
     * @param account 用户账号
     * @return 用户账号是否已存在
     */
    boolean checkAccount(String account);

    /**
     * 由用户账号获取用户信息详情（包含用户角色、用户权限等信息）
     *
     * @param account 用户账号
     * @return 用户信息详情（包含用户角色、用户权限等信息）
     */
    SysUserVO findDetailByAccount(String account);

}
