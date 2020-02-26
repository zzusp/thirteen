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
public class SysRoleVO extends BaseRecordVO {

    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "所属组织", hidden = true)
    private SysGroupVO group;
    @ApiModelProperty(value = "角色下的应用", hidden = true)
    private List<SysApplicationVO> applications;
    @ApiModelProperty(value = "角色下的权限", hidden = true)
    private List<SysPermissionVO> permissions;

}