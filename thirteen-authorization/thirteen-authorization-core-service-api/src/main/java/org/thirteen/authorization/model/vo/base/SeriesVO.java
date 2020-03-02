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
 * @description 图表核心数据对象
 * @date Created in 16:39 2019/4/21
 * @modified By
 */
@ApiModel(value = "图表核心数据")
@EqualsAndHashCode
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SeriesVO<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "数据内容")
    private List<T> data;

}