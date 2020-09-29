package org.thirteen.datamation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.thirteen.datamation.model.po.DmRelationPO;
import org.thirteen.datamation.model.po.DmTablePO;

/**
 * @author Aaron.Sun
 * @description 数据化表间关联关系信息数据操作层接口
 * @date Created in 14:30 2020/9/27
 * @modified by
 */
@Repository
public interface DmRelationRepository extends JpaRepository<DmRelationPO, String>, JpaSpecificationExecutor<DmTablePO> {
}
