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
 * @description 角色模块关联实体类
 * @date Created in 11:02 2018/9/14
 * @modified by
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "sys_role_application")
@org.hibernate.annotations.Table(appliesTo = "sys_role_application", comment = "角色应用关联表")
public class SysRoleApplicationPO extends BasePO {

    private static final long serialVersionUID = 1L;
    /** 角色编码 */
    @Column(name = "role_code", columnDefinition = "CHAR(20) COMMENT '角色编码'")
    private String roleCode;
    /** 应用编码 */
    @Column(name = "application_code", columnDefinition = "CHAR(20) COMMENT '应用编码'")
    private String applicationCode;

}