package org.thirteen.datamation.auth.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.thirteen.datamation.auth.criteria.DmAuthInsert;
import org.thirteen.datamation.auth.criteria.DmAuthRule;
import org.thirteen.datamation.auth.criteria.DmAuthSpecification;
import org.thirteen.datamation.auth.criteria.DmAuthUpdate;
import org.thirteen.datamation.auth.exception.UnauthorizedException;
import org.thirteen.datamation.auth.service.DmAuthService;
import org.thirteen.datamation.core.criteria.DmCriteria;
import org.thirteen.datamation.service.DmService;
import org.thirteen.datamation.util.CollectionUtils;
import org.thirteen.datamation.util.JwtUtil;
import org.thirteen.datamation.web.PagerResult;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * @author Aaron.Sun
 * @description 数据化通用服务接口实现
 * @date Created in 10:06 2021/2/13
 * @modified by
 */
@Service
public class DmAuthServiceImpl implements DmAuthService {

    private final DmService dmService;

    public DmAuthServiceImpl(DmService dmService) {
        this.dmService = dmService;
    }

    @Override
    public void insert(DmAuthInsert dmAuthInsert) {
        ruleHandle(dmAuthInsert.getModel(), dmAuthInsert.getRule());
        dmService.insert(dmAuthInsert);
    }

    @Override
    public void insertAll(DmAuthInsert dmAuthInsert) {
        ruleHandle(dmAuthInsert.getModels(), dmAuthInsert.getRule());
        dmService.insertAll(dmAuthInsert);
    }

    @Override
    public void update(DmAuthUpdate dmAuthUpdate) {
        ruleHandle(dmAuthUpdate.getModel(), dmAuthUpdate.getRule());
        dmService.update(dmAuthUpdate);
    }

    @Override
    public void updateAll(DmAuthUpdate dmAuthUpdate) {
        ruleHandle(dmAuthUpdate.getModels(), dmAuthUpdate.getRule());
        dmService.updateAll(dmAuthUpdate);
    }

    @Override
    public Map<String, Object> findOneBySpecification(DmAuthSpecification dmAuthSpecification) {
        ruleCriteriaHandle(dmAuthSpecification.getCriterias(), dmAuthSpecification.getRule());
        return dmService.findOneBySpecification(dmAuthSpecification);
    }

    @Override
    public PagerResult<Map<String, Object>> findAllBySpecification(DmAuthSpecification dmAuthSpecification) {
        ruleCriteriaHandle(dmAuthSpecification.getCriterias(), dmAuthSpecification.getRule());
        return dmService.findAllBySpecification(dmAuthSpecification);
    }

    @Override
    public boolean isExist(DmAuthSpecification dmAuthSpecification) {
        ruleCriteriaHandle(dmAuthSpecification.getCriterias(), dmAuthSpecification.getRule());
        return dmService.isExist(dmAuthSpecification);
    }

    @Override
    public String getCurrentAccount() {
        // 获取当前登录用户账号
        String account = JwtUtil.getAccount();
        // 判断用户是否存在
        if (StringUtils.isEmpty(account)) {
            throw new UnauthorizedException();
        }
        return account;
    }

    /**
     * 规则处理
     *
     * @param model model
     * @param rule  规则
     */
    private void ruleHandle(Map<String, Object> model, DmAuthRule rule) {
        if (rule != null && model != null) {
            if (CollectionUtils.isNotEmpty(rule.getCurrentAccount())) {
                for (String key : rule.getCurrentAccount()) {
                    model.put(key, this.getCurrentAccount());
                }
            }
            if (CollectionUtils.isNotEmpty(rule.getCurrentDate())) {
                for (String key : rule.getCurrentDate()) {
                    model.put(key, LocalDate.now());
                }
            }
            if (CollectionUtils.isNotEmpty(rule.getCurrentDateTime())) {
                for (String key : rule.getCurrentDateTime()) {
                    model.put(key, LocalDateTime.now());
                }
            }
        }
    }

    /**
     * 规则处理
     *
     * @param models models
     * @param rule   规则
     */
    private void ruleHandle(List<Map<String, Object>> models, DmAuthRule rule) {
        if (rule != null && CollectionUtils.isNotEmpty(models)) {
            for (Map<String, Object> model : models) {
                ruleHandle(model, rule);
            }
        }
    }

    /**
     * 规则条件处理
     *
     * @param criteriaList 条件
     * @param rule         规则
     */
    private void ruleCriteriaHandle(List<DmCriteria> criteriaList, DmAuthRule rule) {
        if (CollectionUtils.isNotEmpty(criteriaList) && rule != null) {
            String field;
            for (DmCriteria criteria : criteriaList) {
                field = criteria.getField();
                if (CollectionUtils.isNotEmpty(rule.getCurrentAccount()) && rule.getCurrentAccount().contains(field)) {
                    criteria.setValue(this.getCurrentAccount());
                } else if (CollectionUtils.isNotEmpty(rule.getCurrentDate()) && rule.getCurrentDate().contains(field)) {
                    criteria.setValue(LocalDate.now());
                } else if (CollectionUtils.isNotEmpty(rule.getCurrentDateTime()) && rule.getCurrentDateTime().contains(field)) {
                    criteria.setValue(LocalDateTime.now());
                }
            }
        }
    }
}
