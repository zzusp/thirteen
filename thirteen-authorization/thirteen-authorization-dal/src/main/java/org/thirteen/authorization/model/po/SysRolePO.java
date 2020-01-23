package org.thirteen.authorization.model.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.thirteen.authorization.model.po.base.BaseRecordPO;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author Aaron.Sun
 * @description 角色实体类
 * @date Created in 11:02 2018/9/14
 * @modified by
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "sys_role")
@org.hibernate.annotations.Table(appliesTo = "sys_role", comment = "角色信息表")
public class SysRolePO extends BaseRecordPO {

    private static final long serialVersionUID = 1L;
    /**
     * 所属组织编码
     */
    @Column(name = "group_code", columnDefinition = "CHAR(20) COMMENT '所属组织编码'")
    private String groupCode;

}