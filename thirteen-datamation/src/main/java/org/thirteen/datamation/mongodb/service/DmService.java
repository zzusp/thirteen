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
     * @param collectionName 集合名称
     * @param data 数据
     */
    void insert(String collectionName, DmDataPO data);

    /**
     * 新增
     *
     * @param collectionName 集合名称
     * @param data 数据
     */
    void insert(String collectionName, List<DmDataPO> data);

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
     * 分页查询
     *
     * @param query 查询参数
     * @return 结果
     */
    PagerResult<DmDataPO> page(DmQuery query);

}
