package org.thirteen.authorization.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.thirteen.authorization.model.po.SysUserPO;

/**
 * @author Aaron.Sun
 * @description 用户数据操作层接口
 * @date Created in 21:46 2019/12/19
 * @modified by
 */
@Repository
public interface SysUserRepository extends JpaRepository<SysUserPO, String> {
}
