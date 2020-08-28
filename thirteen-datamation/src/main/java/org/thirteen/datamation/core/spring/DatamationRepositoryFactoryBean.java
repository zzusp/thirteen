package org.thirteen.datamation.core.spring;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.thirteen.datamation.repository.DmColumnRepository;
import org.thirteen.datamation.repository.DmTableRepository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

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
        return new DatamationRepository(dmTableRepository, dmColumnRepository);
    }

    @Override
    public Class<?> getObjectType() {
        return DatamationRepository.class;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

}
