package org.thirteen.datamation.core.spring;

import org.hibernate.internal.SessionImpl;
import org.hibernate.jpa.boot.internal.EntityManagerFactoryBuilderImpl;
import org.hibernate.jpa.boot.internal.PersistenceUnitInfoDescriptor;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.DefaultSingletonBeanRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.boot.web.servlet.context.AnnotationConfigServletWebApplicationContext;
import org.springframework.boot.web.servlet.context.AnnotationConfigServletWebServerApplicationContext;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractRefreshableApplicationContext;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.jdbc.datasource.lookup.SingleDataSourceLookup;
import org.springframework.lang.NonNull;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.persistenceunit.DefaultPersistenceUnitManager;
import org.springframework.orm.jpa.persistenceunit.MutablePersistenceUnitInfo;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.thirteen.datamation.DatamationAppilcation;
import org.thirteen.datamation.core.criteria.DmCriteria;
import org.thirteen.datamation.core.criteria.DmExample;
import org.thirteen.datamation.core.generate.ClassInfo;
import org.thirteen.datamation.core.generate.config.DatasourceConfigGenerator;
import org.thirteen.datamation.core.generate.po.PoConverter;
import org.thirteen.datamation.core.generate.po.PoGenerator;
import org.thirteen.datamation.core.generate.repository.BaseRepository;
import org.thirteen.datamation.core.generate.repository.RepositoryConverter;
import org.thirteen.datamation.core.generate.repository.RepositoryGenerator;
import org.thirteen.datamation.core.orm.jpa.persistenceunit.PersistenceUnitInfoImpl;
import org.thirteen.datamation.model.po.DmColumnPO;
import org.thirteen.datamation.model.po.DmTablePO;
import org.thirteen.datamation.repository.DmColumnRepository;
import org.thirteen.datamation.repository.DmTableRepository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;
import java.util.*;
import java.util.concurrent.Callable;
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
     * PersistenceUnitInfo
     *
     * org.springframework.orm.jpa.vendor.SpringHibernateJpaPersistenceProvider#createContainerEntityManagerFactory
     * (new EntityManagerFactoryBuilderImpl(new PersistenceUnitInfoDescriptor(info) {
     *             public List<String> getManagedClassNames() {
     *                 return mergedClassesAndPackages;
     *             }
     *         }, properties)).build()
     *
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

//        AnnotationConfigServletWebApplicationContext applicationContext1 = new AnnotationConfigServletWebApplicationContext();

        // 动态生成po类
        PoConverter poConverter = new PoConverter();
        PoGenerator poGenerate = new PoGenerator();
        ClassInfo tableClassInfo;
        Class<?> poClass = null;
        // 动态生成repository类
        RepositoryConverter repositoryConverter;
        RepositoryGenerator repositoryGenerate = new RepositoryGenerator();
        Class<?> repositoryClass;
        String beanName;

        // 获取beanFactory用于手动注册bd
        DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory) applicationContext.getAutowireCapableBeanFactory();
        DefaultSingletonBeanRegistry registry = (DefaultSingletonBeanRegistry) applicationContext.getAutowireCapableBeanFactory();
        // bean定义构建对象
        BeanDefinitionBuilder builder;
        // bean定义构建原型
        RootBeanDefinition tempBeanDefinition;

        // 遍历所有table
        for (DmTablePO table : tableList) {
            // 将所有column关联到对应的table
            table.getColumns().addAll(columnMap.get(table.getCode()));
            // 获取生成po类所需要的信息
            tableClassInfo = poConverter.getClassInfo(table);
            // 生成po类
            poClass = poGenerate.generate(tableClassInfo);
//            try {
//                poGenerate.writeClass(tableClassInfo);
//            } catch (IOException | URISyntaxException e) {
//                e.printStackTrace();
//            }
            try {
//                emfb.setPackagesToScan(poClass.getPackageName());
//                emfb.getNativeEntityManagerFactory().
//                emfb.setPackagesToScan(poClass.getPackageName());
//                ((MutablePersistenceUnitInfo) emfb.getPersistenceUnitInfo()).addManagedClassName(poClass.getName());
//                persistenceUnitInfo.addManagedClassName(poClass.getName());
//                emfb.getPersistenceUnitInfo().getManagedClassNames().add(poClass.getName());
//                beanFactory.destroyScopedBean("entityManagerFactory");
//                beanFactory.destroyBean(beanFactory.getBean(LocalContainerEntityManagerFactoryBean.class));
//                beanFactory.destroySingleton(beanFactory.getBeanNamesForType(LocalContainerEntityManagerFactoryBean.class)[0]);
//                registry.destroySingleton(beanFactory.getBeanNamesForType(LocalContainerEntityManagerFactoryBean.class)[0]);

//                entityManagerFactoryBean.afterPropertiesSet();
//                EntityManagerFactory emf = entityManagerFactoryBean.getObject();

//                BeanDefinition beanDefinition = beanFactory.getBeanDefinition(name);
//                beanDefinition.setBeanClassName(name);
//                beanFactory.registerBeanDefinition(name, beanDefinition);



//                ((AbstractRefreshableApplicationContext) applicationContext).refresh();

                refreshModel(poClass.getDeclaredConstructor().newInstance(), poClass.getName());
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                e.printStackTrace();
            }

            // 初始化repository转换器
            repositoryConverter = new RepositoryConverter(poClass);
            // 生成repository类
            repositoryClass = repositoryGenerate.generate(repositoryConverter.getClassInfo(table));
//            try {
//                repositoryGenerate.writeClass(repositoryConverter.getClassInfo(table));
//            } catch (IOException | URISyntaxException e) {
//                e.printStackTrace();
//            }
            // 获取已加载到spring中的repository bean定义，作为原型
            tempBeanDefinition = (RootBeanDefinition) beanFactory.getBeanDefinition("dmTableRepository");

            // bean定义构建器
//            builder = BeanDefinitionBuilder.rootBeanDefinition("org.springframework.data.jpa.repository.support.JpaRepositoryFactoryBean");
//            builder.getRawBeanDefinition().setSource(tempBeanDefinition.getSource());
//            builder.addConstructorArgValue(repositoryClass.getName());
//            builder.addPropertyValue("queryLookupStrategyKey", tempBeanDefinition.getPropertyValues().get("queryLookupStrategyKey"));
//            builder.addPropertyValue("lazyInit", tempBeanDefinition.isLazyInit());
//            builder.setLazyInit(tempBeanDefinition.isLazyInit());
//            builder.addPropertyValue("namedQueries", tempBeanDefinition.getPropertyValues().get("namedQueries"));
//            builder.addPropertyValue("repositoryFragments", tempBeanDefinition.getPropertyValues().get("repositoryFragments"));

            RootBeanDefinition rootBeanDefinition = new RootBeanDefinition();
            rootBeanDefinition.overrideFrom(tempBeanDefinition);
            rootBeanDefinition.getConstructorArgumentValues().addIndexedArgumentValue(0, repositoryClass.getName());
            ((AnnotationMetadata) rootBeanDefinition.getSource()).getAnnotations()
                .get("org.springframework.data.jpa.repository.config.EnableJpaRepositories").asMap();
            // 注入到spring容器内，beanName为表名
            beanName = BEAN_NAME_PREFIX + lowerFirst(repositoryClass.getSimpleName());
//            beanFactory.registerBeanDefinition(beanName, rootBeanDefinition.cloneBeanDefinition());
        }

//        String name = beanFactory.getBeanNamesForType(LocalContainerEntityManagerFactoryBean.class)[0];

//                LocalContainerEntityManagerFactoryBean entityManagerFactory = beanFactory.getBean(LocalContainerEntityManagerFactoryBean.class);

//                registry.destroySingleton(name);
//                registry.registerSingleton(name, entityManagerFactory);
//                beanFactory.getBean(LocalContainerEntityManagerFactoryBean.class);
//        LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
//        LocalContainerEntityManagerFactoryBean springEntityManagerFactoryBean = (LocalContainerEntityManagerFactoryBean) beanFactory.getBean(name);
//        entityManagerFactoryBean.setJpaVendorAdapter(springEntityManagerFactoryBean.getJpaVendorAdapter());
//
//        DefaultPersistenceUnitManager persistenceUnitManager = new DefaultPersistenceUnitManager();
//        persistenceUnitManager.setPackagesToScan("org.thirteen.datamation.core.generate.po");
//        persistenceUnitManager.setDataSourceLookup(new SingleDataSourceLookup(springEntityManagerFactoryBean.getDataSource()));
//        persistenceUnitManager.setDefaultDataSource(springEntityManagerFactoryBean.getDataSource());
//
//        entityManagerFactoryBean.setPersistenceUnitManager(persistenceUnitManager);
//
//        name = BEAN_NAME_PREFIX + name.replace("&", "");
//        beanFactory.registerSingleton(name, entityManagerFactoryBean);


//        DatasourceConfigGenerator datasourceConfigGenerator = new DatasourceConfigGenerator();
//        Class<?> datasourceConfigClass = datasourceConfigGenerator.generate(null);

//        try {
//            beanFactory.registerSingleton(BEAN_NAME_PREFIX + "datasourceConfig", datasourceConfigClass.getDeclaredConstructor().newInstance());
//        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
//            e.printStackTrace();
//        }
        // 设置父容器
//        applicationContext1.setParent(this.applicationContext);
//        String dataSourceBeanName = beanFactory.getBeanNamesForType(javax.sql.DataSource.class)[0];
//        applicationContext1.getBeanFactory().registerSingleton(dataSourceBeanName, beanFactory.getBean(dataSourceBeanName));
//        try {
//            applicationContext1.getClassLoader().
//            String poPath = poGenerate.getClass().getResource("/").getPath();
//            poPath = poPath + "org/thirteen/datamation/core/generate/po/RentalStorePO.class";
//            applicationContext1.getClassLoader().loadClass(poPath);
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        }
//        applicationContext1.register(datasourceConfigClass);
//        applicationContext1.refresh();
        // 创建监听器，主要用来发布项目中存在父子容器事件
//        DmTableRepository dmTableRepository1 = applicationContext1.getBean(repositoryClass);
//        BaseRepository repository = (BaseRepository) beanFactory.getBean(BEAN_NAME_PREFIX + "rentalStockRepository");
//        repository.findAll();
        System.out.println("success");

        Properties properties = new Properties();
        properties.put("hibernate.format_sql", "true");
        properties.put("hibernate.show_sql", "true");
        properties.put("hibernate.hbm2ddl.auto", "update");
        properties.put("hibernate.connection.handling_mode", "DELAYED_ACQUISITION_AND_HOLD");
        properties.put("hibernate.dialect", "org.hibernate.dialect.MySQL5Dialect");
        PersistenceUnitInfoImpl persistenceUnitInfo = new PersistenceUnitInfoImpl(
            "default",
            Collections.singletonList("org.thirteen.datamation.core.generate.po.RentalStorePO"),
//            Collections.singletonList("org.thirteen.datamation.model.po.DmTablePO"),
            properties
        );
        persistenceUnitInfo.setNonJtaDataSource(beanFactory.getBean(DataSource.class));
        Map<String, Object> configuration = new HashMap<>();
        EntityManagerFactoryBuilderImpl entityManagerFactoryBuilder = new EntityManagerFactoryBuilderImpl(
            new PersistenceUnitInfoDescriptor(persistenceUnitInfo), configuration, PoGenerator.class.getClassLoader()
        );
        EntityManagerFactory emf = entityManagerFactoryBuilder.build();
        if (poClass != null) {
//            ((SessionImpl) emf.createEntityManager()).refresh("org.thirteen.datamation.model.po.DmTablePO", DmTablePO.class);
        }

        System.out.println("build sucess");
    }

    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
        buildDatamationRepository();
    }

    void refreshModel(Object poClass, String className) {
//        emfb.getPersistenceUnitInfo().getManagedClassNames().add(className);
//        emf.createEntityManager().getTransaction().begin();
//        emf.createEntityManager().refresh(poClass);
//        emf.createEntityManager().getTransaction().commit();
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
