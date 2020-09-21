package org.thirteen.datamation.core.generate.repository;

import aj.org.objectweb.asm.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.thirteen.datamation.core.exception.DatamationException;
import org.thirteen.datamation.core.generate.AbstractClassConverter;
import org.thirteen.datamation.core.generate.AnnotationInfo;
import org.thirteen.datamation.core.generate.ClassInfo;
import org.thirteen.datamation.core.generate.FieldInfo;
import org.thirteen.datamation.model.po.DmColumnPO;
import org.thirteen.datamation.model.po.DmTablePO;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Aaron.Sun
 * @description 数据库配置信息转为生成类所需要的信息
 * @date Created in 15:54 2020/8/11
 * @modified By
 */
public class RepositoryConverter extends AbstractClassConverter {

    private static final Logger logger = LoggerFactory.getLogger(RepositoryConverter.class);

    /** po类完整路径 */
    private final String poClassName;
    /** 主键的java类型 */
    private final String pkJavaType;

    public RepositoryConverter(Class<?> poClass) {
        this(Type.getInternalName(poClass), "Ljava/lang/String;");
    }

    public RepositoryConverter(String poClassName, String pkJavaType) {
        // po是否存在校验。未找到PO类时，应中断运行，所以抛出中断异常
        try {
            String regex = "/";
            RepositoryGenerator.class.getClassLoader().loadClass(poClassName.replaceAll(regex, "."));
        } catch (ClassNotFoundException e) {
            logger.error("po class not found: {}", poClassName);
            throw new DatamationException("po class not found: " + poClassName, e);
        }
        this.poClassName = poClassName;
        this.pkJavaType = pkJavaType;
    }

    /**
     * table转classInfo
     *
     * @param table 数据库配置表信息
     * @return classInfo（生成class所需要的信息）
     */
    @Override
    protected ClassInfo tableToClass(DmTablePO table) {
        // 接口
        String interfaceStr = Type.getInternalName(BaseRepository.class);
        // 签名（泛型。Ljava/lang/Object;表示超类或超接口）
        String signature = "Ljava/lang/Object;L" + interfaceStr + "<L" + poClassName + ";" + pkJavaType + ">;";

        ClassInfo classInfo = new ClassInfo();
        classInfo.setClassName(lineToHumpAndStartWithCapitalize(table.getCode()) + REPOSITORY_SUFFIX);
        classInfo.setAccess("interface");
        classInfo.setSignature(signature);
        classInfo.setInterfaces(new String[]{interfaceStr});
        return classInfo;
    }

    /**
     * table转classInfo
     *
     * @param table 数据库配置表信息
     * @return classInfo（生成class的注解所需要的信息）
     */
    @Override
    protected List<AnnotationInfo> tableToAnnotation(DmTablePO table) {
        List<AnnotationInfo> annotationInfos = new ArrayList<>();
        // repository注解
        AnnotationInfo repositoryAnno = new AnnotationInfo();
        repositoryAnno.setDesc("Lorg/springframework/stereotype/Repository;");
        annotationInfos.add(repositoryAnno);
        return annotationInfos;
    }

    /**
     * table转classInfo
     *
     * @param column 数据库配置列信息
     * @return classInfo（生成class中的字段的注解所需要的信息）
     */
    @Override
    protected List<AnnotationInfo> columnToAnnotation(DmColumnPO column) {
        return new ArrayList<>();
    }

    /**
     * column转fieldInfo
     *
     * @param column 数据库配置列信息
     * @return fieldInfo（生成class中的字段所需要的信息）
     */
    @Override
    protected FieldInfo columnToField(DmColumnPO column) {
        return null;
    }

}
