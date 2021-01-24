package org.thirteen.datamation.core.criteria;

import java.io.Serializable;

import static org.thirteen.datamation.core.DmCodes.ASC;
import static org.thirteen.datamation.core.DmCodes.DESC;

/**
 * @author Aaron.Sun
 * @description 排序查询入参对象
 * @date Created in 23:43 2019/12/19
 * @modified by
 */
public class DmSort implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 字段名
     */
    private String field;
    /**
     * 排序 asc（升序）/desc（降序）
     */
    private String orderBy;

    public static DmSort of() {
        return new DmSort();
    }

    public static DmSort asc(String field) {
        return of().field(field).asc();
    }

    public static DmSort desc(String field) {
        return of().field(field).desc();
    }

    public DmSort field(String field) {
        this.field = field;
        return this;
    }

    public DmSort asc() {
        this.orderBy = ASC;
        return this;
    }

    public DmSort desc() {
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
