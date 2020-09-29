package org.thirteen.datamation.core.spring;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.lang.NonNull;
import org.thirteen.datamation.repository.DmColumnRepository;
import org.thirteen.datamation.repository.DmTableRepository;

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
    public DatamationRepository getObject() {
        return new DatamationRepository(dmTableRepository, dmColumnRepository, dmRelationRepository);
    }

    @Override
    public Class<?> getObjectType() {
        return DatamationRepository.class;
    }

    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

}
