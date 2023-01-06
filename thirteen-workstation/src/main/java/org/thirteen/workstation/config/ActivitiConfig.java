package org.thirteen.workstation.config;

import org.activiti.engine.*;
import org.activiti.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.activiti.spring.ProcessEngineFactoryBean;
import org.activiti.spring.SpringProcessEngineConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

/**
 * @author Aaron.Sun
 * @description activiti引擎配置类
 * @date Created in 17:36 2022/11/17
 * @modified By
 */
@Configuration
public class ActivitiConfig {

    /**
     * 流程引擎配置
     *
     * @param dataSource 数据源
     * @return 流程引擎配置对象
     */
    @Bean
    public SpringProcessEngineConfiguration processEngineConfiguration(DataSource dataSource) {
        SpringProcessEngineConfiguration configuration = new SpringProcessEngineConfiguration();
        configuration.setDataSource(dataSource);
        configuration.setTransactionManager(new DataSourceTransactionManager(dataSource));
        // 设置工作流引擎启动和关闭时，如何处理数据库
        // false(默认)：检查数据库表的版本和依赖库的版本，如果版本不匹配则抛出异常
        // true：构建流程引擎时，执行检查，如果需要就执行更新。如果表不存在，就创建
        // create-drop：构建流程引擎时创建数据库，关闭流程引擎时删除这些表
        configuration.setDatabaseSchemaUpdate("true");
        // 自动部署流程 default,fail-on-no-process,never-fail,resource-parent-folder,single-resource
        configuration.setDeploymentMode("never-fail");
        configuration.setDatabaseType("never-fail");
        return configuration;
    }

    /**
     * 工作流引擎
     *
     * @param processEngineConfiguration 流程引擎配置对象
     * @return 工作流引擎对象
     */
    @Bean
    public ProcessEngineFactoryBean processEngine(ProcessEngineConfigurationImpl processEngineConfiguration) {
        ProcessEngineFactoryBean processEngineFactoryBean = new ProcessEngineFactoryBean();
        processEngineFactoryBean.setProcessEngineConfiguration(processEngineConfiguration);
        return processEngineFactoryBean;
    }

    @Bean
    public RepositoryService repositoryService(ProcessEngine processEngine) {
        return processEngine.getRepositoryService();
    }

    @Bean
    public RuntimeService runtimeService(ProcessEngine processEngine) {
        return processEngine.getRuntimeService();
    }

    @Bean
    public TaskService taskService(ProcessEngine processEngine) {
        return processEngine.getTaskService();
    }

    @Bean
    public HistoryService historyService(ProcessEngine processEngine) {
        return processEngine.getHistoryService();
    }

    @Bean
    public ManagementService managementService(ProcessEngine processEngine) {
        return processEngine.getManagementService();
    }

}
