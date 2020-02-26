package org.thirteen.authorization.model.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.thirteen.authorization.model.po.base.BaseTreeSortPO;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author Aaron.Sun
 * @description 部门实体类
 * @date Created in 11:02 2018/9/14
 * @modified by
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "sys_dept")
@org.hibernate.annotations.Table(appliesTo = "sys_dept", comment = "部门信息表")
public class SysDeptPO extends BaseTreeSortPO {

    private static final long serialVersionUID = 1L;
    @Column(name = "short_name", columnDefinition = "VARCHAR(20) COMMENT '部门简称'")
    private String shortName;

}