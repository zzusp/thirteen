package org.thirteen.authorization.model.vo.base;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author Aaron.Sun
 * @description 通用VO（包含删除标记信息的VO的基类）
 * @date Created in 15:23 2018/1/11
 * @modified by
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BaseDeleteVO extends BaseVO {

    private static final long serialVersionUID = 1L;
    /**
     * 0：正常；1：删除
     */
    @ApiModelProperty(value = "删除标志 0：正常；1：删除", example = "0")
    protected String delFlag;
    /**
     * 版本号，使用删除标记字段时，必须添加版本号字段，避免误操作
     */
    @ApiModelProperty(value = "版本号")
    protected Integer version;

}
