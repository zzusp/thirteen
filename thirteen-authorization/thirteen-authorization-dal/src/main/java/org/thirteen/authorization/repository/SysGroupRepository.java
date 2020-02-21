package org.thirteen.authorization.repository;

import org.springframework.stereotype.Repository;
import org.thirteen.authorization.model.po.SysGroupPO;
import org.thirteen.authorization.repository.base.BaseRepository;

/**
 * @author Aaron.Sun
 * @description 组织数据操作层接口
 * @date Created in 21:22 2020/2/21
 * @modified By
 */
@Repository
public interface SysGroupRepository extends BaseRepository<SysGroupPO, String> {
}
