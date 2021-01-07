package org.thirteen.datamation.service;

import org.thirteen.datamation.core.criteria.DmSpecification;
import org.thirteen.datamation.web.PagerResult;

import java.util.List;
import java.util.Map;

/**
 * @author Aaron.Sun
 * @description 数据化通用服务接口
 * @date Created in 17:46 2020/9/21
 * @modified by
 */
public interface DatamationService {

    /**
     * 刷新
     */
    void refresh();

    /**
     * 新增
     *
     * @param tableCode 表名
     * @param model 对象
     */
    void insert(String tableCode, Map<String, Object> model);

    /**
     * 批量新增
     *
     * @param tableCode 表名
     * @param models 对象集合
     */
    void insertAll(String tableCode, List<Map<String, Object>> models);

    /**
     * 根据主键更新，只更新属性值不为null的字段
     *
     * @param tableCode 表名
     * @param model 对象
     */
    void update(String tableCode, Map<String, Object> model);

    /**
     * 根据主键批量更新，只更新属性值不为null的字段
     *
     * @param tableCode 表名
     * @param models VO对象集合
     */
    void updateAll(String tableCode, List<Map<String, Object>> models);

    /**
     * 根据主键删除
     *
     * @param tableCode 表名
     * @param id 主键
     */
    void delete(String tableCode, String id);

    /**
     * 根据主键批量删除，一条sql语句，效率高，推荐
     *
     * @param tableCode 表名
     * @param ids 主键数组
     */
    void deleteInBatch(String tableCode, List<String> ids);

    /**
     * 根据ID值获取一条数据
     *
     * @param tableCode 表名
     * @param id 主键
     * @return VO对象
     */
    Map<String, Object> findById(String tableCode, String id);

    /**
     * 获取所有数据
     *
     * @param tableCode 表名
     * @param ids 主键数组
     * @return VO对象集合
     */
    List<Map<String, Object>> findByIds(String tableCode, List<String> ids);

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
     * @param dmSpecification 条件基类
     * @return VO对象集合
     */
    Map<String, Object> findOneBySpecification(DmSpecification dmSpecification);

    /**
     * 由条件基类查询所有数据
     *
     * @param dmSpecification 条件基类
     * @return VO对象集合
     */
    PagerResult<Map<String, Object>> findAllBySpecification(DmSpecification dmSpecification);

}
