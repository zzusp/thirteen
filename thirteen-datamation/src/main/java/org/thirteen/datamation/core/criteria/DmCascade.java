package org.thirteen.datamation.core.criteria;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Aaron.Sun
 * @description 级联查询入参对象基类
 * @date Created in 17:41 2020/9/28
 * @modified by
 */
public class DmCascade implements Serializable {

    private static final long serialVersionUID = 1L;
    /** 表名 */
    private String tableCode;
    /** 条件参数对象 */
    private List<DmCriteria> criterias;

    public static DmCascade of(String tableCode) {
        return of(tableCode, new ArrayList<>());
    }

    public static DmCascade of(String tableCode, List<DmCriteria> criterias) {
        return new DmCascade().table(tableCode).criteria(criterias);
    }

    private DmCascade table(String tableCode) {
        this.tableCode = tableCode;
        return this;
    }

    public DmCascade criteria(List<DmCriteria> criterias) {
        this.criterias = new ArrayList<>();
        this.criterias.addAll(criterias);
        return this;
    }

    public DmCascade add(DmCriteria criteria) {
        if (this.criterias == null) {
            this.criterias = new ArrayList<>();
        }
        this.criterias.add(criteria);
        return this;
    }

    public String getTableCode() {
        return tableCode;
    }

    public void setTableCode(String tableCode) {
        this.tableCode = tableCode;
    }

    public List<DmCriteria> getCriterias() {
        return criterias;
    }

    public void setCriterias(List<DmCriteria> criterias) {
        this.criterias = criterias;
    }
}
