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
public class DmInsert implements Serializable {

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

    public DmInsert() {
        this.models = new ArrayList<>();
        this.lookups = new ArrayList<>();
    }

    public static DmInsert of(String table) {
        return new DmInsert().table(table);
    }

    private DmInsert table(String table) {
        this.table = table;
        return this;
    }

    public DmInsert model(Map<String, Object> model) {
        this.model = model;
        return this;
    }

    public DmInsert models(List<Map<String, Object>> models) {
        this.models = models;
        return this;
    }

    public DmInsert lookups(List<DmLookup> lookups) {
        this.lookups = lookups;
        return this;
    }

    public DmInsert add(DmLookup lookup) {
        if (this.lookups == null) {
            this.lookups = new ArrayList<>();
        }
        this.lookups.add(lookup);
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
}
