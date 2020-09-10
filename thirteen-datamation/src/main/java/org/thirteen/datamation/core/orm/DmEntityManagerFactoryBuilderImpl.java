package org.thirteen.datamation.core.orm;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;
import javax.persistence.AttributeConverter;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceException;
import javax.persistence.spi.PersistenceUnitTransactionType;
import javax.sql.DataSource;
import org.hibernate.SessionFactory;
import org.hibernate.SessionFactoryObserver;
import org.hibernate.boot.CacheRegionDefinition;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.SessionFactoryBuilder;
import org.hibernate.boot.archive.scan.internal.StandardScanOptions;
import org.hibernate.boot.cfgxml.spi.CfgXmlAccessService;
import org.hibernate.boot.cfgxml.spi.LoadedConfig;
import org.hibernate.boot.cfgxml.spi.MappingReference;
import org.hibernate.boot.model.TypeContributor;
import org.hibernate.boot.model.process.spi.ManagedResources;
import org.hibernate.boot.model.process.spi.MetadataBuildingProcess;
import org.hibernate.boot.registry.BootstrapServiceRegistry;
import org.hibernate.boot.registry.BootstrapServiceRegistryBuilder;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.boot.registry.classloading.internal.TcclLookupPrecedence;
import org.hibernate.boot.registry.classloading.spi.ClassLoaderService;
import org.hibernate.boot.registry.selector.StrategyRegistrationProvider;
import org.hibernate.boot.registry.selector.spi.StrategySelector;
import org.hibernate.boot.spi.MetadataBuilderContributor;
import org.hibernate.boot.spi.MetadataBuilderImplementor;
import org.hibernate.boot.spi.MetadataImplementor;
import org.hibernate.boot.spi.SessionFactoryBuilderImplementor;
import org.hibernate.bytecode.enhance.spi.DefaultEnhancementContext;
import org.hibernate.bytecode.enhance.spi.EnhancementContext;
import org.hibernate.bytecode.enhance.spi.UnloadedClass;
import org.hibernate.bytecode.enhance.spi.UnloadedField;
import org.hibernate.cfg.AttributeConverterDefinition;
import org.hibernate.cfg.Environment;
import org.hibernate.cfg.beanvalidation.BeanValidationIntegrator;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.id.factory.spi.MutableIdentifierGeneratorFactory;
import org.hibernate.integrator.spi.Integrator;
import org.hibernate.internal.EntityManagerMessageLogger;
import org.hibernate.internal.HEMLogging;
import org.hibernate.internal.util.NullnessHelper;
import org.hibernate.internal.util.StringHelper;
import org.hibernate.internal.util.config.ConfigurationHelper;
import org.hibernate.jpa.boot.internal.StandardJpaScanEnvironmentImpl;
import org.hibernate.jpa.boot.spi.EntityManagerFactoryBuilder;
import org.hibernate.jpa.boot.spi.IntegratorProvider;
import org.hibernate.jpa.boot.spi.PersistenceUnitDescriptor;
import org.hibernate.jpa.boot.spi.StrategyRegistrationProviderList;
import org.hibernate.jpa.boot.spi.TypeContributorList;
import org.hibernate.jpa.internal.util.LogHelper;
import org.hibernate.jpa.internal.util.PersistenceUnitTransactionTypeHelper;
import org.hibernate.jpa.spi.IdentifierGeneratorStrategyProvider;
import org.hibernate.proxy.EntityNotFoundDelegate;
import org.hibernate.resource.transaction.backend.jdbc.internal.JdbcResourceLocalTransactionCoordinatorBuilderImpl;
import org.hibernate.resource.transaction.backend.jta.internal.JtaTransactionCoordinatorBuilderImpl;
import org.hibernate.resource.transaction.spi.TransactionCoordinatorBuilder;
import org.hibernate.secure.spi.GrantedPermission;
import org.hibernate.secure.spi.JaccPermissionDeclarations;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.spi.ServiceRegistryImplementor;
import org.hibernate.tool.schema.spi.DelayedDropRegistryNotAvailableImpl;
import org.hibernate.tool.schema.spi.SchemaManagementToolCoordinator;

public class DmEntityManagerFactoryBuilderImpl implements EntityManagerFactoryBuilder {
    private static final EntityManagerMessageLogger LOG = HEMLogging.messageLogger(DmEntityManagerFactoryBuilderImpl.class);
    public static final String INTEGRATOR_PROVIDER = "hibernate.integrator_provider";
    public static final String STRATEGY_REGISTRATION_PROVIDERS = "hibernate.strategy_registration_provider";
    public static final String TYPE_CONTRIBUTORS = "hibernate.type_contributors";
    public static final String METADATA_BUILDER_CONTRIBUTOR = "hibernate.metadata_builder_contributor";
    public static final String JANDEX_INDEX = "hibernate.jandex_index";
    private final PersistenceUnitDescriptor persistenceUnit;
    private final Map configurationValues;
    private final StandardServiceRegistry standardServiceRegistry;
    private final ManagedResources managedResources;
    private final MetadataBuilderImplementor metamodelBuilder;
    private static final String IS_JTA_TXN_COORD = "local.setting.IS_JTA_TXN_COORD";
    private Object validatorFactory;
    private Object cdiBeanManager;
    private DataSource dataSource;
    private MetadataImplementor metadata;

    public DmEntityManagerFactoryBuilderImpl(PersistenceUnitDescriptor persistenceUnit, Map integrationSettings) {
        this(persistenceUnit, integrationSettings, (ClassLoader)null, (ClassLoaderService)null, null);
    }

    public DmEntityManagerFactoryBuilderImpl(PersistenceUnitDescriptor persistenceUnit, Map integrationSettings, ClassLoader providedClassLoader, LinkedHashSet<String> mapperList) {
        this(persistenceUnit, integrationSettings, providedClassLoader, (ClassLoaderService)null, mapperList);
    }

    public DmEntityManagerFactoryBuilderImpl(PersistenceUnitDescriptor persistenceUnit, Map integrationSettings, ClassLoaderService providedClassLoaderService) {
        this(persistenceUnit, integrationSettings, (ClassLoader)null, providedClassLoaderService, null);
    }

    private DmEntityManagerFactoryBuilderImpl(PersistenceUnitDescriptor persistenceUnit, Map integrationSettings, ClassLoader providedClassLoader, ClassLoaderService providedClassLoaderService, LinkedHashSet<String> mapperList) {
        LogHelper.logPersistenceUnitInformation(persistenceUnit);
        this.persistenceUnit = persistenceUnit;
        if (integrationSettings == null) {
            integrationSettings = Collections.emptyMap();
        }

        BootstrapServiceRegistry bsr = this.buildBootstrapServiceRegistry(integrationSettings, providedClassLoader, providedClassLoaderService);
        StandardServiceRegistryBuilder ssrBuilder = StandardServiceRegistryBuilder.forJpa(bsr);
        DmEntityManagerFactoryBuilderImpl.MergedSettings mergedSettings = this.mergeSettings(persistenceUnit, integrationSettings, ssrBuilder);
        if ("true".equals(mergedSettings.configurationValues.get("hibernate.transaction.flush_before_completion"))) {
            LOG.definingFlushBeforeCompletionIgnoredInHem("hibernate.transaction.flush_before_completion");
            mergedSettings.configurationValues.put("hibernate.transaction.flush_before_completion", "false");
        }

        this.configurationValues = mergedSettings.getConfigurationValues();
        ssrBuilder.applySettings(this.configurationValues);
        this.standardServiceRegistry = ssrBuilder.build();
        this.configureIdentifierGenerators(this.standardServiceRegistry);
        MetadataSources metadataSources = new MetadataSources(bsr);
        List<AttributeConverterDefinition> attributeConverterDefinitions = this.applyMappingResources(metadataSources);
        this.metamodelBuilder = (MetadataBuilderImplementor)metadataSources.getMetadataBuilder(this.standardServiceRegistry);
        this.applyMetamodelBuilderSettings(mergedSettings, attributeConverterDefinitions);
        this.applyMetadataBuilderContributor();
        CfgXmlAccessService cfgXmlAccessService = (CfgXmlAccessService)this.standardServiceRegistry.getService(CfgXmlAccessService.class);
        if (cfgXmlAccessService.getAggregatedConfig() != null && cfgXmlAccessService.getAggregatedConfig().getMappingReferences() != null) {
            Iterator var11 = cfgXmlAccessService.getAggregatedConfig().getMappingReferences().iterator();

            while(var11.hasNext()) {
                MappingReference mappingReference = (MappingReference)var11.next();
                mappingReference.apply(metadataSources);
            }
        }

        if (mapperList != null) {
            for (String mapper : mapperList) {
                metadataSources.addAnnotatedClassName(mapper);
            }
        }

        this.managedResources = MetadataBuildingProcess.prepare(metadataSources, this.metamodelBuilder.getBootstrapContext());
        this.withValidatorFactory(this.configurationValues.get("javax.persistence.validation.factory"));
        boolean dirtyTrackingEnabled = this.readBooleanConfigurationValue("hibernate.enhancer.enableDirtyTracking");
        boolean lazyInitializationEnabled = this.readBooleanConfigurationValue("hibernate.enhancer.enableLazyInitialization");
        boolean associationManagementEnabled = this.readBooleanConfigurationValue("hibernate.enhancer.enableAssociationManagement");
        if (dirtyTrackingEnabled || lazyInitializationEnabled || associationManagementEnabled) {
            EnhancementContext enhancementContext = this.getEnhancementContext(dirtyTrackingEnabled, lazyInitializationEnabled, associationManagementEnabled);
            persistenceUnit.pushClassTransformer(enhancementContext);
        }

        this.metamodelBuilder.applyTempClassLoader((ClassLoader)null);
    }

    private void applyMetadataBuilderContributor() {
        Object metadataBuilderContributorSetting = this.configurationValues.get("hibernate.metadata_builder_contributor");
        if (metadataBuilderContributorSetting != null) {
            MetadataBuilderContributor metadataBuilderContributor = (MetadataBuilderContributor)this.loadSettingInstance("hibernate.metadata_builder_contributor", metadataBuilderContributorSetting, MetadataBuilderContributor.class);
            if (metadataBuilderContributor != null) {
                metadataBuilderContributor.contribute(this.metamodelBuilder);
            }

        }
    }

    public Map getConfigurationValues() {
        return Collections.unmodifiableMap(this.configurationValues);
    }

    private boolean readBooleanConfigurationValue(String propertyName) {
        Object propertyValue = this.configurationValues.remove(propertyName);
        return propertyValue != null && Boolean.parseBoolean(propertyValue.toString());
    }

    protected EnhancementContext getEnhancementContext(final boolean dirtyTrackingEnabled, final boolean lazyInitializationEnabled, final boolean associationManagementEnabled) {
        return new DefaultEnhancementContext() {
            @Override
            public boolean isEntityClass(UnloadedClass classDescriptor) {
                return DmEntityManagerFactoryBuilderImpl.this.managedResources.getAnnotatedClassNames().contains(classDescriptor.getName()) && super.isEntityClass(classDescriptor);
            }

            @Override
            public boolean isCompositeClass(UnloadedClass classDescriptor) {
                return DmEntityManagerFactoryBuilderImpl.this.managedResources.getAnnotatedClassNames().contains(classDescriptor.getName()) && super.isCompositeClass(classDescriptor);
            }

            @Override
            public boolean doBiDirectionalAssociationManagement(UnloadedField field) {
                return associationManagementEnabled;
            }

            @Override
            public boolean doDirtyCheckingInline(UnloadedClass classDescriptor) {
                return dirtyTrackingEnabled;
            }

            @Override
            public boolean hasLazyLoadableAttributes(UnloadedClass classDescriptor) {
                return lazyInitializationEnabled;
            }

            @Override
            public boolean isLazyLoadable(UnloadedField field) {
                return lazyInitializationEnabled;
            }

            @Override
            public boolean doExtendedEnhancement(UnloadedClass classDescriptor) {
                return false;
            }
        };
    }

    private BootstrapServiceRegistry buildBootstrapServiceRegistry(Map integrationSettings, ClassLoader providedClassLoader, ClassLoaderService providedClassLoaderService) {
        BootstrapServiceRegistryBuilder bsrBuilder = new BootstrapServiceRegistryBuilder();
        this.applyIntegrationProvider(integrationSettings, bsrBuilder);
        StrategyRegistrationProviderList strategyRegistrationProviderList = (StrategyRegistrationProviderList)integrationSettings.get("hibernate.strategy_registration_provider");
        if (strategyRegistrationProviderList != null) {
            Iterator var6 = strategyRegistrationProviderList.getStrategyRegistrationProviders().iterator();

            while(var6.hasNext()) {
                StrategyRegistrationProvider strategyRegistrationProvider = (StrategyRegistrationProvider)var6.next();
                bsrBuilder.applyStrategySelectors(strategyRegistrationProvider);
            }
        }

        if (providedClassLoaderService != null) {
            bsrBuilder.applyClassLoaderService(providedClassLoaderService);
        } else {
            if (this.persistenceUnit.getClassLoader() != null) {
                bsrBuilder.applyClassLoader(this.persistenceUnit.getClassLoader());
            }

            if (providedClassLoader != null) {
                bsrBuilder.applyClassLoader(providedClassLoader);
            }

            ClassLoader appClassLoader = (ClassLoader)integrationSettings.get("hibernate.classLoader.application");
            if (appClassLoader != null) {
                LOG.debugf("Found use of deprecated `%s` setting; use `%s` instead.", "hibernate.classLoader.application", "hibernate.classLoaders");
            }

            Object classLoadersSetting = integrationSettings.get("hibernate.classLoaders");
            if (classLoadersSetting != null) {
                if (Collection.class.isInstance(classLoadersSetting)) {
                    Iterator var14 = ((Collection)classLoadersSetting).iterator();

                    while(var14.hasNext()) {
                        ClassLoader classLoader = (ClassLoader)var14.next();
                        bsrBuilder.applyClassLoader(classLoader);
                    }
                } else if (classLoadersSetting.getClass().isArray()) {
                    ClassLoader[] var8 = (ClassLoader[])((ClassLoader[])classLoadersSetting);
                    int var9 = var8.length;

                    for(int var10 = 0; var10 < var9; ++var10) {
                        ClassLoader classLoader = var8[var10];
                        bsrBuilder.applyClassLoader(classLoader);
                    }
                } else if (ClassLoader.class.isInstance(classLoadersSetting)) {
                    bsrBuilder.applyClassLoader((ClassLoader)classLoadersSetting);
                }
            }

            Properties puProperties = this.persistenceUnit.getProperties();
            if (puProperties != null) {
                String tcclLookupPrecedence = puProperties.getProperty("hibernate.classLoader.tccl_lookup_precedence");
                if (tcclLookupPrecedence != null) {
                    bsrBuilder.applyTcclLookupPrecedence(TcclLookupPrecedence.valueOf(tcclLookupPrecedence.toUpperCase(Locale.ROOT)));
                }
            }
        }

        return bsrBuilder.build();
    }

    private void applyIntegrationProvider(Map integrationSettings, BootstrapServiceRegistryBuilder bsrBuilder) {
        Object integrationSetting = integrationSettings.get("hibernate.integrator_provider");
        if (integrationSetting != null) {
            IntegratorProvider integratorProvider = (IntegratorProvider)this.loadSettingInstance("hibernate.integrator_provider", integrationSetting, IntegratorProvider.class);
            if (integratorProvider != null) {
                Iterator var5 = integratorProvider.getIntegrators().iterator();

                while(var5.hasNext()) {
                    Integrator integrator = (Integrator)var5.next();
                    bsrBuilder.applyIntegrator(integrator);
                }
            }

        }
    }

    private DmEntityManagerFactoryBuilderImpl.MergedSettings mergeSettings(PersistenceUnitDescriptor persistenceUnit, Map<?, ?> integrationSettings, StandardServiceRegistryBuilder ssrBuilder) {
        DmEntityManagerFactoryBuilderImpl.MergedSettings mergedSettings = new DmEntityManagerFactoryBuilderImpl.MergedSettings();
        mergedSettings.processPersistenceUnitDescriptorProperties(persistenceUnit);
        String cfgXmlResourceName = (String)mergedSettings.configurationValues.remove("hibernate.ejb.cfgfile");
        if (StringHelper.isEmpty(cfgXmlResourceName)) {
            cfgXmlResourceName = (String)integrationSettings.get("hibernate.ejb.cfgfile");
        }

        if (StringHelper.isNotEmpty(cfgXmlResourceName)) {
            this.processHibernateConfigXmlResources(ssrBuilder, mergedSettings, cfgXmlResourceName);
        }

        this.normalizeSettings(persistenceUnit, integrationSettings, mergedSettings);
        String jaccContextId = (String)mergedSettings.configurationValues.get("hibernate.jacc_context_id");
        Iterator itr = mergedSettings.configurationValues.entrySet().iterator();

        while(itr.hasNext()) {
            Map.Entry entry = (Map.Entry)itr.next();
            if (entry.getValue() == null) {
                itr.remove();
                break;
            }

            if (String.class.isInstance(entry.getKey()) && String.class.isInstance(entry.getValue())) {
                String keyString = (String)entry.getKey();
                String valueString = (String)entry.getValue();
                if (keyString.startsWith("hibernate.jacc")) {
                    if (!"hibernate.jacc_context_id".equals(keyString) && !"hibernate.jacc.enabled".equals(keyString)) {
                        if (jaccContextId == null) {
                            LOG.debug("Found JACC permission grant [%s] in properties, but no JACC context id was specified; ignoring");
                        } else {
                            mergedSettings.getJaccPermissions(jaccContextId).addPermissionDeclaration(this.parseJaccConfigEntry(keyString, valueString));
                        }
                    }
                } else if (keyString.startsWith("hibernate.ejb.classcache")) {
                    mergedSettings.addCacheRegionDefinition(this.parseCacheRegionDefinitionEntry(keyString.substring("hibernate.ejb.classcache".length() + 1), valueString, CacheRegionDefinition.CacheRegionType.ENTITY));
                } else if (keyString.startsWith("hibernate.ejb.collectioncache")) {
                    mergedSettings.addCacheRegionDefinition(this.parseCacheRegionDefinitionEntry(keyString.substring("hibernate.ejb.collectioncache".length() + 1), (String)entry.getValue(), CacheRegionDefinition.CacheRegionType.COLLECTION));
                }
            }
        }

        return mergedSettings;
    }

    private void normalizeSettings(PersistenceUnitDescriptor persistenceUnit, Map<?, ?> integrationSettings, DmEntityManagerFactoryBuilderImpl.MergedSettings mergedSettings) {
        HashMap<?, ?> integrationSettingsCopy = new HashMap(integrationSettings);
        this.normalizeConnectionAccessUserAndPass(integrationSettingsCopy, mergedSettings);
        this.normalizeTransactionCoordinator(persistenceUnit, integrationSettingsCopy, mergedSettings);
        this.normalizeDataAccess(integrationSettingsCopy, mergedSettings, persistenceUnit);
        Object intgValidationMode = integrationSettingsCopy.remove("javax.persistence.validation.mode");
        if (intgValidationMode != null) {
            mergedSettings.configurationValues.put("javax.persistence.validation.mode", intgValidationMode);
        } else if (persistenceUnit.getValidationMode() != null) {
            mergedSettings.configurationValues.put("javax.persistence.validation.mode", persistenceUnit.getValidationMode());
        }

        Object intgCacheMode = integrationSettingsCopy.remove("javax.persistence.sharedCache.mode");
        if (intgCacheMode != null) {
            mergedSettings.configurationValues.put("javax.persistence.sharedCache.mode", intgCacheMode);
        } else if (persistenceUnit.getSharedCacheMode() != null) {
            mergedSettings.configurationValues.put("javax.persistence.sharedCache.mode", persistenceUnit.getSharedCacheMode());
        }

        Iterator var7 = integrationSettingsCopy.entrySet().iterator();

        while(var7.hasNext()) {
            Map.Entry<?, ?> entry = (Map.Entry)var7.next();
            if (entry.getKey() != null) {
                if (entry.getValue() == null) {
                    mergedSettings.configurationValues.remove(entry.getKey());
                } else {
                    mergedSettings.configurationValues.put(entry.getKey(), entry.getValue());
                }
            }
        }

    }

    private void normalizeConnectionAccessUserAndPass(HashMap<?, ?> integrationSettingsCopy, DmEntityManagerFactoryBuilderImpl.MergedSettings mergedSettings) {
        Object effectiveUser = NullnessHelper.coalesceSuppliedValues(new Supplier[]{() -> {
            return integrationSettingsCopy.remove("hibernate.connection.username");
        }, () -> {
            return integrationSettingsCopy.remove("javax.persistence.jdbc.user");
        }, () -> {
            return this.extractPuProperty(this.persistenceUnit, "hibernate.connection.username");
        }, () -> {
            return this.extractPuProperty(this.persistenceUnit, "javax.persistence.jdbc.user");
        }});
        Object effectivePass = NullnessHelper.coalesceSuppliedValues(new Supplier[]{() -> {
            return integrationSettingsCopy.remove("hibernate.connection.password");
        }, () -> {
            return integrationSettingsCopy.remove("javax.persistence.jdbc.password");
        }, () -> {
            return this.extractPuProperty(this.persistenceUnit, "hibernate.connection.password");
        }, () -> {
            return this.extractPuProperty(this.persistenceUnit, "javax.persistence.jdbc.password");
        }});
        if (effectiveUser != null || effectivePass != null) {
            this.applyUserAndPass(effectiveUser, effectivePass, mergedSettings);
        }

    }

    private <T> T extractPuProperty(PersistenceUnitDescriptor persistenceUnit, String propertyName) {
        return persistenceUnit.getProperties() == null ? null : (T) persistenceUnit.getProperties().get(propertyName);
    }

    private void applyUserAndPass(Object effectiveUser, Object effectivePass, DmEntityManagerFactoryBuilderImpl.MergedSettings mergedSettings) {
        if (effectiveUser != null) {
            mergedSettings.configurationValues.put("hibernate.connection.username", effectiveUser);
            mergedSettings.configurationValues.put("javax.persistence.jdbc.user", effectiveUser);
        }

        if (effectivePass != null) {
            mergedSettings.configurationValues.put("hibernate.connection.password", effectivePass);
            mergedSettings.configurationValues.put("javax.persistence.jdbc.password", effectivePass);
        }

    }

    private void normalizeTransactionCoordinator(PersistenceUnitDescriptor persistenceUnit, HashMap<?, ?> integrationSettingsCopy, DmEntityManagerFactoryBuilderImpl.MergedSettings mergedSettings) {
        PersistenceUnitTransactionType txnType = null;
        Object intgTxnType = integrationSettingsCopy.remove("javax.persistence.transactionType");
        if (intgTxnType != null) {
            txnType = PersistenceUnitTransactionTypeHelper.interpretTransactionType(intgTxnType);
        } else if (persistenceUnit.getTransactionType() != null) {
            txnType = persistenceUnit.getTransactionType();
        } else {
            Object puPropTxnType = mergedSettings.configurationValues.get("javax.persistence.transactionType");
            if (puPropTxnType != null) {
                txnType = PersistenceUnitTransactionTypeHelper.interpretTransactionType(puPropTxnType);
            }
        }

        if (txnType == null) {
            LOG.debugf("PersistenceUnitTransactionType not specified - falling back to RESOURCE_LOCAL", new Object[0]);
            txnType = PersistenceUnitTransactionType.RESOURCE_LOCAL;
        }

        boolean hasTxStrategy = mergedSettings.configurationValues.containsKey("hibernate.transaction.coordinator_class");
        Boolean definiteJtaCoordinator;
        if (hasTxStrategy) {
            LOG.overridingTransactionStrategyDangerous("hibernate.transaction.coordinator_class");
            Object strategy = mergedSettings.configurationValues.get("hibernate.transaction.coordinator_class");
            if (strategy instanceof TransactionCoordinatorBuilder) {
                definiteJtaCoordinator = ((TransactionCoordinatorBuilder)strategy).isJta();
            } else {
                definiteJtaCoordinator = false;
            }
        } else if (txnType == PersistenceUnitTransactionType.JTA) {
            mergedSettings.configurationValues.put("hibernate.transaction.coordinator_class", JtaTransactionCoordinatorBuilderImpl.class);
            definiteJtaCoordinator = true;
        } else {
            if (txnType != PersistenceUnitTransactionType.RESOURCE_LOCAL) {
                throw new IllegalStateException("Could not determine TransactionCoordinator strategy to use");
            }

            mergedSettings.configurationValues.put("hibernate.transaction.coordinator_class", JdbcResourceLocalTransactionCoordinatorBuilderImpl.class);
            definiteJtaCoordinator = false;
        }

        mergedSettings.configurationValues.put("local.setting.IS_JTA_TXN_COORD", definiteJtaCoordinator);
    }

    private void normalizeDataAccess(HashMap<?, ?> integrationSettingsCopy, DmEntityManagerFactoryBuilderImpl.MergedSettings mergedSettings, PersistenceUnitDescriptor persistenceUnit) {
        if (this.dataSource != null) {
            this.applyDataSource(this.dataSource, (Boolean)null, integrationSettingsCopy, mergedSettings);
        } else {
            Object url;
            if (integrationSettingsCopy.containsKey("hibernate.connection.datasource")) {
                url = integrationSettingsCopy.remove("hibernate.connection.datasource");
                if (url != null) {
                    this.applyDataSource(url, (Boolean)null, integrationSettingsCopy, mergedSettings);
                    return;
                }
            }

            if (integrationSettingsCopy.containsKey("javax.persistence.jtaDataSource")) {
                url = integrationSettingsCopy.remove("javax.persistence.jtaDataSource");
                if (url != null) {
                    this.applyDataSource(url, true, integrationSettingsCopy, mergedSettings);
                    return;
                }
            }

            if (integrationSettingsCopy.containsKey("javax.persistence.nonJtaDataSource")) {
                url = integrationSettingsCopy.remove("javax.persistence.nonJtaDataSource");
                this.applyDataSource(url, false, integrationSettingsCopy, mergedSettings);
            } else {
                if (integrationSettingsCopy.containsKey("hibernate.connection.url")) {
                    url = integrationSettingsCopy.get("hibernate.connection.url");
                    if (url != null) {
                        this.applyJdbcSettings(url, (String)NullnessHelper.coalesceSuppliedValues(new Supplier[]{() -> {
                            return ConfigurationHelper.getString("hibernate.connection.driver_class", integrationSettingsCopy);
                        }, () -> {
                            return ConfigurationHelper.getString("javax.persistence.jdbc.driver", integrationSettingsCopy);
                        }, () -> {
                            return ConfigurationHelper.getString("hibernate.connection.driver_class", mergedSettings.configurationValues);
                        }, () -> {
                            return ConfigurationHelper.getString("javax.persistence.jdbc.driver", mergedSettings.configurationValues);
                        }}), integrationSettingsCopy, mergedSettings);
                        return;
                    }
                }

                if (integrationSettingsCopy.containsKey("javax.persistence.jdbc.url")) {
                    url = integrationSettingsCopy.get("javax.persistence.jdbc.url");
                    if (url != null) {
                        this.applyJdbcSettings(url, (String)NullnessHelper.coalesceSuppliedValues(new Supplier[]{() -> {
                            return ConfigurationHelper.getString("javax.persistence.jdbc.driver", integrationSettingsCopy);
                        }, () -> {
                            return ConfigurationHelper.getString("javax.persistence.jdbc.driver", mergedSettings.configurationValues);
                        }}), integrationSettingsCopy, mergedSettings);
                        return;
                    }
                }

                if (persistenceUnit.getJtaDataSource() != null) {
                    this.applyDataSource(persistenceUnit.getJtaDataSource(), true, integrationSettingsCopy, mergedSettings);
                } else if (persistenceUnit.getNonJtaDataSource() != null) {
                    this.applyDataSource(persistenceUnit.getNonJtaDataSource(), false, integrationSettingsCopy, mergedSettings);
                } else {
                    if (mergedSettings.configurationValues.containsKey("hibernate.connection.url")) {
                        url = mergedSettings.configurationValues.get("hibernate.connection.url");
                        if (url != null && (!(url instanceof String) || StringHelper.isNotEmpty((String)url))) {
                            this.applyJdbcSettings(url, ConfigurationHelper.getString("hibernate.connection.driver_class", mergedSettings.configurationValues), integrationSettingsCopy, mergedSettings);
                            return;
                        }
                    }

                    if (mergedSettings.configurationValues.containsKey("javax.persistence.jdbc.url")) {
                        url = mergedSettings.configurationValues.get("javax.persistence.jdbc.url");
                        if (url != null && (!(url instanceof String) || StringHelper.isNotEmpty((String)url))) {
                            this.applyJdbcSettings(url, ConfigurationHelper.getString("javax.persistence.jdbc.driver", mergedSettings.configurationValues), integrationSettingsCopy, mergedSettings);
                            return;
                        }
                    }

                }
            }
        }
    }

    private void applyDataSource(Object dataSourceRef, Boolean useJtaDataSource, HashMap<?, ?> integrationSettingsCopy, DmEntityManagerFactoryBuilderImpl.MergedSettings mergedSettings) {
        boolean isJtaTransactionCoordinator = (Boolean)mergedSettings.configurationValues.remove("local.setting.IS_JTA_TXN_COORD");
        boolean isJta = useJtaDataSource == null ? isJtaTransactionCoordinator : useJtaDataSource;
        String emfKey;
        String inverseEmfKey;
        if (isJta) {
            emfKey = "javax.persistence.jtaDataSource";
            inverseEmfKey = "javax.persistence.nonJtaDataSource";
        } else {
            emfKey = "javax.persistence.nonJtaDataSource";
            inverseEmfKey = "javax.persistence.jtaDataSource";
        }

        mergedSettings.configurationValues.put(emfKey, dataSourceRef);
        this.cleanUpConfigKeys(integrationSettingsCopy, mergedSettings, inverseEmfKey, "javax.persistence.jdbc.driver", "hibernate.connection.driver_class", "javax.persistence.jdbc.url", "hibernate.connection.url");
        this.cleanUpConfigKeys(integrationSettingsCopy, "hibernate.connection.datasource", "javax.persistence.jtaDataSource", "javax.persistence.nonJtaDataSource");
        mergedSettings.configurationValues.put("hibernate.connection.datasource", dataSourceRef);
    }

    private void cleanUpConfigKeys(HashMap<?, ?> integrationSettingsCopy, DmEntityManagerFactoryBuilderImpl.MergedSettings mergedSettings, String... keys) {
        String[] var4 = keys;
        int var5 = keys.length;

        for(int var6 = 0; var6 < var5; ++var6) {
            String key = var4[var6];
            Object removedIntgSetting = integrationSettingsCopy.remove(key);
            if (removedIntgSetting != null) {
                LOG.debugf("Removed integration override setting [%s] due to normalization", key);
            }

            Object removedMergedSetting = mergedSettings.configurationValues.remove(key);
            if (removedMergedSetting != null) {
                LOG.debugf("Removed merged setting [%s] due to normalization", key);
            }
        }

    }

    private void cleanUpConfigKeys(Map<?, ?> settings, String... keys) {
        String[] var3 = keys;
        int var4 = keys.length;

        for(int var5 = 0; var5 < var4; ++var5) {
            String key = var3[var5];
            settings.remove(key);
        }

    }

    private void applyJdbcSettings(Object url, String driver, HashMap<?, ?> integrationSettingsCopy, DmEntityManagerFactoryBuilderImpl.MergedSettings mergedSettings) {
        mergedSettings.configurationValues.put("hibernate.connection.url", url);
        mergedSettings.configurationValues.put("javax.persistence.jdbc.url", url);
        if (driver != null) {
            mergedSettings.configurationValues.put("hibernate.connection.driver_class", driver);
            mergedSettings.configurationValues.put("javax.persistence.jdbc.driver", driver);
        } else {
            mergedSettings.configurationValues.remove("hibernate.connection.driver_class");
            mergedSettings.configurationValues.remove("javax.persistence.jdbc.driver");
        }

        this.cleanUpConfigKeys(integrationSettingsCopy, "hibernate.connection.driver_class", "javax.persistence.jdbc.driver", "hibernate.connection.url", "javax.persistence.jdbc.url", "hibernate.connection.username", "javax.persistence.jdbc.user", "hibernate.connection.password", "javax.persistence.jdbc.password");
        this.cleanUpConfigKeys(integrationSettingsCopy, mergedSettings, "hibernate.connection.datasource", "javax.persistence.jtaDataSource", "javax.persistence.nonJtaDataSource");
    }

    private void processHibernateConfigXmlResources(StandardServiceRegistryBuilder ssrBuilder, DmEntityManagerFactoryBuilderImpl.MergedSettings mergedSettings, String cfgXmlResourceName) {
        LoadedConfig loadedConfig = ssrBuilder.getConfigLoader().loadConfigXmlResource(cfgXmlResourceName);
        mergedSettings.processHibernateConfigXmlResources(loadedConfig);
        ssrBuilder.getAggregatedCfgXml().merge(loadedConfig);
    }

    private GrantedPermission parseJaccConfigEntry(String keyString, String valueString) {
        try {
            int roleStart = "hibernate.jacc".length() + 1;
            String role = keyString.substring(roleStart, keyString.indexOf(46, roleStart));
            int classStart = roleStart + role.length() + 1;
            String clazz = keyString.substring(classStart, keyString.length());
            return new GrantedPermission(role, clazz, valueString);
        } catch (IndexOutOfBoundsException var7) {
            throw this.persistenceException("Illegal usage of hibernate.jacc: " + keyString);
        }
    }

    private CacheRegionDefinition parseCacheRegionDefinitionEntry(String role, String value, CacheRegionDefinition.CacheRegionType cacheType) {
        StringTokenizer params = new StringTokenizer(value, ";, ");
        if (!params.hasMoreTokens()) {
            StringBuilder error = new StringBuilder("Illegal usage of ");
            if (cacheType == CacheRegionDefinition.CacheRegionType.ENTITY) {
                error.append("hibernate.ejb.classcache").append(": ").append("hibernate.ejb.classcache");
            } else {
                error.append("hibernate.ejb.collectioncache").append(": ").append("hibernate.ejb.collectioncache");
            }

            error.append('.').append(role).append(' ').append(value).append(".  Was expecting configuration (usage[,region[,lazy]]), but found none");
            throw this.persistenceException(error.toString());
        } else {
            String usage = params.nextToken();
            String region = null;
            if (params.hasMoreTokens()) {
                region = params.nextToken();
            }

            boolean lazyProperty = true;
            if (cacheType == CacheRegionDefinition.CacheRegionType.ENTITY) {
                if (params.hasMoreTokens()) {
                    lazyProperty = "all".equalsIgnoreCase(params.nextToken());
                }
            } else {
                lazyProperty = false;
            }

            return new CacheRegionDefinition(cacheType, role, usage, region, lazyProperty);
        }
    }

    private void configureIdentifierGenerators(StandardServiceRegistry ssr) {
        StrategySelector strategySelector = (StrategySelector)ssr.getService(StrategySelector.class);
        Object idGeneratorStrategyProviderSetting = this.configurationValues.remove("hibernate.ejb.identifier_generator_strategy_provider");
        if (idGeneratorStrategyProviderSetting != null) {
            IdentifierGeneratorStrategyProvider idGeneratorStrategyProvider = (IdentifierGeneratorStrategyProvider)strategySelector.resolveStrategy(IdentifierGeneratorStrategyProvider.class, idGeneratorStrategyProviderSetting);
            MutableIdentifierGeneratorFactory identifierGeneratorFactory = (MutableIdentifierGeneratorFactory)ssr.getService(MutableIdentifierGeneratorFactory.class);
            if (identifierGeneratorFactory == null) {
                throw this.persistenceException("Application requested custom identifier generator strategies, but the MutableIdentifierGeneratorFactory could not be found");
            }

            Iterator var6 = idGeneratorStrategyProvider.getStrategies().entrySet().iterator();

            while(var6.hasNext()) {
                Map.Entry<String, Class<?>> entry = (Map.Entry)var6.next();
                identifierGeneratorFactory.register((String)entry.getKey(), (Class)entry.getValue());
            }
        }

    }

    private List<AttributeConverterDefinition> applyMappingResources(MetadataSources metadataSources) {
        List<AttributeConverterDefinition> attributeConverterDefinitions = null;
        List<Class> loadedAnnotatedClasses = (List)this.configurationValues.remove("hibernate.ejb.loaded.classes");
        if (loadedAnnotatedClasses != null) {
            Iterator var4 = loadedAnnotatedClasses.iterator();

            while(var4.hasNext()) {
                Class cls = (Class)var4.next();
                if (AttributeConverter.class.isAssignableFrom(cls)) {
                    if (attributeConverterDefinitions == null) {
                        attributeConverterDefinitions = new ArrayList();
                    }

                    attributeConverterDefinitions.add(AttributeConverterDefinition.from(cls));
                } else {
                    metadataSources.addAnnotatedClass(cls);
                }
            }
        }

        String explicitHbmXmls = (String)this.configurationValues.remove("hibernate.hbmxml.files");
        if (explicitHbmXmls != null) {
            String[] var10 = StringHelper.split(", ", explicitHbmXmls);
            int var6 = var10.length;

            for(int var7 = 0; var7 < var6; ++var7) {
                String hbmXml = var10[var7];
                metadataSources.addResource(hbmXml);
            }
        }

        List<String> explicitOrmXmlList = (List)this.configurationValues.remove("hibernate.ejb.xml_files");
        if (explicitOrmXmlList != null) {
            explicitOrmXmlList.forEach(metadataSources::addResource);
        }

        return attributeConverterDefinitions;
    }

    private void applyMetamodelBuilderSettings(DmEntityManagerFactoryBuilderImpl.MergedSettings mergedSettings, List<AttributeConverterDefinition> attributeConverterDefinitions) {
        this.metamodelBuilder.getBootstrapContext().markAsJpaBootstrap();
        if (this.persistenceUnit.getTempClassLoader() != null) {
            this.metamodelBuilder.applyTempClassLoader(this.persistenceUnit.getTempClassLoader());
        }

        this.metamodelBuilder.applyScanEnvironment(new StandardJpaScanEnvironmentImpl(this.persistenceUnit));
        this.metamodelBuilder.applyScanOptions(new StandardScanOptions((String)this.configurationValues.get("hibernate.archive.autodetection"), this.persistenceUnit.isExcludeUnlistedClasses()));
        List var10000;
        MetadataBuilderImplementor var10001;
        if (mergedSettings.cacheRegionDefinitions != null) {
            var10000 = mergedSettings.cacheRegionDefinitions;
            var10001 = this.metamodelBuilder;
            ((List<CacheRegionDefinition>) var10000).forEach(var10001::applyCacheRegionDefinition);
        }

        TypeContributorList typeContributorList = (TypeContributorList)this.configurationValues.remove("hibernate.type_contributors");
        if (typeContributorList != null) {
            var10000 = typeContributorList.getTypeContributors();
            var10001 = this.metamodelBuilder;
            ((List<TypeContributor>) var10000).forEach(var10001::applyTypes);
        }

        if (attributeConverterDefinitions != null) {
            var10001 = this.metamodelBuilder;
            attributeConverterDefinitions.forEach(var10001::applyAttributeConverter);
        }

    }

    public MetadataImplementor getMetadata() {
        return this.metadata;
    }

    @Override
    public EntityManagerFactoryBuilder withValidatorFactory(Object validatorFactory) {
        this.validatorFactory = validatorFactory;
        if (validatorFactory != null) {
            BeanValidationIntegrator.validateFactory(validatorFactory);
        }

        return this;
    }

    @Override
    public EntityManagerFactoryBuilder withDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
        return this;
    }

    @Override
    public void cancel() {
    }

    private MetadataImplementor metadata() {
        if (this.metadata == null) {
            this.metadata = MetadataBuildingProcess.complete(this.managedResources, this.metamodelBuilder.getBootstrapContext(), this.metamodelBuilder.getMetadataBuildingOptions());
        }

        return this.metadata;
    }

    @Override
    public void generateSchema() {
        try {
            SessionFactoryBuilder sfBuilder = this.metadata().getSessionFactoryBuilder();
            this.populateSfBuilder(sfBuilder, this.standardServiceRegistry);
            SchemaManagementToolCoordinator.process(this.metadata, this.standardServiceRegistry, this.configurationValues, DelayedDropRegistryNotAvailableImpl.INSTANCE);
        } catch (Exception var2) {
            throw this.persistenceException("Error performing schema management", var2);
        }

        this.cancel();
    }

    @Override
    public EntityManagerFactory build() {
        SessionFactoryBuilder sfBuilder = this.metadata().getSessionFactoryBuilder();
        this.populateSfBuilder(sfBuilder, this.standardServiceRegistry);

        try {
            return sfBuilder.build();
        } catch (Exception var3) {
            throw this.persistenceException("Unable to build Hibernate SessionFactory", var3);
        }
    }

    protected void populateSfBuilder(SessionFactoryBuilder sfBuilder, StandardServiceRegistry ssr) {
        StrategySelector strategySelector = (StrategySelector)ssr.getService(StrategySelector.class);
        boolean jtaTransactionAccessEnabled = this.readBooleanConfigurationValue("hibernate.jta.allowTransactionAccess");
        if (!jtaTransactionAccessEnabled) {
            ((SessionFactoryBuilderImplementor)sfBuilder).disableJtaTransactionAccess();
        }

        boolean allowRefreshDetachedEntity = this.readBooleanConfigurationValue("hibernate.allow_refresh_detached_entity");
        if (!allowRefreshDetachedEntity) {
            ((SessionFactoryBuilderImplementor)sfBuilder).disableRefreshDetachedEntity();
        }

        Object sessionFactoryObserverSetting = this.configurationValues.remove("hibernate.ejb.session_factory_observer");
        if (sessionFactoryObserverSetting != null) {
            SessionFactoryObserver suppliedSessionFactoryObserver = (SessionFactoryObserver)strategySelector.resolveStrategy(SessionFactoryObserver.class, sessionFactoryObserverSetting);
            sfBuilder.addSessionFactoryObservers(new SessionFactoryObserver[]{suppliedSessionFactoryObserver});
        }

        sfBuilder.addSessionFactoryObservers(new SessionFactoryObserver[]{DmEntityManagerFactoryBuilderImpl.ServiceRegistryCloser.INSTANCE});
        sfBuilder.applyEntityNotFoundDelegate(DmEntityManagerFactoryBuilderImpl.JpaEntityNotFoundDelegate.INSTANCE);
        if (this.validatorFactory != null) {
            sfBuilder.applyValidatorFactory(this.validatorFactory);
        }

        if (this.cdiBeanManager != null) {
            sfBuilder.applyBeanManager(this.cdiBeanManager);
        }

    }

    private PersistenceException persistenceException(String message) {
        return this.persistenceException(message, (Exception)null);
    }

    private PersistenceException persistenceException(String message, Exception cause) {
        return new PersistenceException(this.getExceptionHeader() + message, cause);
    }

    private String getExceptionHeader() {
        return "[PersistenceUnit: " + this.persistenceUnit.getName() + "] ";
    }

    private <T> T loadSettingInstance(String settingName, Object settingValue, Class<T> clazz) {
        T instance = null;
        Class<? extends T> instanceClass = null;
        if (clazz.isAssignableFrom(settingValue.getClass())) {
            instance = (T) settingValue;
        } else if (settingValue instanceof Class) {
            instanceClass = (Class)settingValue;
        } else {
            if (!(settingValue instanceof String)) {
                throw new IllegalArgumentException("The provided " + settingName + " setting value [" + settingValue + "] is not supported!");
            }

            String settingStringValue = (String)settingValue;
            if (this.standardServiceRegistry != null) {
                ClassLoaderService classLoaderService = (ClassLoaderService)this.standardServiceRegistry.getService(ClassLoaderService.class);
                instanceClass = classLoaderService.classForName(settingStringValue);
            } else {
                try {
                    instanceClass = (Class<? extends T>) Class.forName(settingStringValue);
                } catch (ClassNotFoundException var9) {
                    throw new IllegalArgumentException("Can't load class: " + settingStringValue, var9);
                }
            }
        }

        if (instanceClass != null) {
            try {
                instance = instanceClass.newInstance();
            } catch (IllegalAccessException | InstantiationException var8) {
                throw new IllegalArgumentException("The " + clazz.getSimpleName() + " class [" + instanceClass + "] could not be instantiated!", var8);
            }
        }

        return instance;
    }

    private static class MergedSettings {
        private final Map configurationValues;
        private Map<String, JaccPermissionDeclarations> jaccPermissionsByContextId;
        private List<CacheRegionDefinition> cacheRegionDefinitions;

        private MergedSettings() {
            this.configurationValues = new ConcurrentHashMap(16, 0.75F, 1);
            this.configurationValues.putAll(Environment.getProperties());
        }

        public void processPersistenceUnitDescriptorProperties(PersistenceUnitDescriptor persistenceUnit) {
            if (persistenceUnit.getProperties() != null) {
                this.configurationValues.putAll(persistenceUnit.getProperties());
            }

            this.configurationValues.put("hibernate.ejb.persistenceUnitName", persistenceUnit.getName());
        }

        public void processHibernateConfigXmlResources(LoadedConfig loadedConfig) {
            if (!this.configurationValues.containsKey("hibernate.session_factory_name")) {
                String sfName = loadedConfig.getSessionFactoryName();
                if (sfName != null) {
                    this.configurationValues.put("hibernate.session_factory_name", sfName);
                }
            }

            this.configurationValues.putAll(loadedConfig.getConfigurationValues());
        }

        public Map getConfigurationValues() {
            return this.configurationValues;
        }

        private JaccPermissionDeclarations getJaccPermissions(String jaccContextId) {
            if (this.jaccPermissionsByContextId == null) {
                this.jaccPermissionsByContextId = new HashMap();
            }

            JaccPermissionDeclarations jaccPermissions = (JaccPermissionDeclarations)this.jaccPermissionsByContextId.get(jaccContextId);
            if (jaccPermissions == null) {
                jaccPermissions = new JaccPermissionDeclarations(jaccContextId);
                this.jaccPermissionsByContextId.put(jaccContextId, jaccPermissions);
            }

            return jaccPermissions;
        }

        private void addCacheRegionDefinition(CacheRegionDefinition cacheRegionDefinition) {
            if (this.cacheRegionDefinitions == null) {
                this.cacheRegionDefinitions = new ArrayList();
            }

            this.cacheRegionDefinitions.add(cacheRegionDefinition);
        }
    }

    private static class ServiceRegistryCloser implements SessionFactoryObserver {
        public static final DmEntityManagerFactoryBuilderImpl.ServiceRegistryCloser INSTANCE = new DmEntityManagerFactoryBuilderImpl.ServiceRegistryCloser();

        private ServiceRegistryCloser() {
        }

        public void sessionFactoryCreated(SessionFactory sessionFactory) {
        }

        public void sessionFactoryClosed(SessionFactory sessionFactory) {
            SessionFactoryImplementor sfi = (SessionFactoryImplementor)sessionFactory;
            sfi.getServiceRegistry().destroy();
            ServiceRegistry basicRegistry = sfi.getServiceRegistry().getParentServiceRegistry();
            ((ServiceRegistryImplementor)basicRegistry).destroy();
        }
    }

    private static class JpaEntityNotFoundDelegate implements EntityNotFoundDelegate, Serializable {
        public static final DmEntityManagerFactoryBuilderImpl.JpaEntityNotFoundDelegate INSTANCE = new DmEntityManagerFactoryBuilderImpl.JpaEntityNotFoundDelegate();

        private JpaEntityNotFoundDelegate() {
        }

        @Override
        public void handleEntityNotFound(String entityName, Serializable id) {
            throw new EntityNotFoundException("Unable to find " + entityName + " with id " + id);
        }
    }
}