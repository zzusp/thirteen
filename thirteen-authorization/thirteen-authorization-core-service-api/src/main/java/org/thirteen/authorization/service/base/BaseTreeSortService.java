package org.thirteen.authorization.service.base;

import org.thirteen.authorization.model.vo.base.BaseTreeSortVO;
import org.thirteen.authorization.web.PagerResult;

/**
 * @author Aaron.Sun
 * @description 通用Service层接口（实体类为上下级结构）
 * @date Created in 18:15 2020/1/15
 * @modified by
 */
public interface BaseTreeSortService<VO extends BaseTreeSortVO> extends BaseRecordService<VO> {

    /**
     * 通过编码获取上级节点
     *
     * @param code 编码
     * @return 上级节点信息
     */
    VO findParent(String code);

    /**
     * 通过编码获取所有上级节点信息
     *
     * @param code 编码
     * @return 所有上级节点信息（包括传入节点信息）
     */
    PagerResult<VO> findAllParent(String code);

    /**
     * 通过编码获取所有下级节点信息
     *
     * @param code 编码
     * @return 所有下级节点信息（不包括传入节点信息）,非树形结构
     */
    PagerResult<VO> findAllChildren(String code);

}
