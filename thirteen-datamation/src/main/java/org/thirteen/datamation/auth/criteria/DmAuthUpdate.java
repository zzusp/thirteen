package org.thirteen.datamation.auth.criteria;

import org.thirteen.datamation.core.criteria.DmUpdate;

/**
 * @author Aaron.Sun
 * @description 更新参数对象
 * @date Created in 10:01 2021/2/13
 * @modified by
 */
@SuppressWarnings("squid:S1948")
public class DmAuthUpdate extends DmUpdate {

    private DmAuthRule rule;

    public DmAuthRule getRule() {
        return rule;
    }

    public void setRule(DmAuthRule rule) {
        this.rule = rule;
    }

}
