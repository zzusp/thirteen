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
 * @description 权限实体类
 * @date Created in 11:02 2018/9/14
 * @modified by
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "sys_permission")
@org.hibernate.annotations.Table(appliesTo = "sys_permission", comment = "权限信息表")
public class SysPermissionPO extends BaseRecordPO {

    private static final long serialVersionUID = 1L;
    @Column(name = "application_code", columnDefinition = "CHAR(20) COMMENT '应用编码'")
    private String applicationCode;
    @Column(name = "url", columnDefinition = "VARCHAR(100) COMMENT '路径'")
    private String url;
    @Column(name = "type", columnDefinition = "CHAR(1) COMMENT '权限类型 " +
        "0：需登录；1：需认证；2：需授权（注：授权需登陆）'")
    private String type;

}