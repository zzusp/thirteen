package org.thirteen.authorization.model.po.base;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;

/**
 * @author Aaron.Sun
 * @description 上下级结构的实体类的基类
 * @date Created in 17:18 2018/1/11
 * @modified by
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class BaseTreeSortPO<PK> extends BaseRecordPO<PK> {

    private static final long serialVersionUID = 1L;
    /**
     * 节点名称
     */
    @Column(name = "`name`")
    protected String name;
    /**
     * 显示顺序
     */
    @Column(name = "`sort`")
    protected Integer sort;
    /**
     * 上级编码
     */
    @Column(name = "`parent_code`")
    protected String parentCode;

}
