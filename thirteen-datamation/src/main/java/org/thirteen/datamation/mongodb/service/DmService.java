package org.thirteen.datamation.mongodb.service;

import org.thirteen.datamation.mongodb.model.po.DmDataPO;
import org.thirteen.datamation.mongodb.model.query.DmQuery;
import org.thirteen.datamation.web.PagerResult;

import java.util.List;

/**
 * @author Aaron.Sun
 * @description 封装的访问mongodb的接口
 * @date Created in 13:54 2021/8/12
 * @modified By
 */
public interface DmService {

    /**
     * 新增
     *
     * @param query 新增参数
     */
    void insert(DmQuery query);

    /**
     * 删除
     *
     * @param query 删除参数
     */
    void delete(DmQuery query);

    /**
     * 更新
     *
     * @param query 更新参数
     */
    void update(DmQuery query);

    /**
     * 查询单条数据
     *
     * @param query 查询参数
     * @return 结果
     */
    DmDataPO get(DmQuery query);

    /**
     * 分页查询
     *
     * @param query 查询参数
     * @return 结果
     */
    List<DmDataPO> list(DmQuery query);

    /**
     * 统计
     *
     * @param query 查询参数
     * @return 统计总数
     */
    long count(DmQuery query);

    /**
     * 分页查询
     *
     * @param query 查询参数
     * @return 结果
     */
    PagerResult<DmDataPO> page(DmQuery query);

}
