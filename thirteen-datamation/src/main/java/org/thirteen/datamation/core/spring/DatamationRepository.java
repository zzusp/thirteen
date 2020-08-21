package org.thirteen.datamation.core.spring;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConstructorArgumentValues;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactoryBean;
import org.springframework.lang.NonNull;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.thirteen.datamation.core.criteria.DmCriteria;
import org.thirteen.datamation.core.criteria.DmExample;
import org.thirteen.datamation.core.generate.ClassInfo;
import org.thirteen.datamation.core.generate.po.PoConverter;
import org.thirteen.datamation.core.generate.po.PoGenerator;
import org.thirteen.datamation.core.generate.repository.RepositoryConverter;
import org.thirteen.datamation.core.generate.repository.RepositoryGenerator;
import org.thirteen.datamation.model.po.DmColumnPO;
import org.thirteen.datamation.model.po.DmTablePO;
import org.thirteen.datamation.repository.DmColumnRepository;
import org.thirteen.datamation.repository.DmTableRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toSet;

/**
 * @author Aaron.Sun
 * @description 数据化repository
 * @date Created in 17:30 2020/8/18
 * @modified by
 */
public class DatamationRepository implements ApplicationContextAware {

    private static final String BEAN_NAME_PREFIX = "datamation:";

    private ApplicationContext applicationContext;
    private final DmTableRepository dmTableRepository;
    private final DmColumnRepository dmColumnRepository;

    private Map<String, Object> repositoryMap;

    public DatamationRepository(DmTableRepository dmTableRepository, DmColumnRepository dmColumnRepository) {
        this.dmTableRepository = dmTableRepository;
        this.dmColumnRepository = dmColumnRepository;
        this.repositoryMap = null;
    }

    private void buildDatamationRepository() {
        // 查询所有有效table数据
        DmExample<DmTablePO> tableExample = new DmExample<>();
        tableExample.createSpecification().add(DmCriteria.noDeleted());
        List<DmTablePO> tableList = dmTableRepository.findAll(tableExample.build());
        // 查询所有有效column数据
        DmExample<DmColumnPO> columnExample = new DmExample<>();
        columnExample.createSpecification().add(DmCriteria.noDeleted());
        List<DmColumnPO> columnList = dmColumnRepository.findAll(columnExample.build());
        // 将所有column按照tableCode分组
        Map<String, Set<DmColumnPO>> columnMap = columnList.stream()
            .collect(Collectors.groupingBy(DmColumnPO::getTableCode, toSet()));

        // 动态生成po类
        PoConverter poConverter = new PoConverter();
        PoGenerator poGenerate = new PoGenerator();
        ClassInfo tableClassInfo;
        Class<?> poClass;
        // 动态生成repository类
        RepositoryConverter repositoryConverter;
        RepositoryGenerator repositoryGenerate = new RepositoryGenerator();
        Class<?> repositoryClass;
        // 获取beanFactory用于手动注册bd
        DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory) applicationContext.getAutowireCapableBeanFactory();
        BeanDefinitionBuilder beanDefinitionBuilder;

        RootBeanDefinition beanDefinition;
        RootBeanDefinition tempBeanDefinition;
        // 遍历所有table
        for (DmTablePO table : tableList) {
            // 将所有column关联到对应的table
            table.getColumns().addAll(columnMap.get(table.getCode()));
            // 获取生成po类所需要的信息
            tableClassInfo = poConverter.getClassInfo(table);
            // 生成po类
            poClass = poGenerate.generate(tableClassInfo);
            // 初始化repository转换器
            repositoryConverter = new RepositoryConverter(poClass);
            // 生成repository类
            repositoryClass = repositoryGenerate.generate(repositoryConverter.getClassInfo(table));

            tempBeanDefinition = (RootBeanDefinition) beanFactory.getBeanDefinition("dmTableRepository");
            beanDefinition = (RootBeanDefinition) BeanDefinitionBuilder.genericBeanDefinition().getBeanDefinition();
            beanDefinition.setConstructorArgumentValues(tempBeanDefinition.getConstructorArgumentValues());
            beanDefinition.setDecoratedDefinition(tempBeanDefinition.getDecoratedDefinition());
            beanDefinition.setParentName(tempBeanDefinition.getParentName());

//            beanDefinition.setBeanClassName(tempBeanDefinition.get());
            // 由class生成bd的builder对象
            ConstructorArgumentValues values = new ConstructorArgumentValues();
            ConstructorArgumentValues.ValueHolder holder = new ConstructorArgumentValues.ValueHolder(repositoryClass.getName());
            values.addIndexedArgumentValue(0, holder);
            beanDefinition.setConstructorArgumentValues(values);

            //            beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(new JpaRepositoryFactoryBean(repositoryClass));
//            beanDefinitionBuilder.
            // 注入到spring容器内，beanName为表名
            beanFactory.registerBeanDefinition(BEAN_NAME_PREFIX + repositoryClass.getSimpleName(),
                beanDefinition);
//            LocalContainerEntityManagerFactoryBean l = applicationContext
//                .getBean(LocalContainerEntityManagerFactoryBean.class);

        }
        System.out.println("success");
    }

    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
        buildDatamationRepository();
    }
}
