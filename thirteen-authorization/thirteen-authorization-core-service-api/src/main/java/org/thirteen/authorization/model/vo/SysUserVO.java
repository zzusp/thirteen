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
    @ApiModelProperty(value = "账号")
    private String account;
    @ApiModelProperty(value = "密码")
    private String password;
    @ApiModelProperty(value = "盐")
    private String salt;
    @ApiModelProperty(value = "性别")
    private String gender;
    @ApiModelProperty(value = "手机")
    private String mobile;
    @ApiModelProperty(value = "电子邮箱")
    private String email;
    @ApiModelProperty(value = "头像")
    private String photo;
    @ApiModelProperty(value = "所属部门", hidden = true)
    private SysDeptVO dept;
    @ApiModelProperty(value = "所属组织", hidden = true)
    private SysGroupVO group;
    @ApiModelProperty(value = "用户拥有的角色", hidden = true)
    private List<SysRoleVO> roles;
    @ApiModelProperty(value = "用户拥有的应用", hidden = true)
    private List<SysApplicationVO> applications;
    @ApiModelProperty(value = "用户拥有的权限", hidden = true)
    private List<SysPermissionVO> permissions;

}