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
 * @description 用户信息实体类
 * @date Created in 11:02 2018/9/14
 * @modified by
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "`sys_user`")
public class SysUserPO extends BaseRecordPO<String> {

    private static final long serialVersionUID = 1L;
    /**
     * 帐号
     */
    @Column(name = "`account`")
    private String account;
    /**
     * 密码
     */
    @Column(name = "`password`")
    private String password;
    /**
     * 盐
     */
    @Column(name = "`salt`")
    private String salt;
    /**
     * 姓名
     */
    @Column(name = "`name`")
    private String name;
    /**
     * 对应业务类型为gender的业务字典中的code
     */
    @Column(name = "`gender`")
    private String gender;
    /**
     * 手机
     */
    @Column(name = "`mobile`")
    private String mobile;
    /**
     * 电子邮箱
     */
    @Column(name = "`email`")
    private String email;
    /**
     * 头像
     */
    @Column(name = "`photo`")
    private String photo;
    /**
     * 0：禁用；1启用
     */
    @Column(name = "`is_active`")
    private String isActive;
    /**
     * 所属部门ID
     */
    @Column(name = "`dept_id`")
    private String deptId;
    /**
     * 所属组织ID
     */
    @Column(name = "`group_id`")
    private String groupId;
    /**
     * 0：正常；1：删除
     */
    @Column(name = "`del_flag`")
    private String delFlag;

}