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
    /** 条件参数对象 */
    protected List<DmCriteria> criterias;
    /** 分页参数对象 */
    protected DmPage page;
    /** 排序参数对象集合 */
    protected List<DmSort> sorts;

    public static DmSpecification of(List<DmCriteria> criterias, DmPage page, List<DmSort> sorts) {
        return new DmSpecification().criterias(criterias).page(page).sorts(sorts);
    }

    public static DmSpecification of() {
        return new DmSpecification().criterias(new ArrayList<>()).sorts(new ArrayList<>());
    }

    public DmSpecification criterias(List<DmCriteria> criterias) {
        this.criterias = new ArrayList<>();
        this.criterias.addAll(criterias);
        return this;
    }

    public DmSpecification add(DmCriteria criteria) {
        if (this.criterias == null) {
            this.criterias = new ArrayList<>();
        }
        this.criterias.add(criteria);
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

    public List<DmCriteria> getCriterias() {
        return criterias;
    }

    public void setCriterias(List<DmCriteria> criterias) {
        this.criterias = criterias;
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
