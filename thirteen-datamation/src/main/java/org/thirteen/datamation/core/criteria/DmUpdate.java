package org.thirteen.datamation.core.criteria;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Aaron.Sun
 * @description 查询入参对象基类
 * @date Created in 14:16 2021/2/2
 * @modified by
 */
@SuppressWarnings("squid:S1948")
public class DmUpdate implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 查询表
     */
    private String table;
    /**
     * 新增或修改的对象
     */
    private Map<String, Object> model;
    /**
     * 新增或修改的对象
     */
    private List<Map<String, Object>> models;
    /**
     * 关联参数对象
     */
    private List<DmLookup> lookups;
    /**
     * 条件参数对象，预留字段
     */
    private List<DmCriteria> criterias;

    public DmUpdate() {
        this.models = new ArrayList<>();
        this.lookups = new ArrayList<>();
        this.criterias = new ArrayList<>();
    }

    public static DmUpdate of(String table) {
        return new DmUpdate().table(table);
    }

    private DmUpdate table(String table) {
        this.table = table;
        return this;
    }

    public DmUpdate model(Map<String, Object> model) {
        this.model = model;
        return this;
    }

    public DmUpdate models(List<Map<String, Object>> models) {
        this.models = models;
        return this;
    }

    public DmUpdate lookups(List<DmLookup> lookups) {
        this.lookups = lookups;
        return this;
    }

    public DmUpdate add(DmLookup lookup) {
        if (this.lookups == null) {
            this.lookups = new ArrayList<>();
        }
        this.lookups.add(lookup);
        return this;
    }

    public DmUpdate criterias(List<DmCriteria> criterias) {
        this.criterias = criterias;
        return this;
    }

    public DmUpdate add(DmCriteria criteria) {
        if (this.criterias == null) {
            this.criterias = new ArrayList<>();
        }
        this.criterias.add(criteria);
        return this;
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public Map<String, Object> getModel() {
        return model;
    }

    public void setModel(Map<String, Object> model) {
        this.model = model;
    }

    public List<Map<String, Object>> getModels() {
        return models;
    }

    public void setModels(List<Map<String, Object>> models) {
        this.models = models;
    }

    public List<DmLookup> getLookups() {
        return lookups;
    }

    public void setLookups(List<DmLookup> lookups) {
        this.lookups = lookups;
    }

    public List<DmCriteria> getCriterias() {
        return criterias;
    }

    public void setCriterias(List<DmCriteria> criterias) {
        this.criterias = criterias;
    }
}
