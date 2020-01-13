package org.thirteen.authorization.model.params.base;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiParam;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @author Aaron.Sun
 * @description 条件参数对象
 * @date Created in 23:43 2019/12/19
 * @modified by
 */
@ApiModel(description = "查询参数-条件参数")
@Data
@NoArgsConstructor
public class CriteriaParam implements Serializable {

    private static final long serialVersionUID = 1L;
    @ApiParam(value = "字段")
    protected String feild;
    @ApiParam(value = "比较操作符")
    protected String operator;
    @ApiParam(value = "字段对应值")
    protected String value;
    @ApiParam(value = "字段对应值，一般用于条件 BETWEEN")
    private String otherValue;
    @ApiParam(value = "字段对应值的集合，一般用于条件 IN")
    protected List<String> values;
    @ApiParam(value = "与上个条件的关系 AND/OR，默认为AND")
    protected String relation;
    @ApiParam(value = "是否必选（字段对应值为空或null时，条件是否仍生效），默认为false")
    protected boolean required;
    @ApiParam(value = "多个条件组成的条件组")
    protected List<CriteriaParam> group;
}
