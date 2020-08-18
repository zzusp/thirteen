package org.thirteen.datamation.core.spring;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.NonNull;
import org.thirteen.datamation.core.criteria.CriteriaBuilder;
import org.thirteen.datamation.core.criteria.DatamationCriteria;
import org.thirteen.datamation.model.po.DmColumnPO;
import org.thirteen.datamation.model.po.DmTablePO;
import org.thirteen.datamation.repository.DmColumnRepository;
import org.thirteen.datamation.repository.DmTableRepository;

import java.util.List;

/**
 * @author Aaron.Sun
 * @description 数据化repository工厂bean。用于生成数据化repository对象
 * @date Created in 17:30 2020/8/18
 * @modified by
 */
public class DatamationRepositoryFactoryBean implements FactoryBean<DatamationRepository>, ApplicationContextAware {

    protected ApplicationContext applicationContext;
    private final DmTableRepository dmTableRepository;
    private final DmColumnRepository dmColumnRepository;

    public DatamationRepositoryFactoryBean(DmTableRepository dmTableRepository, DmColumnRepository dmColumnRepository) {
        this.dmTableRepository = dmTableRepository;
        this.dmColumnRepository = dmColumnRepository;
    }

    @Override
    public DatamationRepository getObject() throws Exception {
        CriteriaBuilder<DmTablePO> tableBuilder = new CriteriaBuilder<>();
        List<DmTablePO> tableList = dmTableRepository.findAll(tableBuilder
            .createSpecification(DatamationCriteria.equal("delFlag", 1)));
        List<DmColumnPO> columnList = dmColumnRepository.findAll();
        return null;
    }

    @Override
    public Class<?> getObjectType() {
        return DatamationRepository.class;
    }

    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
