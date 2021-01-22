package org.thirteen.datamation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.thirteen.datamation.model.po.DmColumnPO;

import java.util.List;

/**
 * @author Aaron.Sun
 * @description 数据化列信息数据操作层接口
 * @date Created in 15:56 2020/7/27
 * @modified by
 */
@Repository
public interface DmColumnRepository extends JpaRepository<DmColumnPO, String>, JpaSpecificationExecutor<DmColumnPO> {

    /**
     * 根据table编码删除
     *
     * @param tableCode table编码
     */
    void deleteByTableCodeEquals(String tableCode);

    /**
     * 根据table编码集合删除
     *
     * @param tableCodes table编码集合
     */
    void deleteByTableCodeIn(List<String> tableCodes);

    /**
     * 根据table编码查询所有列信息
     *
     * @param tableCode table编码
     * @return 列信息集合
     */
    List<DmColumnPO> findByTableCodeEquals(String tableCode);

}
