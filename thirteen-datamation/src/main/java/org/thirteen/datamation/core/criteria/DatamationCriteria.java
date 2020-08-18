package org.thirteen.datamation.core.criteria;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Aaron.Sun
 * @description 条件参数对象
 * @date Created in 23:43 2019/12/19
 * @modified by
 */
public class DatamationCriteria implements Serializable {

    private static final long serialVersionUID = 1L;

    /** field = value */
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

    /** 字段 */
    protected String field;
    /** 比较操作符，默认为equals */
    protected String operator;
    /** 字段对应值 */
    protected Object value;
    /** 字段对应值的集合，一般用于比较操作符in */
    protected List<Object> values;
    /** 与上个条件的关系 AND/OR，默认为AND */
    protected String relation;
    /** 是否必选（字段对应值为空或null时，条件是否仍生效），默认为false */
    protected boolean required;
    /** 多个条件组成的条件组 */
    protected List<DatamationCriteria> criterias;

    public static DatamationCriteria of() {
        return new DatamationCriteria().operator(EQUAL).and().required(false).criterias(new ArrayList<>());
    }

    public static DatamationCriteria equal(String field, Object value) {
        return new DatamationCriteria().field(field).operator(EQUAL).value(value);
    }

    public static DatamationCriteria in(String field, List<Object> values) {
        return new DatamationCriteria().field(field).operator(IN).values(Arrays.asList(values.toArray()));
    }

    public DatamationCriteria add(DatamationCriteria criteria) {
        if (this.criterias == null) {
            this.criterias = new ArrayList<>();
        }
        this.criterias.add(criteria);
        return this;
    }

    public DatamationCriteria field(String field) {
        this.field = field;
        return this;
    }

    public DatamationCriteria operator(String operator) {
        this.operator = operator;
        return this;
    }

    public DatamationCriteria value(Object value) {
        this.value = value;
        return this;
    }

    public DatamationCriteria values(List<Object> values) {
        this.values = new ArrayList<>();
        this.values.addAll(values);
        return this;
    }

    public DatamationCriteria required(boolean required) {
        this.required = required;
        return this;
    }

    public DatamationCriteria criterias(List<DatamationCriteria> criterias) {
        this.criterias = new ArrayList<>();
        this.criterias.addAll(criterias);
        return this;
    }

    public DatamationCriteria and() {
        this.relation = AND;
        return this;
    }

    public DatamationCriteria or() {
        this.relation = OR;
        return this;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public List<Object> getValues() {
        return values;
    }

    public void setValues(List<Object> values) {
        this.values = values;
    }

    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public List<DatamationCriteria> getCriterias() {
        return criterias;
    }

    public void setCriterias(List<DatamationCriteria> criterias) {
        this.criterias = criterias;
    }
}
