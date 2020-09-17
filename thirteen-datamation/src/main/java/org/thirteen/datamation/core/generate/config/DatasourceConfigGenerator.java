package org.thirteen.datamation.core.generate.config;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.thirteen.datamation.core.generate.AbstractClassGenerator;
import org.thirteen.datamation.core.generate.AnnotationInfo;
import org.thirteen.datamation.core.generate.ClassInfo;
import org.thirteen.datamation.core.generate.repository.BaseRepository;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Aaron.Sun
 * @description 生成config类
 * @date Created in 16:19 2020/8/10
 * @modified By
 */
public class DatasourceConfigGenerator extends AbstractClassGenerator {

    public DatasourceConfigGenerator() {
        super(DatasourceConfigGenerator.class);
    }

    @Override
    protected byte[] getClassByteArray(ClassInfo classInfo) {
        if (classInfo == null) {
            classInfo = new ClassInfo();
        }
        classInfo.setClassName("DatamationJpaDatasourceConfig");
        classInfo.setAccess("public");
        List<AnnotationInfo> annotationInfos = new ArrayList<>();
        AnnotationInfo configurationAnno = new AnnotationInfo();
        configurationAnno.setDesc("Lorg/springframework/context/annotation/Configuration;");
        AnnotationInfo enableJpaRepositoriesAnno = new AnnotationInfo();
        enableJpaRepositoriesAnno.setDesc("Lorg/springframework/data/jpa/repository/config/EnableJpaRepositories;");
        enableJpaRepositoriesAnno.add("entityManagerFactoryRef", "datamationEntityManagerFactory");
        enableJpaRepositoriesAnno.add("transactionManagerRef", "datamationTransactionManager");
        enableJpaRepositoriesAnno.add("basePackages", new String[]{BaseRepository.class.getPackageName()});
        AnnotationInfo enableTransactionManagementAnno = new AnnotationInfo();
        enableTransactionManagementAnno.setDesc("Lorg/springframework/transaction/annotation/EnableTransactionManagement;");
        annotationInfos.add(configurationAnno);
        annotationInfos.add(enableJpaRepositoriesAnno);
        annotationInfos.add(enableTransactionManagementAnno);
        classInfo.setAnnotationInfos(annotationInfos);

        ClassWriter cw = new ClassWriter(0);
        // 设置类基本属性
        // 参数：版本号，类的访问标志，类名（包含路径），签名，父类，内部接口
        cw.visit(57, accessOf(classInfo.getAccess()), this.defaultPackage + classInfo.getClassName(),
            classInfo.getSignature(), classInfo.getSuperName(), classInfo.getInterfaces());
        // 无参构造函数处理
        initMethodHandle(cw);
        // 注解处理
        annotationHandle(cw, classInfo.getAnnotationInfos());

//        dataSourceMethodHandle(cw);
        entityManagerFactoryMethodHandle(cw);
        transactionManagerMethodHandle(cw);

        // cw结束
        cw.visitEnd();
        // 转为字节数组
        return cw.toByteArray();
    }

    @Override
    protected byte[] getClassByteArray(Set<String> mappers) {
        return new byte[0];
    }

    private void dataSourceMethodHandle(ClassWriter cw) {
        String dataSource = "Ljavax/sql/DataSource;";
        String dataSourceBuilder = "Lorg/springframework/boot/jdbc/DataSourceBuilder;";

        MethodVisitor mv = cw.visitMethod(ACC_PUBLIC, "dataSource",
            "()" + dataSource,
            null, null);
        // 添加Bean注解
        AnnotationVisitor beanAnno = mv.visitAnnotation("Lorg/springframework/context/annotation/Bean;", true);
        beanAnno.visitEnd();
        AnnotationVisitor configurationPropertiesAnno = mv
            .visitAnnotation("Lorg/springframework/boot/context/properties/ConfigurationProperties;", true);
        configurationPropertiesAnno.visit("prefix", "spring.datasource");
        configurationPropertiesAnno.visitEnd();

        mv.visitCode();
        mv.visitMethodInsn(INVOKESTATIC, dataSourceBuilder.replace("L", "").replace(";", ""), "create", "()" + dataSourceBuilder);
        mv.visitLdcInsn(Type.getType(com.zaxxer.hikari.HikariDataSource.class));
        mv.visitMethodInsn(INVOKEVIRTUAL, dataSourceBuilder, "type", "(Ljava/lang/Class;)" + dataSourceBuilder);
        mv.visitMethodInsn(INVOKEVIRTUAL, dataSourceBuilder, "build", "()" + dataSource);
        mv.visitInsn(loadAndReturnOf(dataSource)[1]);
        mv.visitMaxs(2, 2);
        mv.visitEnd();
    }

    private void entityManagerFactoryMethodHandle(ClassWriter cw) {
        String factoryBeanTypeOf = "Lorg/springframework/orm/jpa/LocalContainerEntityManagerFactoryBean;";
        String jpaVendorAdapter = "Lorg/springframework/orm/jpa/vendor/HibernateJpaVendorAdapter;";
        String jpaProperties = "Lorg/springframework/boot/autoconfigure/orm/jpa/JpaProperties;";
        String dataSource = "Ljavax/sql/DataSource;";
//        String factoryBeanTypeOf = "org/thirteen/datamation/core/orm/DatamationEntityManagerFactoryBean";

        MethodVisitor mv = cw.visitMethod(ACC_PUBLIC, "datamationEntityManagerFactory",
            "(" + dataSource + ")" + factoryBeanTypeOf,
            null, null);
        // 添加Bean注解
        AnnotationVisitor av = mv.visitAnnotation("Lorg/springframework/context/annotation/Bean;", true);
        av.visitEnd();

        mv.visitCode();

//        mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "()Ljava/io/PrintStream;");
//        mv.visitLdcInsn("111111111111111111111");
//        mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V");

        mv.visitTypeInsn(NEW, factoryBeanTypeOf);
        mv.visitInsn(DUP);
        mv.visitMethodInsn(INVOKESPECIAL, factoryBeanTypeOf, "<init>", "()V");
        mv.visitVarInsn(ASTORE, 2);

        mv.visitTypeInsn(NEW, jpaVendorAdapter);
        mv.visitInsn(DUP);
        mv.visitMethodInsn(INVOKESPECIAL, jpaVendorAdapter, "<init>", "()V");
        mv.visitVarInsn(ASTORE, 3);

//        mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "()Ljava/io/PrintStream;");
//        mv.visitVarInsn(ALOAD, 3);
//        mv.visitMethodInsn(INVOKEVIRTUAL, "Ljava/io/PrintStream;", "println", "(Ljava/lang/String;)V");
        mv.visitVarInsn(ALOAD, 2);
        mv.visitVarInsn(ALOAD, 3);
//        mv.visitLdcInsn(true);
//        mv.visitMethodInsn(INVOKEVIRTUAL, jpaVendorAdapter, "setGenerateDdl", "(Ljava/lang/Boolean;)V");
//        mv.visitVarInsn(ALOAD, 2);
        mv.visitMethodInsn(INVOKEVIRTUAL, factoryBeanTypeOf, "setJpaVendorAdapter", "(Lorg/springframework/orm/jpa/JpaVendorAdapter;)V");
//        mv.visitVarInsn(ALOAD, 4);
//        mv.visitVarInsn(ALOAD, 2);
//        mv.visitMethodInsn(INVOKEVIRTUAL, jpaProperties, "getProperties", "()Ljava/util/Map;");
//        mv.visitMethodInsn(INVOKEVIRTUAL, factoryBeanTypeOf, "setJpaPropertyMap", "(" + jpaProperties + ")V");
        mv.visitVarInsn(ALOAD, 2);
        mv.visitVarInsn(ALOAD, 1);
//        mv.visitMethodInsn(INVOKEVIRTUAL, Type.getDescriptor(this.getClass()), "dataSource", "()" + dataSource);
        mv.visitMethodInsn(INVOKEVIRTUAL, factoryBeanTypeOf, "setDataSource", "(" + dataSource + ")V");
        mv.visitVarInsn(ALOAD, 2);

        mv.visitInsn(ICONST_1);
        mv.visitTypeInsn(ANEWARRAY, "Ljava/lang/String;");
        mv.visitInsn(DUP);
        mv.visitInsn(ICONST_0);
        mv.visitLdcInsn("org.thirteen.datamation.core.generate.po");
        mv.visitInsn(AASTORE);
        mv.visitMethodInsn(INVOKEVIRTUAL, factoryBeanTypeOf, "setPackagesToScan", "([Ljava/lang/String;)V");
        mv.visitVarInsn(ALOAD, 2);
        mv.visitInsn(loadAndReturnOf(factoryBeanTypeOf)[1]);
        mv.visitMaxs(5, 4);
        mv.visitEnd();
    }

    private void transactionManagerMethodHandle(ClassWriter cw) {
        String transactionManagerTypeOf = "Lorg/springframework/transaction/PlatformTransactionManager;";
        String jpaTransactionManagerTypeOf = "Lorg/springframework/orm/jpa/JpaTransactionManager;";
        String entityManagerFactory = "Ljavax/persistence/EntityManagerFactory;";

        MethodVisitor mv = cw.visitMethod(ACC_PUBLIC, "datamationTransactionManager",
            "(" + entityManagerFactory + ")" + transactionManagerTypeOf,
            null, null);
        // 添加Bean注解
        AnnotationVisitor av = mv.visitAnnotation("Lorg/springframework/context/annotation/Bean;", true);
        av.visitEnd();

//        AnnotationVisitor apv = mv.visitParameterAnnotation(0,
//            "Lorg/springframework/beans/factory/annotation/Qualifier;", true);
//        apv.visit("value", "datamationEntityManagerFactory");
//        apv.visitEnd();

        mv.visitCode();
        mv.visitTypeInsn(NEW, jpaTransactionManagerTypeOf);
        mv.visitInsn(DUP);
        mv.visitMethodInsn(INVOKESPECIAL, jpaTransactionManagerTypeOf, "<init>", "()V");
        mv.visitVarInsn(ASTORE, 2);
        mv.visitVarInsn(ALOAD, 2);
        mv.visitVarInsn(ALOAD, 1);
        mv.visitMethodInsn(INVOKEVIRTUAL, jpaTransactionManagerTypeOf, "setEntityManagerFactory", "(" + entityManagerFactory + ")V");

        mv.visitVarInsn(ALOAD, 2);
        mv.visitInsn(loadAndReturnOf(transactionManagerTypeOf)[1]);
        mv.visitMaxs(2, 3);
        mv.visitEnd();
    }

    public static void main(String[] args) throws Exception {
        DatasourceConfigGenerator generator = new DatasourceConfigGenerator();
        ClassInfo classInfo = new ClassInfo();
        classInfo.setClassName("DatamationJpaDatasourceConfig");
        generator.writeClass(classInfo);
    }
}