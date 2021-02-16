package org.thirteen.datamation.auth.service;

import org.thirteen.datamation.auth.criteria.DmAuthInsert;

/**
 * @author Aaron.Sun
 * @description 用户模块服务
 * @date Created in 17:33 2021/2/13
 * @modified by
 */
public interface DmAuthUserService {

    /**
     * 新增用户信息
     *
     * @param dmAuthInsert 用户信息新增对象
     */
    void insert(DmAuthInsert dmAuthInsert);

}
