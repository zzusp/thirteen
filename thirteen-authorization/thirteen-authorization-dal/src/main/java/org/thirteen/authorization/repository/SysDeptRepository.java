package org.thirteen.authorization.repository;

import org.springframework.stereotype.Repository;
import org.thirteen.authorization.model.po.SysDeptPO;
import org.thirteen.authorization.repository.base.BaseRepository;

/**
 * @author Aaron.Sun
 * @description 部门数据操作层接口
 * @date Created in 21:17 2020/2/21
 * @modified By
 */
@Repository
public interface SysDeptRepository extends BaseRepository<SysDeptPO, String> {
}
