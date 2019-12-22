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
 * @description 角色信息VO
 * @date Created in 11:02 2018/9/14
 * @modified by
 */
@ApiModel(value = "角色信息")
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SysRoleVO extends BaseRecordVO<String> {

    private static final long serialVersionUID = 1L;
    /**
     * 角色名称
     */
    @ApiModelProperty(value = "名称")
    private String name;
    /**
     * 0：禁用；1启用
     */
    @ApiModelProperty(value = "是否启用")
    private String isActive;
    /**
     * 所属组织
     */
    @ApiModelProperty(value = "所属组织", hidden = true)
    private SysGroupVO group;
    /**
     * 角色下的应用
     */
    @ApiModelProperty(value = "角色下的应用", hidden = true)
    private List<SysApplicationVO> applications;
    /**
     * 角色下的权限
     */
    @ApiModelProperty(value = "角色下的权限", hidden = true)
    private List<SysPermissionVO> permissions;
    /**
     * 0：正常；1：删除
     */
    @ApiModelProperty(value = "删除标志 0：正常；1：删除")
    private String delFlag;

}