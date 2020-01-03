package org.thirteen.authorization.model.vo.base;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BaseDeleteVO extends BaseVO {

    private static final long serialVersionUID = 1L;
    /** 0：正常；1：删除 */
    @ApiModelProperty(value = "删除标志 0：正常；1：删除")
    private String delFlag;

}
