package org.thirteen.authorization.model.params.base;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiParam;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author Aaron.Sun
 * @description 查询参数-日期范围
 * @date Created in 16:41 2019/12/23
 * @modified by
 */
@ApiModel(description = "查询参数-日期范围")
@Data
@NoArgsConstructor
public class DateRangeParam implements Serializable {

    private static final long serialVersionUID = 1L;
    @ApiParam(value = "字段名")
    private String field;
    @ApiParam(value = "开始日期，格式yyyy-MM-dd")
    private String startDate;
    @ApiParam(value = "结束日期，格式yyyy-MM-dd")
    private String endDate;

}
