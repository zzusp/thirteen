package org.thirteen.datamation.auth.service;

/**
 * @author Aaron.Sun
 * @description 数据化通用鉴权服务接口
 * @date Created in 18:44 2021/2/19
 * @modified by
 */
public interface DmValidateService {

    /**
     * 权限验证
     *
     * @param url 需验证的请求路径
     * @return 是否拥有权限
     */
    boolean validate(String url);
}
