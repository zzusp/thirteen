package org.thirteen.authorization.repository;

import org.springframework.stereotype.Repository;
import org.thirteen.authorization.model.po.SysUserRolePO;
import org.thirteen.authorization.repository.base.BaseRepository;

/**
 * @author Aaron.Sun
 * @description 用户角色关联数据操作层接口
 * @date Created in 13:58 2020/2/22
 * @modified By
 */
@Repository
public interface SysUserRoleRepository extends BaseRepository<SysUserRolePO, String> {
}
