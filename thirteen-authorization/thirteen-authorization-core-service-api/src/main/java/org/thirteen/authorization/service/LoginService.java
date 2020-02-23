package org.thirteen.authorization.service;

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

}
