package org.thirteen.authorization.repository;

import org.springframework.stereotype.Repository;
import org.thirteen.authorization.model.po.SysRoleApplicationPO;
import org.thirteen.authorization.repository.base.BaseRepository;

/**
 * @author Aaron.Sun
 * @description 角色应用关联数据操作层接口
 * @date Created in 13:58 2020/2/22
 * @modified By
 */
@Repository
public interface SysRoleApplicationRepository extends BaseRepository<SysRoleApplicationPO, String> {
}
