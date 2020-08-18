package org.thirteen.datamation.core.criteria;

import java.io.Serializable;

/**
 * @author Aaron.Sun
 * @description 排序查询入参对象
 * @date Created in 23:43 2019/12/19
 * @modified by
 */
public class DatamationSort implements Serializable {

    private static final long serialVersionUID = 1L;
    /** 升序 */
    public static final String ASC = "asc";
    /** 降序 */
    public static final String DESC = "desc";

    /** 字段名 */
    private String field;
    /** 排序 asc（升序）/desc（降序） */
    private String orderBy;

    public static DatamationSort of() {
        return new DatamationSort();
    }

    public static DatamationSort asc(String field) {
        return of().field(field).asc();
    }

    public static DatamationSort desc(String field) {
        return of().field(field).desc();
    }

    public DatamationSort field(String field) {
        this.field = field;
        return this;
    }

    public DatamationSort asc() {
        this.orderBy = ASC;
        return this;
    }

    public DatamationSort desc() {
        this.orderBy = DESC;
        return this;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }
}
