package org.thirteen.authorization.model.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.thirteen.authorization.model.po.base.BasePO;

import javax.persistence.Column;
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
@Table(name = "`sys_role_application`")
public class SysRoleApplicationPO extends BasePO<String> {

    private static final long serialVersionUID = 1L;
    /**
     * 角色ID
     */
    @Column(name = "`role_id`")
    private String roleId;
    /**
     * 应用ID
     */
    @Column(name = "`application_id`")
    private String applicationId;

}