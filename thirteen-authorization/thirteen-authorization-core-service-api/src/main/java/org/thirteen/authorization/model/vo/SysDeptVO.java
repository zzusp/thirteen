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
public class SysDeptVO extends BaseTreeSortVO {

    private static final long serialVersionUID = 1L;
    /**
     * 部门简称
     */
    @ApiModelProperty(value = "部门简称")
    private String shortName;
    /**
     * 部门拥有的角色
     */
    @ApiModelProperty(value = "部门拥有的角色", hidden = true)
    private List<SysRoleVO> roles;

}