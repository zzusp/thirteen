package org.thirteen.authorization.model.vo.base;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @author Aaron.Sun
 * @description 通用图表返回结果VO
 * @date Created in 16:39 2019/4/21
 * @modified By
 */
@ApiModel(value = "通用图表返回结果")
@EqualsAndHashCode
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChartVO<T> implements Serializable {

    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "图表名称")
    private String name;
    @ApiModelProperty(value = "X轴数据")
    private List<String> axisx;
    @ApiModelProperty(value = "核心数据集合")
    private List<SeriesVO<T>> series;

}
