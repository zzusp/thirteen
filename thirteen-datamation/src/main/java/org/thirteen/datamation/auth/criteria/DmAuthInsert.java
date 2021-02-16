package org.thirteen.datamation.auth.criteria;

import org.thirteen.datamation.core.criteria.DmInsert;

/**
 * @author Aaron.Sun
 * @description 新增参数对象
 * @date Created in 14:16 2021/2/2
 * @modified by
 */
@SuppressWarnings("squid:S1948")
public class DmAuthInsert extends DmInsert {

    private DmAuthRule rule;

    public DmAuthRule getRule() {
        return rule;
    }

    public void setRule(DmAuthRule rule) {
        this.rule = rule;
    }
}
