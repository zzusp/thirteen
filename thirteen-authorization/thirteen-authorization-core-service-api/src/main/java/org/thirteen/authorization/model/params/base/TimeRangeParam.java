package org.thirteen.authorization.model.params.base;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiParam;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author Aaron.Sun
 * @description 查询参数-时间范围
 * @date Created in 16:41 2019/12/23
 * @modified by
 */
@ApiModel(description = "查询参数-时间范围")
@Data
@NoArgsConstructor
public class TimeRangeParam implements Serializable {

    private static final long serialVersionUID = 1L;
    @ApiParam(value = "字段名")
    private String field;
    @ApiParam(value = "开始时间，格式yyyy-MM-dd hh:mm:ss.SSS")
    private String startDate;
    @ApiParam(value = "结束时间，格式yyyy-MM-dd hh:mm:ss.SSS")
    private String endDate;

}
