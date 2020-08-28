package org.thirteen.datamation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.thirteen.datamation.core.spring.DatamationRepository;
import org.thirteen.datamation.core.spring.DatamationRepositoryFactoryBean;
import org.thirteen.datamation.repository.DmColumnRepository;
import org.thirteen.datamation.repository.DmTableRepository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 * @author Aaron.Sun
 * @description 数据化模板模块启动类
 * @date Created in 22:09 2020/6/23
 * @modified By
 */
@SpringBootApplication
public class DatamationAppilcation {

    public static void main(String[] args) {
        SpringApplication.run(DatamationAppilcation.class, args);
    }

    @Bean
    public DatamationRepository datamationRepositoryFactoryBean(DmTableRepository dmTableRepository,
                                                                DmColumnRepository dmColumnRepository) throws Exception {
        return new DatamationRepositoryFactoryBean(dmTableRepository, dmColumnRepository).getObject();
    }
}
