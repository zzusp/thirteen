package org.thirteen.authorization.service;

import org.thirteen.authorization.model.vo.SysUserVO;

/**
 * @author Aaron.Sun
 * @description 登录服务接口
 * @date Created in 17:37 2019/12/24
 * @modified by
 */
public interface LoginService {

    /**
     * 登录
     *
     * @param account  用户账号
     * @param password 用户密码
     * @return token
     */
    String login(String account, String password);

    /**
     * 获取当前登录用户信息
     *
     * @return 当前登录用户信息
     */
    SysUserVO getCurrentUser();

}
