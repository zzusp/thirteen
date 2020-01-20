package org.thirteen.authorization.repository;

import org.springframework.stereotype.Repository;
import org.thirteen.authorization.model.po.SysApplicationPO;
import org.thirteen.authorization.repository.base.BaseRepository;

/**
 * @author Aaron.Sun
 * @description 应用数据操作层接口
 * @date Created in 21:38 2020/1/15
 * @modified by
 */
@Repository
public interface SysApplicationRepository extends BaseRepository<SysApplicationPO, String> {
}
