package org.thirteen.authorization.model.params.base;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiParam;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author Aaron.Sun
 * @description 分页查询入参对象
 * @date Created in 23:43 2019/12/19
 * @modified by
 */
@ApiModel(description = "查询参数-分页参数")
@Data
@NoArgsConstructor
public class PageParam implements Serializable {

    private static final long serialVersionUID = 1L;
    @ApiParam(required = true, value = "当前页码")
    private Integer pageNum;
    @ApiParam(required = true, value = "每页大小")
    private Integer pageSize;

}
