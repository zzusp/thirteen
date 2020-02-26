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
public class SysApplicationVO extends BaseTreeSortVO {

    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "图标")
    private String icon;
    @ApiModelProperty(value = "路径")
    private String url;
    @ApiModelProperty(value = "应用类型", example = "1")
    private String type;

}