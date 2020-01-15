package org.thirteen.authorization.service.base;

import org.thirteen.authorization.model.params.base.BaseParam;
import org.thirteen.authorization.model.vo.base.BaseVO;
import org.thirteen.authorization.web.PagerResult;

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
    void update(VO model);

    /**
     * 根据主键批量更新，只更新属性值不为null的字段
     *
     * @param models VO对象集合
     */
    void updateAll(List<VO> models);

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
     * 根据ID值获取一条数据
     *
     * @param id 主键
     * @return VO对象
     */
    VO findById(String id);

    /**
     * 获取所有数据
     *
     * @param ids 主键数组
     * @return VO对象集合
     */
    PagerResult<VO> findByIds(List<String> ids);

    /**
     * 获取所有数据
     *
     * @return VO对象集合
     */
    PagerResult<VO> findAll();

    /**
     * 由条件基类获取一条数据
     *
     * @param param 条件基类
     * @return VO对象集合
     */
    VO findOneByParam(BaseParam param);

    /**
     * 由条件基类查询所有数据
     *
     * @param param 条件基类
     * @return VO对象集合
     */
    PagerResult<VO> findAllByParam(BaseParam param);

}
