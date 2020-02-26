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
 * @description 用户信息实体类
 * @date Created in 11:02 2018/9/14
 * @modified by
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "sys_user")
@org.hibernate.annotations.Table(appliesTo = "sys_user", comment = "用户信息表")
public class SysUserPO extends BaseRecordPO {

    private static final long serialVersionUID = 1L;
    @Column(name = "account", columnDefinition = "VARCHAR(20) NOT NULL COMMENT '帐号'")
    private String account;
    @Column(name = "password", columnDefinition = "VARCHAR(100) NOT NULL COMMENT '密码'")
    private String password;
    @Column(name = "salt", columnDefinition = "VARCHAR(20) NOT NULL COMMENT '盐'")
    private String salt;
    @Column(name = "gender", columnDefinition = "CHAR(20) COMMENT '性别，对应业务类型为gender的业务字典中的code'")
    private String gender;
    @Column(name = "mobile", columnDefinition = "VARCHAR(20) COMMENT '手机'")
    private String mobile;
    @Column(name = "email", columnDefinition = "VARCHAR(50) COMMENT '电子邮箱'")
    private String email;
    @Column(name = "photo", columnDefinition = "VARCHAR(500) COMMENT '头像'")
    private String photo;
    @Column(name = "dept_code", columnDefinition = "CHAR(20) COMMENT '所属部门编码'")
    private String deptCode;
    @Column(name = "group_code", columnDefinition = "CHAR(20) COMMENT '所属组织编码'")
    private String groupCode;

}