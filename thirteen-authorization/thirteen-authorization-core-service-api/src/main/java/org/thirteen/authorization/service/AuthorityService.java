package org.thirteen.authorization.service;

/**
 * @author Aaron.Sun
 * @description 权限校验服务接口
 * @date Created in 17:37 2019/12/24
 * @modified by
 */
public interface AuthorityService {

    /**
     * 权限验证
     *
     * @param url 需验证的请求路径
     * @return 是否拥有权限
     * @throws Exception 异常
     */
    boolean validate(String url) throws Exception;

}
