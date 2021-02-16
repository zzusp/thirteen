package org.thirteen.datamation.core.criteria;

import java.io.Serializable;
import java.util.List;

/**
 * @author Aaron.Sun
 * @description 删除参数对象
 * @date Created in 16:10 2021/2/13
 * @modified by
 */
@SuppressWarnings("squid:S1948")
public class DmDelete implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 查询表
     */
    private String table;
    /**
     * id
     */
    private Object id;
    /**
     * id集合
     */
    private List<Object> ids;
    /**
     * 关联参数对象
     */
    private List<DmLookup> lookups;

    public DmDelete() {
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public Object getId() {
        return id;
    }

    public void setId(Object id) {
        this.id = id;
    }

    public List<Object> getIds() {
        return ids;
    }

    public void setIds(List<Object> ids) {
        this.ids = ids;
    }

    public List<DmLookup> getLookups() {
        return lookups;
    }

    public void setLookups(List<DmLookup> lookups) {
        this.lookups = lookups;
    }
}
