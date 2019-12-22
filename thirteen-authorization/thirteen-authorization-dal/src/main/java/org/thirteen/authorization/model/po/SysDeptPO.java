package org.thirteen.authorization.model.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.thirteen.authorization.model.po.base.BaseTreeSortPO;

import javax.persistence.Column;
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
@Table(name = "`sys_dept`")
public class SysDeptPO extends BaseTreeSortPO<String> {

    private static final long serialVersionUID = 1L;
    /**
     * 部门简称
     */
    @Column(name = "`short_name`")
    private String shortName;
    /**
     * 0：禁用；1启用
     */
    @Column(name = "`is_active`")
    private String isActive;
    /**
     * 0：正常；1：删除
     */
    @Column(name = "`del_flag`")
    private String delFlag;

}