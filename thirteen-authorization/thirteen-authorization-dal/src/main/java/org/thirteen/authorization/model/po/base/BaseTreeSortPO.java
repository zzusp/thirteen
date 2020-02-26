package org.thirteen.authorization.model.po.base;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.MappedSuperclass;

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
@MappedSuperclass
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class BaseTreeSortPO extends BaseRecordPO {

    private static final long serialVersionUID = 1L;
    @Column(name = "sort", columnDefinition = "INT COMMENT '显示顺序'")
    protected Integer sort;
    @Column(name = "parent_code", columnDefinition = "CHAR(20) NOT NULL COMMENT '上级编码'")
    protected String parentCode;

}
