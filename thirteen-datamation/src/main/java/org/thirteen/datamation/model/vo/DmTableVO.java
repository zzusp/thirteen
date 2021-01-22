package org.thirteen.datamation.model.vo;

import org.thirteen.datamation.model.po.DmTablePO;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Aaron.Sun
 * @description 数据化表VO对象
 * @date Created in 16:47 2021/1/22
 * @modified by
 */
public class DmTableVO extends DmTablePO {

    private static final long serialVersionUID = 1L;

    /**
     * po转vo
     *
     * @param po po对象
     * @return vo对象
     */
    public static DmTableVO convert(DmTablePO po) {
        if (po == null) {
            return null;
        }
        DmTableVO vo = new DmTableVO();
        vo.setId(po.getId());
        vo.setCode(po.getCode());
        vo.setName(po.getName());
        vo.setStatus(po.getStatus());
        vo.setCreateBy(po.getCreateBy());
        vo.setCreateTime(po.getCreateTime());
        vo.setUpdateBy(po.getUpdateBy());
        vo.setUpdateTime(po.getUpdateTime());
        vo.setRemark(po.getRemark());
        vo.setVersion(po.getVersion());
        vo.setColumns(po.getColumns());
        return vo;
    }

    /**
     * po集合转vo集合
     *
     * @param pos po集合
     * @return vo集合
     */
    public static List<DmTableVO> convert(List<DmTablePO> pos) {
        if (pos == null || pos.isEmpty()) {
            return new ArrayList<>();
        }
        return pos.stream().map(DmTableVO::convert).collect(Collectors.toList());
    }
}
