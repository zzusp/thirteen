package org.thirteen.datamation.core.orm;

import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

import java.util.Objects;

public class DatamationEntityManagerFactoryBean extends LocalContainerEntityManagerFactoryBean {

    public DatamationEntityManagerFactoryBean() {
    }

    public void copy(LocalContainerEntityManagerFactoryBean localContainerEntityManagerFactoryBean) {
        this.setJpaVendorAdapter(localContainerEntityManagerFactoryBean.getJpaVendorAdapter());
        this.setPackagesToScan("org.thirteen.datamation.core.generate.po");
        this.setJpaPropertyMap(localContainerEntityManagerFactoryBean.getJpaPropertyMap());
        this.setDataSource(Objects.requireNonNull(localContainerEntityManagerFactoryBean.getDataSource()));
    }
}
