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
public class DatamationSpecification implements Serializable {

    private static final long serialVersionUID = 1L;
    /** 条件参数对象 */
    protected List<DatamationCriteria> criterias;
    /** 分页参数对象 */
    protected DatamationPage page;
    /** 排序参数对象集合 */
    protected List<DatamationSort> sorts;

    public static DatamationSpecification of(List<DatamationCriteria> criterias, DatamationPage page, List<DatamationSort> sorts) {
        return new DatamationSpecification().criterias(criterias).page(page).sorts(sorts);
    }

    public static DatamationSpecification of() {
        return new DatamationSpecification().criterias(new ArrayList<>()).sorts(new ArrayList<>());
    }

    public DatamationSpecification criterias(List<DatamationCriteria> criterias) {
        this.criterias = new ArrayList<>();
        this.criterias.addAll(criterias);
        return this;
    }

    public DatamationSpecification add(DatamationCriteria criteria) {
        if (this.criterias == null) {
            this.criterias = new ArrayList<>();
        }
        this.criterias.add(criteria);
        return this;
    }

    public DatamationSpecification add(DatamationSort sort) {
        if (this.sorts == null) {
            this.sorts = new ArrayList<>();
        }
        this.sorts.add(sort);
        return this;
    }

    public DatamationSpecification page(DatamationPage page) {
        this.page = page;
        return this;
    }

    public DatamationSpecification sorts(List<DatamationSort> sorts) {
        this.sorts = new ArrayList<>();
        this.sorts.addAll(sorts);
        return this;
    }

    public List<DatamationCriteria> getCriterias() {
        return criterias;
    }

    public void setCriterias(List<DatamationCriteria> criterias) {
        this.criterias = criterias;
    }

    public DatamationPage getPage() {
        return page;
    }

    public void setPage(DatamationPage page) {
        this.page = page;
    }

    public List<DatamationSort> getSorts() {
        return sorts;
    }

    public void setSorts(List<DatamationSort> sorts) {
        this.sorts = sorts;
    }
}
