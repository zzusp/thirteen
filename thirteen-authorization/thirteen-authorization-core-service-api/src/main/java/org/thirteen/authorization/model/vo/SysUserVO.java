package org.thirteen.authorization.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.thirteen.authorization.model.vo.base.BaseRecordVO;

import java.util.List;

/**
 * @author Aaron.Sun
 * @description 用户信息VO
 * @date Created in 11:02 2018/9/14
 * @modified by
 */
@ApiModel(value = "用户信息")
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SysUserVO extends BaseRecordVO {

    private static final long serialVersionUID = 1L;
    /**
     * 帐号
     */
    @ApiModelProperty(value = "账号")
    private String account;
    /**
     * 密码
     */
    @ApiModelProperty(value = "密码")
    private String password;
    /**
     * 盐
     */
    @ApiModelProperty(value = "盐")
    private String salt;
    /**
     * 对应业务类型为gender的业务字典中的code
     */
    @ApiModelProperty(value = "性别")
    private String gender;
    /**
     * 手机
     */
    @ApiModelProperty(value = "手机")
    private String mobile;
    /**
     * 电子邮箱
     */
    @ApiModelProperty(value = "电子邮箱")
    private String email;
    /**
     * 头像
     */
    @ApiModelProperty(value = "头像")
    private String photo;
    /**
     * 所属部门
     */
    @ApiModelProperty(value = "所属部门", hidden = true)
    private SysDeptVO dept;
    /**
     * 所属组织
     */
    @ApiModelProperty(value = "所属组织", hidden = true)
    private SysGroupVO group;
    /**
     * 用户拥有的角色
     */
    @ApiModelProperty(value = "用户拥有的角色", hidden = true)
    private List<SysRoleVO> roles;
    /**
     * 角色下的应用
     */
    @ApiModelProperty(value = "用户拥有的应用", hidden = true)
    private List<SysApplicationVO> applications;
    /**
     * 角色下的权限
     */
    @ApiModelProperty(value = "用户拥有的权限", hidden = true)
    private List<SysPermissionVO> permissions;

}