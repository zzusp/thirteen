package org.thirteen.authorization.repository;

import org.springframework.stereotype.Repository;
import org.thirteen.authorization.model.po.SysPermissionPO;
import org.thirteen.authorization.repository.base.BaseRepository;

/**
 * @author Aaron.Sun
 * @description 权限数据操作层接口
 * @date Created in 21:37 2020/2/21
 * @modified By
 */
@Repository
public interface SysPermissionRepository extends BaseRepository<SysPermissionPO, String> {
}
