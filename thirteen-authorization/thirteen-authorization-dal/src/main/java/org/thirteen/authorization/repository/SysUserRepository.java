package org.thirteen.authorization.repository;

import org.springframework.stereotype.Repository;
import org.thirteen.authorization.model.po.SysUserPO;
import org.thirteen.authorization.repository.base.BaseRepository;

/**
 * @author Aaron.Sun
 * @description 用户数据操作层接口
 * @date Created in 21:46 2019/12/19
 * @modified by
 */
@Repository
public interface SysUserRepository extends BaseRepository<SysUserPO, String> {

    /**
     * 由用户账号获取用户信息
     *
     * @param account 用户账号
     * @return 用户信息
     */
    SysUserPO findByAccount(String account);

}
