package org.thirteen.datamation.core.orm.jpa.persistenceunit;

import org.hibernate.bytecode.enhance.spi.EnhancementContext;
import org.hibernate.jpa.boot.spi.PersistenceUnitDescriptor;

import javax.persistence.SharedCacheMode;
import javax.persistence.ValidationMode;
import javax.persistence.spi.PersistenceUnitTransactionType;
import javax.sql.DataSource;
import java.net.URL;
import java.util.*;

public class PersistenceUnitInfoImpl implements PersistenceUnitDescriptor {

    private final String persistenceUnitName;
    private PersistenceUnitTransactionType transactionType;
    private final List<String> managedClassNames;
    private final List<String> mappingFileNames = new LinkedList<>();
    private SharedCacheMode sharedCacheMode;
    private ValidationMode validationMode;
    private Properties properties;
    private String persistenceXMLSchemaVersion;
    private DataSource jtaDataSource;
    private DataSource nonJtaDataSource;

    public PersistenceUnitInfoImpl(String persistenceUnitName, Properties properties) {
        this.sharedCacheMode = SharedCacheMode.UNSPECIFIED;
        this.validationMode = ValidationMode.AUTO;
        this.persistenceUnitName = persistenceUnitName;
        this.managedClassNames = new ArrayList<>();
        this.properties = properties;
        this.persistenceXMLSchemaVersion = "2.1";
    }

    @Override
    public PersistenceUnitTransactionType getTransactionType() {
        if (this.transactionType != null) {
            return this.transactionType;
        }
        return this.jtaDataSource != null ? PersistenceUnitTransactionType.JTA : PersistenceUnitTransactionType.RESOURCE_LOCAL;
    }

    @Override
    public DataSource getJtaDataSource() {
        return this.jtaDataSource;
    }

    public void setJtaDataSource(DataSource jtaDataSource) {
        this.jtaDataSource = jtaDataSource;
    }

    @Override
    public DataSource getNonJtaDataSource() {
        return nonJtaDataSource;
    }

    public void setNonJtaDataSource(DataSource nonJtaDataSource) {
        this.nonJtaDataSource = nonJtaDataSource;
    }

    public void addMappingFileName(String mappingFileName) {
        this.mappingFileNames.add(mappingFileName);
    }

    @Override
    public List<String> getMappingFileNames() {
        return mappingFileNames;
    }

    @Override
    public List<URL> getJarFileUrls() {
        return Collections.emptyList();
    }

    @Override
    public URL getPersistenceUnitRootUrl() {
        return null;
    }

    @Override
    public String getName() {
        return persistenceUnitName;
    }

    @Override
    public String getProviderClassName() {
        return null;
    }

    @Override
    public boolean isUseQuotedIdentifiers() {
        return false;
    }

    @Override
    public boolean isExcludeUnlistedClasses() {
        return false;
    }

    @Override
    public List<String> getManagedClassNames() {
        return managedClassNames;
    }

    @Override
    public SharedCacheMode getSharedCacheMode() {
        return this.sharedCacheMode;
    }

    @Override
    public ValidationMode getValidationMode() {
        return this.validationMode;
    }

    @Override
    public Properties getProperties() {
        return properties;
    }

    @Override
    public ClassLoader getClassLoader() {
        return Thread.currentThread().getContextClassLoader();
    }

    @Override
    public ClassLoader getTempClassLoader() {
        return null;
    }

    @Override
    public void pushClassTransformer(EnhancementContext enhancementContext) {

    }
}
