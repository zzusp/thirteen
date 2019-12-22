package org.thirteen.authorization.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.thirteen.authorization.model.vo.base.BaseTreeSortVO;

/**
 * @author Aaron.Sun
 * @description 应用信息VO
 * @date Created in 11:02 2018/9/14
 * @modified by
 */
@ApiModel(value = "应用信息")
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SysApplicationVO extends BaseTreeSortVO<String> {

    private static final long serialVersionUID = 1L;
    /**
     * 图标
     */
    @ApiModelProperty(value = "图标")
    private String icon;
    /**
     * 路径
     */
    @ApiModelProperty(value = "路径")
    private String url;
    /**
     * 0：微服务应用；1：应用接口；2：应用菜单；3：应用菜单组
     */
    @ApiModelProperty(value = "应用类型")
    private String type;
    /**
     * 0：禁用；1启用
     */
    @ApiModelProperty(value = "是否启用")
    private String isActive;
    /**
     * 0：正常；1：删除
     */
    @ApiModelProperty(value = "删除标志 0：正常；1：删除")
    private String delFlag;

}