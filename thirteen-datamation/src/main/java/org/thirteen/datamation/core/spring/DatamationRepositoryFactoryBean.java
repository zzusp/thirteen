package org.thirteen.datamation.core.spring;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.lang.NonNull;
import org.thirteen.datamation.core.criteria.DmExample;
import org.thirteen.datamation.core.criteria.DmCriteria;
import org.thirteen.datamation.model.po.DmColumnPO;
import org.thirteen.datamation.model.po.DmTablePO;
import org.thirteen.datamation.repository.DmColumnRepository;
import org.thirteen.datamation.repository.DmTableRepository;

import java.util.List;
import java.util.Map;

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

    protected DatamationRepository buildDatamationRepository() {
        // 查询所有有效table数据
        DmExample<DmTablePO> tableExample = new DmExample<>();
        tableExample.createSpecification().add(DmCriteria.noDeleted());
        List<DmTablePO> tableList = dmTableRepository.findAll(tableExample.build());
        // 查询所有有效column数据
        DmExample<DmColumnPO> columnExample = new DmExample<>();
        columnExample.createSpecification().add(DmCriteria.noDeleted());
        List<DmColumnPO> columnList = dmColumnRepository.findAll(columnExample.build());
        // 将所有column关联到对应的table
        for (DmTablePO table : tableList) {
            for (DmColumnPO column : columnList) {
                if (table.getCode().equals(column.getTableCode())) {
                    table.getColumns().add(column);
                }
            }
        }


        return null;
    }
}
