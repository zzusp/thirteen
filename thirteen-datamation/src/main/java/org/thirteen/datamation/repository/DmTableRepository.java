package org.thirteen.datamation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.thirteen.datamation.model.po.DmTablePO;

/**
 * @author Aaron.Sun
 * @description 数据化表信息数据操作层接口
 * @date Created in 15:56 2020/7/27
 * @modified by
 */
@Repository
public interface DmTableRepository extends JpaRepository<DmTablePO, String>, JpaSpecificationExecutor<DmTablePO> {
}
