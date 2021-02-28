package org.thirteen.datamation.rent.service;

import org.thirteen.datamation.auth.criteria.DmAuthInsert;
import org.thirteen.datamation.auth.criteria.DmAuthSpecification;
import org.thirteen.datamation.auth.criteria.DmAuthUpdate;
import org.thirteen.datamation.web.PagerResult;

import java.util.Map;

/**
 * @author Aaron.Sun
 * @description 租赁系统通用服务接口
 * @date Created in 21:43 2021/2/28
 * @modified by
 */
public interface RentService {

    /**
     * 新增
     *
     * @param dmAuthInsert 新增参数对象
     */
    void insert(DmAuthInsert dmAuthInsert);

    /**
     * 批量新增
     *
     * @param dmAuthInsert 新增参数对象
     */
    void insertAll(DmAuthInsert dmAuthInsert);

    /**
     * 根据主键更新，只更新属性值不为null的字段
     *
     * @param dmAuthUpdate 更新参数对象
     */
    void update(DmAuthUpdate dmAuthUpdate);

    /**
     * 根据主键批量更新，只更新属性值不为null的字段
     *
     * @param dmAuthUpdate 更新参数对象
     */
    void updateAll(DmAuthUpdate dmAuthUpdate);

    /**
     * 获取所有数据
     *
     * @param tableCode 表名
     * @return VO对象集合
     */
    PagerResult<Map<String, Object>> findAll(String tableCode);

    /**
     * 由条件基类获取一条数据
     *
     * @param dmAuthSpecification 条件基类
     * @return VO对象集合
     */
    Map<String, Object> findOneBySpecification(DmAuthSpecification dmAuthSpecification);

    /**
     * 由条件基类查询所有数据
     *
     * @param dmAuthSpecification 条件基类
     * @return VO对象集合
     */
    PagerResult<Map<String, Object>> findAllBySpecification(DmAuthSpecification dmAuthSpecification);

    /**
     * 检查数据是否已存在
     *
     * @param dmAuthSpecification 条件基类
     * @return 是否存在
     */
    boolean isExist(DmAuthSpecification dmAuthSpecification);

}
