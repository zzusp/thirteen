package org.thirteen.authorization.model.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.thirteen.authorization.model.po.base.BaseRecordPO;

import javax.persistence.Column;
import javax.persistence.Table;

/**
 * @author Aaron.Sun
 * @description 权限实体类
 * @date Created in 11:02 2018/9/14
 * @modified by
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "`sys_permission`")
public class SysPermissionPO extends BaseRecordPO<String> {

    private static final long serialVersionUID = 1L;
    /**
     * 权限名称
     */
    @Column(name = "`name`")
    private String name;
    /**
     * 显示顺序
     */
    @Column(name = "`sort`")
    protected Integer sort;
    /**
     * 应用ID
     */
    @Column(name = "`application_id`")
    private String applicationId;
    /**
     * 路径
     */
    @Column(name = "`url`")
    private String url;
    /**
     * 0：需登录；1：需认证；2：需授权（注：授权需登陆）
     */
    @Column(name = "`type`")
    private String type;
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