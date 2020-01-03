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
 * @description 用户信息角色关联实体类
 * @date Created in 11:02 2018/9/14
 * @modified by
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "sys_user_role")
@org.hibernate.annotations.Table(appliesTo = "sys_user_role", comment = "用户角色关联表")
public class SysUserRolePO extends BasePO {

    private static final long serialVersionUID = 1L;
    /** 用户编码 */
    @Column(name = "user_code", columnDefinition = "CHAR(20) COMMENT '用户编码'")
    private String userCode;
    /** 角色编码 */
    @Column(name = "role_code", columnDefinition = "CHAR(20) COMMENT '角色编码'")
    private String roleCode;

}