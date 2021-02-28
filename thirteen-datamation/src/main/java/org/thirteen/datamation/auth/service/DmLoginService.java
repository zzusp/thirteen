package org.thirteen.datamation.auth.service;

import java.util.Map;

/**
 * @author Aaron.Sun
 * @description 登录模块服务
 * @date Created in 18:12 2021/2/13
 * @modified by
 */
public interface DmLoginService {

    /**
     * 登录
     *
     * @param account  账号
     * @param password 密码
     * @return token
     */
    String login(String account, String password);

    /**
     * 获取当前用户信息
     *
     * @return 当前用户信息
     */
    Map<String, Object> me();

}
