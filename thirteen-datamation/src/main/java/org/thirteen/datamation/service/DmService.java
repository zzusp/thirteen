package org.thirteen.datamation.service;

import org.thirteen.datamation.core.criteria.*;
import org.thirteen.datamation.web.PagerResult;

import java.util.List;
import java.util.Map;

/**
 * @author Aaron.Sun
 * @description 数据化通用服务接口
 * @date Created in 17:46 2020/9/21
 * @modified by
 */
public interface DmService {

    /**
     * 刷新
     */
    void refresh();

    /**
     * 新增
     *
     * @param dmInsert 新增参数对象
     */
    void insert(DmInsert dmInsert);

    /**
     * 批量新增
     *
     * @param dmInsert 新增参数对象
     */
    void insertAll(DmInsert dmInsert);

    /**
     * 根据主键更新，只更新属性值不为null的字段
     *
     * @param dmUpdate 更新参数对象
     */
    void update(DmUpdate dmUpdate);

    /**
     * 根据主键批量更新，只更新属性值不为null的字段
     *
     * @param dmUpdate 更新参数对象
     */
    void updateAll(DmUpdate dmUpdate);

    /**
     * 根据主键删除
     *
     * @param dmDelete 删除参数对象
     */
    void delete(DmDelete dmDelete);

    /**
     * 根据主键批量删除，一条sql语句，效率高，推荐
     *
     * @param dmDelete 删除参数对象
     */
    void deleteInBatch(DmDelete dmDelete);

    /**
     * 删除所有数据
     *
     * @param dmDelete 删除参数对象
     */
    void deleteAll(DmDelete dmDelete);

    /**
     * 级联删除
     *
     * @param value 键对应的value
     * @param dmLookup 关联
     */
    void delete(Object value, DmLookup dmLookup);

    /**
     * 根据ID值获取一条数据
     *
     * @param tableCode 表名
     * @param id 主键
     * @return VO对象
     */
    Map<String, Object> findById(String tableCode, Object id);

    /**
     * 获取所有数据
     *
     * @param tableCode 表名
     * @param ids 主键数组
     * @return VO对象集合
     */
    List<Map<String, Object>> findByIds(String tableCode, List<Object> ids);

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

    /**
     * 检查数据是否已存在
     *
     * @param dmSpecification 条件基类
     * @return 是否存在
     */
    boolean isExist(DmSpecification dmSpecification);

}
