package org.thirteen.datamation.core.spring;

import org.hibernate.internal.SessionImpl;
import org.hibernate.jpa.boot.internal.PersistenceUnitInfoDescriptor;
import org.springframework.beans.BeansException;
import org.springframework.beans.PropertyValue;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.DefaultSingletonBeanRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.lang.NonNull;
import org.thirteen.datamation.DatamationAppilcation;
import org.thirteen.datamation.core.criteria.DmCriteria;
import org.thirteen.datamation.core.criteria.DmExample;
import org.thirteen.datamation.core.generate.ClassInfo;
import org.thirteen.datamation.core.generate.po.PoConverter;
import org.thirteen.datamation.core.generate.po.PoGenerator;
import org.thirteen.datamation.core.generate.repository.BaseRepository;
import org.thirteen.datamation.core.generate.repository.RepositoryConverter;
import org.thirteen.datamation.core.generate.repository.RepositoryGenerator;
import org.thirteen.datamation.core.orm.DmEntityManagerFactoryBuilderImpl;
import org.thirteen.datamation.core.orm.jpa.persistenceunit.PersistenceUnitInfoImpl;
import org.thirteen.datamation.model.po.DmColumnPO;
import org.thirteen.datamation.model.po.DmTablePO;
import org.thirteen.datamation.repository.DmColumnRepository;
import org.thirteen.datamation.repository.DmTableRepository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
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

    /**
     * 构建datamationRepository
     */
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
        ClassLoader poClassLoader = poGenerate.getNeighborClassLoader();
        ClassInfo tableClassInfo;
        Class<?> poClass;
        // 动态生成repository类
        RepositoryConverter repositoryConverter;
        RepositoryGenerator repositoryGenerate = new RepositoryGenerator();
        Class<?> repositoryClass;

        // 获取beanFactory用于手动注册bd
        DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory) applicationContext.getAutowireCapableBeanFactory();
        // 实体类完整类路径集合
        LinkedHashSet<String> entityList = new LinkedHashSet<>();
        // 记录repository bean定义的map。key为beanName，value为bean定义
        Map<String, RootBeanDefinition> repositoryBeanDefinitionMap = new HashMap<>(tableList.size() << 1);
        String repositoryName;
        RootBeanDefinition repositoryBd;
        // bean定义构建原型
        String tempBeanName = beanFactory.getBeanNamesForType(DmTableRepository.class)[0];
        RootBeanDefinition tempBeanDefinition = (RootBeanDefinition) beanFactory.getBeanDefinition(tempBeanName);
        // 遍历所有table
        for (DmTablePO table : tableList) {
            // 将所有column关联到对应的table
            table.getColumns().addAll(columnMap.get(table.getCode()));
            // 获取生成po类所需要的信息
            tableClassInfo = poConverter.getClassInfo(table);
            // 生成po类
            poClass = poGenerate.generate(tableClassInfo);
            // 将po完整类名添加到实体类集合，供hibernate加载
            entityList.add(poClass.getName());

            // 初始化repository转换器
            repositoryConverter = new RepositoryConverter(poClass);
            // 生成repository类
            repositoryClass = repositoryGenerate.generate(repositoryConverter.getClassInfo(table));
            // 获取已加载到spring中的repository bean定义，作为原型
            repositoryBd = new RootBeanDefinition();
            repositoryBd.overrideFrom(tempBeanDefinition);
            repositoryBd.getConstructorArgumentValues().addIndexedArgumentValue(0, repositoryClass.getName());
            // 注入到spring容器内，beanName为表名
            repositoryName = BEAN_NAME_PREFIX + lowerFirst(repositoryClass.getSimpleName());
            // 将bean定义放入map中
            repositoryBeanDefinitionMap.put(repositoryName, repositoryBd);
        }

        // 判断是否有生成实体对象
        if (!entityList.isEmpty()) {
            // 创建EntityManagerFactory
            EntityManagerFactory emf = createEntityManagerFactory(beanFactory, poClassLoader, entityList);
            // 以下代码为repository注入容器处理
            registerRepository(beanFactory, emf, repositoryBeanDefinitionMap);

            BaseRepository repository = (BaseRepository) beanFactory.getBean(BEAN_NAME_PREFIX + "rentalStockRepository");
            repository.findAll();
        }
        System.out.println("success");
    }

    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
        buildDatamationRepository();
    }

    /**
     * 创建EntityManagerFactory
     *
     * @param beanFactory bean工厂对象
     * @param classLoader 类加载器
     * @param entityList 实体类完整类路径集合
     * @return EntityManagerFactory
     */
    private EntityManagerFactory createEntityManagerFactory(DefaultListableBeanFactory beanFactory,
                                                            ClassLoader classLoader, LinkedHashSet<String> entityList) {
        Properties properties = new Properties();
        properties.put("hibernate.format_sql", "true");
        properties.put("hibernate.show_sql", "true");
        properties.put("hibernate.hbm2ddl.auto", "update");
        properties.put("hibernate.connection.handling_mode", "DELAYED_ACQUISITION_AND_HOLD");
        properties.put("hibernate.dialect", "org.hibernate.dialect.MySQL5Dialect");
        PersistenceUnitInfoImpl persistenceUnitInfo = new PersistenceUnitInfoImpl(
            "default",
            properties
        );
        persistenceUnitInfo.setNonJtaDataSource(beanFactory.getBean(DataSource.class));
        // 使用自定义EntityManagerFactoryBuilderImpl，可实现直接加载asm生成的class类
        DmEntityManagerFactoryBuilderImpl entityManagerFactoryBuilder = new DmEntityManagerFactoryBuilderImpl(
            new PersistenceUnitInfoDescriptor(persistenceUnitInfo), null,
            classLoader, entityList
        );
        return entityManagerFactoryBuilder.build();
    }

    /**
     * 注册repository到spring容器
     *
     * @param beanFactory bean工厂对象
     * @param emf 实体管理工厂
     * @param repositoryBeanDefinitionMap 记录repository bean定义的map。key为beanName，value为bean定义
     */
    private void registerRepository(DefaultListableBeanFactory beanFactory, EntityManagerFactory emf,
                                    Map<String, RootBeanDefinition> repositoryBeanDefinitionMap) {
        RootBeanDefinition bd;
        RootBeanDefinition entityManagerBd;
        PropertyValue pv;
        // 遍历所有要注册到spring容器的repository定义
        for (Map.Entry<String, RootBeanDefinition> rbd : repositoryBeanDefinitionMap.entrySet()) {
            bd = rbd.getValue();
            // 替换EntityManagerFactory
            for (int i = 0; i < bd.getPropertyValues().getPropertyValueList().size(); i++) {
                pv = bd.getPropertyValues().getPropertyValueList().get(i);
                // 替换entityManager属性中，org.springframework.orm.jpa.SharedEntityManagerCreator定义中构造方法参数中的
                // EntityManagerFactory。之所以不直接替换entityManager，而是替换entityManagerFactory，
                // 是因为要将entityManager的创建和关闭交给spring容器管理
                if ("entityManager".equals(pv.getName())) {
                    entityManagerBd = (RootBeanDefinition) pv.getValue();
                    if (entityManagerBd != null) {
                        entityManagerBd.getConstructorArgumentValues().addIndexedArgumentValue(0, emf);
                        bd.getPropertyValues().getPropertyValueList().set(i, new PropertyValue(pv, entityManagerBd));
                    }
                }
            }
            // 将repository注入容器中
            beanFactory.registerBeanDefinition(rbd.getKey(), bd.cloneBeanDefinition());
        }
    }

    /**
     * 首字母小写
     *
     * @param str 字符串
     * @return 首字母小写的字符串
     */
    private String lowerFirst(String str) {
        char[] charArr = str.toCharArray();
        // 如果为大写字母，则将ascii编码前移32位
        char a = 'A';
        char z = 'Z';
        if (charArr[0] >= a && charArr[0] <= z) {
            charArr[0] += 32;
        }
        return String.valueOf(charArr);
    }

}
