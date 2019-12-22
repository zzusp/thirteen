package org.thirteen.authorization.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.thirteen.authorization.model.vo.base.BaseTreeSortVO;

import java.util.List;

/**
 * @author Aaron.Sun
 * @description 部门信息VO
 * @date Created in 11:02 2018/9/14
 * @modified by
 */
@ApiModel(value = "部门信息")
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SysDeptVO extends BaseTreeSortVO<String> {

    private static final long serialVersionUID = 1L;
    /**
     * 部门简称
     */
    @ApiModelProperty(value = "部门简称")
    private String shortName;
    /**
     * 0：禁用；1启用
     */
    @ApiModelProperty(value = "是否启用")
    private String isActive;
    /**
     * 部门拥有的角色
     */
    @ApiModelProperty(value = "部门拥有的角色", hidden = true)
    private List<SysRoleVO> roles;
    /**
     * 0：正常；1：删除
     */
    @ApiModelProperty(value = "删除标志 0：正常；1：删除")
    private String delFlag;

}