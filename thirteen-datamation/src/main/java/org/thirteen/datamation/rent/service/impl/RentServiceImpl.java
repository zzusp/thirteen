package org.thirteen.datamation.rent.service.impl;

import org.springframework.stereotype.Service;
import org.thirteen.datamation.auth.criteria.DmAuthInsert;
import org.thirteen.datamation.auth.criteria.DmAuthRule;
import org.thirteen.datamation.auth.criteria.DmAuthSpecification;
import org.thirteen.datamation.auth.criteria.DmAuthUpdate;
import org.thirteen.datamation.auth.service.DmAuthService;
import org.thirteen.datamation.core.criteria.DmCriteria;
import org.thirteen.datamation.rent.constant.RentCodes;
import org.thirteen.datamation.rent.service.RentService;
import org.thirteen.datamation.service.DmService;
import org.thirteen.datamation.util.CollectionUtils;
import org.thirteen.datamation.web.PagerResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Aaron.Sun
 * @description 租赁系统通用服务接口实现
 * @date Created in 21:43 2021/2/28
 * @modified by
 */
@Service
public class RentServiceImpl implements RentService {

    private final DmAuthService dmAuthService;
    private final DmService dmService;

    public RentServiceImpl(DmAuthService dmAuthService, DmService dmService) {
        this.dmAuthService = dmAuthService;
        this.dmService = dmService;
    }

    @Override
    public void insert(DmAuthInsert dmAuthInsert) {
        dmAuthInsert.setRule(this.handleRule(dmAuthInsert.getTable(), dmAuthInsert.getRule()));
        this.dmAuthService.insert(dmAuthInsert);
    }

    @Override
    public void insertAll(DmAuthInsert dmAuthInsert) {
        dmAuthInsert.setRule(this.handleRule(dmAuthInsert.getTable(), dmAuthInsert.getRule()));
        this.dmAuthService.insertAll(dmAuthInsert);
    }

    @Override
    public void update(DmAuthUpdate dmAuthUpdate) {
        dmAuthUpdate.setRule(this.handleRule(dmAuthUpdate.getTable(), dmAuthUpdate.getRule()));
        this.dmAuthService.update(dmAuthUpdate);
    }

    @Override
    public void updateAll(DmAuthUpdate dmAuthUpdate) {
        dmAuthUpdate.setRule(this.handleRule(dmAuthUpdate.getTable(), dmAuthUpdate.getRule()));
        this.dmAuthService.updateAll(dmAuthUpdate);
    }

    @Override
    public PagerResult<Map<String, Object>> findAll(String tableCode) {
        return this.findAllBySpecification(DmAuthSpecification.of(tableCode));
    }

    @Override
    public Map<String, Object> findOneBySpecification(DmAuthSpecification dmAuthSpecification) {
        String table = dmAuthSpecification.getTable();
        dmAuthSpecification.setCriterias(this.handleCriteria(table, dmAuthSpecification.getCriterias()));
        dmAuthSpecification.setRule(this.handleRule(table, dmAuthSpecification.getRule()));
        return this.dmAuthService.findOneBySpecification(dmAuthSpecification);
    }

    @Override
    public PagerResult<Map<String, Object>> findAllBySpecification(DmAuthSpecification dmAuthSpecification) {
        String table = dmAuthSpecification.getTable();
        dmAuthSpecification.setCriterias(this.handleCriteria(table, dmAuthSpecification.getCriterias()));
        dmAuthSpecification.setRule(this.handleRule(table, dmAuthSpecification.getRule()));
        return this.dmAuthService.findAllBySpecification(dmAuthSpecification);
    }

    @Override
    public boolean isExist(DmAuthSpecification dmAuthSpecification) {
        String table = dmAuthSpecification.getTable();
        dmAuthSpecification.setCriterias(this.handleCriteria(table, dmAuthSpecification.getCriterias()));
        dmAuthSpecification.setRule(this.handleRule(table, dmAuthSpecification.getRule()));
        return this.dmAuthService.isExist(dmAuthSpecification);
    }

    /**
     * 判断是否包含账号字段
     *
     * @param table 表名
     * @return 是否包含账号字段
     */
    private boolean containsAccountField(String table) {
        return this.dmService.getClassInfo(table).contains(RentCodes.RENT_ACCOUNT_FIELD);
    }

    /**
     * 因为租赁系统的主要信息表都根据账号区分，所以规则参数对象自动填充当前账号字段
     *
     * @param table      表名
     * @param dmAuthRule 规则参数对象
     * @return 规则参数对象
     */
    private DmAuthRule handleRule(String table, DmAuthRule dmAuthRule) {
        if (!this.containsAccountField(table)) {
            return dmAuthRule;
        }
        if (dmAuthRule == null) {
            dmAuthRule = DmAuthRule.of();
        }
        if (!dmAuthRule.getCurrentAccount().contains(RentCodes.RENT_ACCOUNT_FIELD)) {
            dmAuthRule.addCurrentAccount(RentCodes.RENT_ACCOUNT_FIELD);
        }
        return dmAuthRule;
    }

    /**
     * 因为租赁系统的主要信息表都根据账号区分，所以条件参数对象自动填充当前账号字段
     *
     * @param table     表名
     * @param criterias 条件参数对象
     * @return 条件参数对象
     */
    private List<DmCriteria> handleCriteria(String table, List<DmCriteria> criterias) {
        if (!this.containsAccountField(table)) {
            return criterias;
        }
        if (CollectionUtils.isEmpty(criterias)) {
            criterias = new ArrayList<>();
        } else {
            criterias.removeIf(dmCriteria -> RentCodes.RENT_ACCOUNT_FIELD.equals(dmCriteria.getField()));
        }
        criterias.add(DmCriteria.equal(RentCodes.RENT_ACCOUNT_FIELD, null));
        return criterias;
    }
}
