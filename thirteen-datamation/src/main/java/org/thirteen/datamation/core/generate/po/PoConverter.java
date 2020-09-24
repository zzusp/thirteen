package org.thirteen.datamation.core.generate.po;

import org.thirteen.datamation.core.generate.AbstractClassConverter;
import org.thirteen.datamation.core.generate.AnnotationInfo;
import org.thirteen.datamation.core.generate.ClassInfo;
import org.thirteen.datamation.core.generate.FieldInfo;
import org.thirteen.datamation.model.po.DmColumnPO;
import org.thirteen.datamation.model.po.DmTablePO;

import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.isNotEmpty;
import static org.thirteen.datamation.core.DmCodes.COLUMN_TYPE_ID;
import static org.thirteen.datamation.core.DmCodes.COLUMN_TYPE_VERSION;
import static org.thirteen.datamation.util.StringUtils.lineToHump;
import static org.thirteen.datamation.util.StringUtils.lineToHumpAndStartWithCapitalize;

/**
 * @author Aaron.Sun
 * @description 数据库配置信息转为生成类所需要的信息
 * @date Created in 15:54 2020/8/11
 * @modified By
 */
public class PoConverter extends AbstractClassConverter {

    /**
     * table转classInfo
     *
     * @param table 数据库配置表信息
     * @return classInfo（生成class所需要的信息）
     */
    @Override
    protected ClassInfo tableToClass(DmTablePO table) {
        ClassInfo classInfo = new ClassInfo();
        classInfo.setClassName(lineToHumpAndStartWithCapitalize(table.getCode()) + PO_SUFFIX);
        classInfo.setAccess("public");
        classInfo.setInterfaces(new String[]{"java/io/Serializable"});
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
        // entity注解
        AnnotationInfo entityAnno = new AnnotationInfo();
        entityAnno.setDesc("Ljavax/persistence/Entity;");
        // table注解
        AnnotationInfo tableAnno = new AnnotationInfo();
        tableAnno.setDesc("Ljavax/persistence/Table;");
        tableAnno.add("name", table.getCode());
        // hibernate的table注解，生成table时可添加上表描述
        AnnotationInfo hibernateTableAnno = new AnnotationInfo();
        hibernateTableAnno.setDesc("Lorg/hibernate/annotations/Table;");
        hibernateTableAnno.add("appliesTo", table.getCode());
        hibernateTableAnno.add("comment", table.getName());
        // 添加到集合中
        annotationInfos.add(entityAnno);
        annotationInfos.add(tableAnno);
        annotationInfos.add(hibernateTableAnno);
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
        List<AnnotationInfo> annotationInfos = new ArrayList<>();
        // 判断是否为主键字段
        if (COLUMN_TYPE_ID.equals(column.getColumnType())) {
            AnnotationInfo idAnno = new AnnotationInfo();
            idAnno.setDesc("Ljavax/persistence/Id;");
            AnnotationInfo genericGeneratorAnno = new AnnotationInfo();
            genericGeneratorAnno.setDesc("Lorg/hibernate/annotations/GenericGenerator;");
            genericGeneratorAnno.add("name", "pk_uuid");
            genericGeneratorAnno.add("strategy", "uuid");
            AnnotationInfo generatedValueAnno = new AnnotationInfo();
            generatedValueAnno.setDesc("Ljavax/persistence/GeneratedValue;");
            generatedValueAnno.add("generator", "pk_uuid");
            annotationInfos.add(idAnno);
            annotationInfos.add(genericGeneratorAnno);
            annotationInfos.add(generatedValueAnno);
        }
        // 判断是否为版本号字段
        else if (COLUMN_TYPE_VERSION.equals(column.getColumnType())) {
            AnnotationInfo versionAnno = new AnnotationInfo();
            versionAnno.setDesc("Ljavax/persistence/Version;");
            annotationInfos.add(versionAnno);
        }
        // column注解
        AnnotationInfo columnAnno = new AnnotationInfo();
        columnAnno.setDesc("Ljavax/persistence/Column;");
        columnAnno.add("name", column.getCode());
        // 拼接columnDefinition
        StringBuilder sb = new StringBuilder();
        sb.append(column.getDbType());
        if (column.getLength() != null && column.getLength() > 0) {
            sb.append("(").append(column.getLength()).append(")");
        }
        if (column.getNotNull() != null && column.getNotNull() == 1) {
            sb.append(" NOT NULL ");
        }
        sb.append(" COMMENT '").append(column.getName());
        if (isNotEmpty(column.getRemark())) {
            sb.append(" ").append(column.getRemark());
        }
        sb.append("'");
        // 添加到集合中
        columnAnno.add("columnDefinition", sb.toString());
        annotationInfos.add(columnAnno);
        return annotationInfos;
    }

    /**
     * column转fieldInfo
     *
     * @param column 数据库配置列信息
     * @return fieldInfo（生成class中的字段所需要的信息）
     */
    @Override
    protected FieldInfo columnToField(DmColumnPO column) {
        FieldInfo fieldInfo = new FieldInfo();
        fieldInfo.setName(lineToHump(column.getCode()));
        fieldInfo.setAccess("private");
        fieldInfo.setColumnType(column.getColumnType());
        return fieldInfo;
    }

}
