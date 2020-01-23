package org.thirteen.authorization.model.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.thirteen.authorization.model.po.base.BasePO;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author Aaron.Sun
 * @description 部门角色关联实体类
 * @date Created in 11:02 2018/9/14
 * @modified by
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "sys_dept_role")
@org.hibernate.annotations.Table(appliesTo = "sys_dept_role", comment = "部门角色关联表")
public class SysDeptRolePO extends BasePO {

    private static final long serialVersionUID = 1L;
    /**
     * 部门编码
     */
    @Column(name = "dept_code", columnDefinition = "CHAR(20) COMMENT '部门编码'")
    private String deptCode;
    /**
     * 角色编码
     */
    @Column(name = "role_code", columnDefinition = "CHAR(20) COMMENT '角色编码'")
    private String roleCode;

}