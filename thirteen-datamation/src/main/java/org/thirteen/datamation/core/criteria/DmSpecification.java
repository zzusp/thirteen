package org.thirteen.datamation.core.criteria;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Aaron.Sun
 * @description 查询入参对象基类
 * @date Created in 23:43 2019/12/19
 * @modified by
 */
@SuppressWarnings("squid:S1948")
public class DmSpecification implements Serializable {

    private static final long serialVersionUID = 1L;
    /** 查询表 */
    private String table;
    /** 条件参数对象 */
    private List<DmCriteria> criterias;
    /** 关联查询参数对象 */
    private List<DmLookup> lookups;
    /** 分页参数对象 */
    private DmPage page;
    /** 排序参数对象集合 */
    private List<DmSort> sorts;

    public static DmSpecification of() {
        return of(null, new ArrayList<>(), null, new ArrayList<>());
    }

    public static DmSpecification of(String table) {
        return of(table, new ArrayList<>(), null, new ArrayList<>());
    }

    public static DmSpecification of(String table, List<DmCriteria> criterias) {
        return of(table, criterias, null, new ArrayList<>());
    }

    public static DmSpecification of(String table, List<DmCriteria> criterias, DmPage page, List<DmSort> sorts) {
        return new DmSpecification().table(table).criteria(criterias).page(page).sorts(sorts);
    }

    public DmSpecification table(String table) {
        this.table = table;
        return this;
    }

    public DmSpecification criteria(List<DmCriteria> criterias) {
        this.criterias = new ArrayList<>();
        this.criterias.addAll(criterias);
        return this;
    }

    public DmSpecification lookup(List<DmLookup> lookups) {
        this.lookups = new ArrayList<>();
        this.lookups.addAll(lookups);
        return this;
    }

    public DmSpecification add(DmCriteria criteria) {
        if (this.criterias == null) {
            this.criterias = new ArrayList<>();
        }
        this.criterias.add(criteria);
        return this;
    }

    public DmSpecification add(DmLookup lookup) {
        if (this.lookups == null) {
            this.lookups = new ArrayList<>();
        }
        this.lookups.add(lookup);
        return this;
    }

    public DmSpecification add(DmSort sort) {
        if (this.sorts == null) {
            this.sorts = new ArrayList<>();
        }
        this.sorts.add(sort);
        return this;
    }

    public DmSpecification page(DmPage page) {
        this.page = page;
        return this;
    }

    public DmSpecification sorts(List<DmSort> sorts) {
        this.sorts = new ArrayList<>();
        this.sorts.addAll(sorts);
        return this;
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public List<DmCriteria> getCriterias() {
        return criterias;
    }

    public void setCriterias(List<DmCriteria> criterias) {
        this.criterias = criterias;
    }

    public List<DmLookup> getLookups() {
        return lookups;
    }

    public void setLookups(List<DmLookup> lookups) {
        this.lookups = lookups;
    }

    public DmPage getPage() {
        return page;
    }

    public void setPage(DmPage page) {
        this.page = page;
    }

    public List<DmSort> getSorts() {
        return sorts;
    }

    public void setSorts(List<DmSort> sorts) {
        this.sorts = sorts;
    }
}
