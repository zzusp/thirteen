package org.thirteen.authorization.repository;

import org.springframework.stereotype.Repository;
import org.thirteen.authorization.model.po.SysLogOperationPO;
import org.thirteen.authorization.repository.base.BaseRepository;

/**
 * @author Aaron.Sun
 * @description 操作数据操作层接口
 * @date Created in 13:45 2020/2/22
 * @modified By
 */
@Repository
public interface SysLogOperationRepository extends BaseRepository<SysLogOperationPO, String> {
}
