package org.thirteen.authorization.model.params.base;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiParam;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author Aaron.Sun
 * @description 查询入参对象基类
 * @date Created in 23:43 2019/12/19
 * @modified by
 */
@ApiModel(description = "查询参数-基类")
@Data
@NoArgsConstructor
public abstract class BaseParam<VO> implements Serializable {

    private static final long serialVersionUID = 1L;
    @ApiParam(value = "条件参数对象")
    protected VO data;
    @ApiParam(value = "分页参数对象")
    protected PageParam page;
    @ApiParam(value = "排序参数对象")
    protected SortParam sort;

}
