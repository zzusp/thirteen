package org.thirteen.datamation.core.criteria;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.thirteen.datamation.core.DmCodes.*;

/**
 * @author Aaron.Sun
 * @description 条件参数对象
 * @date Created in 23:43 2019/12/19
 * @modified by
 */
@SuppressWarnings("squid:S1948")
public class DmCriteria implements Serializable {

    private static final long serialVersionUID = 1L;

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
    protected List<DmCriteria> criterias;

    public static DmCriteria of() {
        return new DmCriteria().operator(EQUAL).and().required(false).criterias(new ArrayList<>());
    }

    @SuppressWarnings("squid:S1221")
    public static DmCriteria equal(String field, Object value) {
        return new DmCriteria().field(field).operator(EQUAL).value(value);
    }

    public static DmCriteria in(String field, List<Object> values) {
        return new DmCriteria().field(field).operator(IN).values(Arrays.asList(values.toArray()));
    }

    public static DmCriteria deleted() {
        return new DmCriteria().field(DEL_FLAG_KEY).operator(EQUAL).value(DEL_FLAG_DELETE);
    }

    public static DmCriteria noDeleted() {
        return new DmCriteria().field(DEL_FLAG_KEY).operator(EQUAL).value(DEL_FLAG_NORMAL);
    }

    public DmCriteria add(DmCriteria criteria) {
        if (this.criterias == null) {
            this.criterias = new ArrayList<>();
        }
        this.criterias.add(criteria);
        return this;
    }

    public DmCriteria field(String field) {
        this.field = field;
        return this;
    }

    public DmCriteria operator(String operator) {
        this.operator = operator;
        return this;
    }

    public DmCriteria value(Object value) {
        this.value = value;
        return this;
    }

    public DmCriteria values(List<Object> values) {
        this.values = new ArrayList<>();
        this.values.addAll(values);
        return this;
    }

    public DmCriteria required(boolean required) {
        this.required = required;
        return this;
    }

    public DmCriteria criterias(List<DmCriteria> criterias) {
        this.criterias = new ArrayList<>();
        this.criterias.addAll(criterias);
        return this;
    }

    public DmCriteria and() {
        this.relation = AND;
        return this;
    }

    public DmCriteria or() {
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

    public List<DmCriteria> getCriterias() {
        return criterias;
    }

    public void setCriterias(List<DmCriteria> criterias) {
        this.criterias = criterias;
    }
}
