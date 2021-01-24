package org.thirteen.datamation.service;

import org.thirteen.datamation.core.criteria.DmSpecification;
import org.thirteen.datamation.model.vo.DmTableVO;
import org.thirteen.datamation.web.PagerResult;

import java.util.List;

/**
 * @author Aaron.Sun
 * @description 数据化表信息数据服务接口
 * @date Created in 16:35 2021/1/22
 * @modified by
 */
public interface DmTableService {

    /**
     * 检查数据是否已存在
     *
     * @param code 编码
     * @return 是否存在
     */
    boolean isExist(String code);

    /**
     * 新增
     *
     * @param model 对象
     */
    void insert(DmTableVO model);

    /**
     * 根据主键更新，只更新属性值不为null的字段
     *
     * @param model 对象
     */
    void update(DmTableVO model);

    /**
     * 根据主键删除
     *
     * @param id 主键
     */
    void delete(String id);

    /**
     * 根据主键批量删除
     *
     * @param ids 主键数组
     */
    void deleteInBatch(List<String> ids);

    /**
     * 根据ID查询table信息及关联的列信息
     *
     * @param id 主键
     * @return table信息及关联的列信息
     */
    DmTableVO findById(String id);

    /**
     * 由条件基类查询所有数据
     *
     * @param dmSpecification 条件基类
     * @return VO对象集合
     */
    PagerResult<DmTableVO> findAllBySpecification(DmSpecification dmSpecification);

}
