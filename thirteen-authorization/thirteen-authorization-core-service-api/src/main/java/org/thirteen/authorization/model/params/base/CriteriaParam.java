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

    /** feild = value */
    public static final String EQUAL = "equal";
    /** filed != value */
    public static final String NOT_EQUAL = "notEqual";
    /** filed > value  value为Number类型数据 */
    public static final String GT = "gt";
    /** filed >= value  value为Number类型数据 */
    public static final String GE = "ge";
    /** filed < value  value为Number类型数据 */
    public static final String LT = "lt";
    /** filed <= value  value为Number类型数据 */
    public static final String LE = "le";
    /** filed > value  value为Comparable类型数据 */
    public static final String GREATER_THAN = "greaterThan";
    /** filed >= value  value为Comparable类型数据 */
    public static final String GREATER_THAN_OR_EQUAL_TO = "greaterThanOrEqualTo";
    /** filed < value  value为Comparable类型数据 */
    public static final String LESS_THEN = "lessThan";
    /** filed <= value  value为Comparable类型数据 */
    public static final String LESS_THAN_OR_EQUAL_TO = "lessThanOrEqualTo";
    /** filed like value */
    public static final String LIKE = "like";
    /** filed not like value */
    public static final String NOT_LIKE = "notLike";
    /** filed in (...values) */
    public static final String IN = "in";

    /** filed != value and filed like value */
    public static final String AND = "and";
    /** filed != value or filed like value */
    public static final String OR = "or";

    @ApiParam(value = "字段")
    protected String feild;
    @ApiParam(value = "比较操作符，默认为equals")
    protected String operator;
    @ApiParam(value = "字段对应值")
    protected Object value;
    @ApiParam(value = "字段对应值，一般用于比较操作符between")
    private Object otherValue;
    @ApiParam(value = "字段对应值的集合，一般用于比较操作符in")
    protected List<Object> values;
    @ApiParam(value = "与上个条件的关系 AND/OR，默认为AND")
    protected String relation;
    @ApiParam(value = "是否必选（字段对应值为空或null时，条件是否仍生效），默认为false")
    protected boolean required;
    @ApiParam(value = "多个条件组成的条件组")
    protected List<CriteriaParam> criterias;
}
