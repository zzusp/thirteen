package org.thirteen.authorization.service.base;

import org.thirteen.authorization.model.vo.base.BaseVO;

import java.util.List;

/**
 * @author Aaron.Sun
 * @description 通用Service层接口
 * @date Created in 21:42 2018/1/10
 * @modified by
 */
public interface BaseService<VO extends BaseVO> {

    /**
     * 新增
     *
     * @param model VO对象
     */
    void insert(VO model);

    /**
     * 新增，立即刷新到DB
     *
     * @param model VO对象
     */
    void insertAndFlush(VO model);

    /**
     * 批量新增
     *
     * @param models VO对象集合
     */
    void insertAll(List<VO> models);

    /**
     * 根据主键更新，只更新属性值不为null的字段
     *
     * @param model VO对象
     */
    void updateSelective(VO model);

    /**
     * 根据主键更新，立即刷新到DB
     *
     * @param model VO对象
     */
    void updateSelectiveAndFlush(VO model);

    /**
     * 根据主键批量更新，更新全部字段，值为null的字段也会更新
     *
     * @param models VO对象集合
     */
    void updateSelectiveAll(List<VO> models);

    /**
     * 根据主键删除
     *
     * @param id 主键
     */
    void delete(String id);

    /**
     * 根据主键批量删除，一条sql语句，效率高，推荐
     *
     * @param ids 主键数组
     */
    void deleteInBatch(List<String> ids);

    /**
     * 根据ID值获取一个对象
     *
     * @param id 主键
     * @return VO对象
     */
    VO findById(String id);

    /**
     * 获取所有数据
     *
     * @return VO对象集合
     */
    List<VO> findByIds(List<String> ids);

    /**
     * 获取所有数据
     *
     * @return VO对象集合
     */
    List<VO> findAll();

}
