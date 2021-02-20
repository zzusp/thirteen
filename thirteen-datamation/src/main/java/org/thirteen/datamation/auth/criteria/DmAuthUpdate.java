package org.thirteen.datamation.auth.criteria;

import org.thirteen.datamation.core.criteria.DmCriteria;
import org.thirteen.datamation.core.criteria.DmLookup;
import org.thirteen.datamation.core.criteria.DmUpdate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Aaron.Sun
 * @description 更新参数对象
 * @date Created in 10:01 2021/2/13
 * @modified by
 */
@SuppressWarnings("squid:S1948")
public class DmAuthUpdate extends DmUpdate {

    private DmAuthRule rule;

    public static DmAuthUpdate of(String table) {
        DmAuthUpdate dmAuthUpdate = new DmAuthUpdate();
        dmAuthUpdate.setTable(table);
        return dmAuthUpdate;
    }

    public DmAuthUpdate rule(DmAuthRule rule) {
        this.rule = rule;
        return this;
    }

    @Override
    public DmAuthUpdate model(Map<String, Object> model) {
        this.setModel(model);
        return this;
    }

    @Override
    public DmUpdate models(List<Map<String, Object>> models) {
        this.setModels(models);
        return this;
    }

    @Override
    public DmUpdate lookups(List<DmLookup> lookups) {
        this.setLookups(lookups);
        return this;
    }

    @Override
    public DmUpdate add(DmLookup lookup) {
        if (this.getLookups() == null) {
            this.setLookups(new ArrayList<>());
        }
        this.getLookups().add(lookup);
        return this;
    }

    @Override
    public DmUpdate criterias(List<DmCriteria> criterias) {
        this.setCriterias(criterias);
        return this;
    }

    @Override
    public DmUpdate add(DmCriteria criteria) {
        if (this.getCriterias() == null) {
            this.setCriterias(new ArrayList<>());
        }
        this.getCriterias().add(criteria);
        return this;
    }

    public DmAuthRule getRule() {
        return rule;
    }

    public void setRule(DmAuthRule rule) {
        this.rule = rule;
    }

}
