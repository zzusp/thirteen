package org.thirteen.authorization.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.thirteen.authorization.model.vo.base.BaseRecordVO;

/**
 * @author Aaron.Sun
 * @description 权限信息VO
 * @date Created in 11:02 2018/9/14
 * @modified by
 */
@ApiModel(value = "权限信息")
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SysPermissionVO extends BaseRecordVO {

    private static final long serialVersionUID = 1L;
    /** 所属应用 */
    @ApiModelProperty(value = "所属应用")
    private SysApplicationVO application;
    /** 路径 */
    @ApiModelProperty(value = "路径")
    private String url;
    /** 0：需登录；1：需认证；2：需授权（注：授权需登陆） */
    @ApiModelProperty(value = "权限类型")
    private String type;

}