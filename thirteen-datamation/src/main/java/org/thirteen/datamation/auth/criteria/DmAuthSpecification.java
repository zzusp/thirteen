package org.thirteen.datamation.auth.criteria;

import org.thirteen.datamation.core.criteria.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Aaron.Sun
 * @description 查询参数对象
 * @date Created in 10:01 2021/2/13
 * @modified by
 */
@SuppressWarnings("squid:S1948")
public class DmAuthSpecification extends DmSpecification {

    private DmAuthRule rule;

    public DmAuthSpecification() {
        super();
    }

    public static DmAuthSpecification of() {
        return of(null, new ArrayList<>(), null, new ArrayList<>());
    }

    public static DmAuthSpecification of(String table) {
        return of(table, new ArrayList<>(), null, new ArrayList<>());
    }

    public static DmAuthSpecification of(String table, List<DmCriteria> criterias) {
        return of(table, criterias, null, new ArrayList<>());
    }

    public static DmAuthSpecification of(String table, List<DmCriteria> criterias, DmPage page, List<DmSort> sorts) {
        return new DmAuthSpecification().table(table).criteria(criterias).page(page).sorts(sorts);
    }

    @Override
    public DmAuthSpecification table(String table) {
        this.table = table;
        return this;
    }

    @Override
    public DmAuthSpecification criteria(List<DmCriteria> criterias) {
        this.criterias = new ArrayList<>();
        this.criterias.addAll(criterias);
        return this;
    }

    @Override
    public DmAuthSpecification lookup(List<DmLookup> lookups) {
        this.lookups = new ArrayList<>();
        this.lookups.addAll(lookups);
        return this;
    }

    @Override
    public DmAuthSpecification add(DmCriteria criteria) {
        if (this.criterias == null) {
            this.criterias = new ArrayList<>();
        }
        this.criterias.add(criteria);
        return this;
    }

    @Override
    public DmAuthSpecification add(DmLookup lookup) {
        if (this.lookups == null) {
            this.lookups = new ArrayList<>();
        }
        this.lookups.add(lookup);
        return this;
    }

    @Override
    public DmAuthSpecification add(DmSort sort) {
        if (this.sorts == null) {
            this.sorts = new ArrayList<>();
        }
        this.sorts.add(sort);
        return this;
    }

    @Override
    public DmAuthSpecification page(DmPage page) {
        this.page = page;
        return this;
    }

    @Override
    public DmAuthSpecification sorts(List<DmSort> sorts) {
        this.sorts = new ArrayList<>();
        this.sorts.addAll(sorts);
        return this;
    }

    public DmAuthRule getRule() {
        return rule;
    }

    public void setRule(DmAuthRule rule) {
        this.rule = rule;
    }

}
