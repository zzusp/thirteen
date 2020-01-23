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
 * @description 组织信息VO
 * @date Created in 11:02 2018/9/14
 * @modified by
 */
@ApiModel(value = "组织信息")
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SysGroupVO extends BaseTreeSortVO {

    private static final long serialVersionUID = 1L;
    /**
     * 组织简称
     */
    @ApiModelProperty(value = "组织简称")
    private String shortName;
    /**
     * 组织拥有的角色
     */
    @ApiModelProperty(value = "组织拥有的角色", hidden = true)
    private List<SysRoleVO> roles;

}