package org.thirteen.datamation.core.generate;

import org.springframework.util.CollectionUtils;
import org.thirteen.datamation.model.po.DmColumnPO;
import org.thirteen.datamation.model.po.DmTablePO;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Aaron.Sun
 * @description 数据库配置转为生成class所需要的信息。抽象
 * @date Created in 16:19 2020/8/10
 * @modified By
 */
public abstract class AbstractClassConverter {

    protected static final String PO_SUFFIX = "PO";
    protected static final String REPOSITORY_SUFFIX = "Repository";

    /**
     * 获取生成class所需要的所有信息
     *
     * @param table 数据库配置信息
     * @return 生成class所需要的所有信息
     */
    public ClassInfo getClassInfo(DmTablePO table) {
        ClassInfo classInfo = this.tableToClass(table);
        classInfo.setAnnotationInfos(this.tableToAnnotation(table));
        FieldInfo fieldInfo;
        if (!CollectionUtils.isEmpty(table.getColumns())) {
            classInfo.setFieldInfos(new ArrayList<>());
            for (DmColumnPO column : table.getColumns()) {
                fieldInfo = this.columnToField(column);
                if (fieldInfo != null) {
                    // 根据db类型设置对应java类型
                    fieldInfo.setFieldClass(getJavaType(column.getDbType()));
                    fieldInfo.setAnnotationInfos(this.columnToAnnotation(column));
                    classInfo.getFieldInfos().add(fieldInfo);
                }
            }
        }
        return classInfo;
    }

    /**
     * table转classInfo
     *
     * @param table 数据库配置表信息
     * @return classInfo（生成class所需要的信息）
     */
    protected abstract ClassInfo tableToClass(DmTablePO table);

    /**
     * table转classInfo
     *
     * @param table 数据库配置表信息
     * @return classInfo（生成class的注解所需要的信息）
     */
    protected abstract List<AnnotationInfo> tableToAnnotation(DmTablePO table);

    /**
     * table转classInfo
     *
     * @param column 数据库配置列信息
     * @return classInfo（生成class中的字段的注解所需要的信息）
     */
    protected abstract List<AnnotationInfo> columnToAnnotation(DmColumnPO column);

    /**
     * column转fieldInfo
     *
     * @param column 数据库配置列信息
     * @return fieldInfo（生成class中的字段所需要的信息）
     */
    protected abstract FieldInfo columnToField(DmColumnPO column);

    /**
     * 数据库类型转java类型
     *
     * @param dbType 数据库类型
     * @return java类型（包装类型）
     */
    public static String getJavaType(String dbType) {
        // 先转大写
        dbType = dbType.toUpperCase();
        String javaType;
        switch (dbType) {
            case "INT":
                javaType = "Ljava/lang/Integer;";
                break;
            case "TINYINT":
                javaType = "Ljava/lang/Byte;";
                break;
            case "BIGINT":
                javaType = "Ljava/lang/Long;";
                break;
            case "FLOAT":
                javaType = "Ljava/lang/Float;";
                break;
            case "DOUBLE":
                javaType = "Ljava/lang/Double;";
                break;
            case "DATE":
                javaType = "Ljava/time/LocalDate;";
                break;
            case "DATETIME":
                javaType = "Ljava/time/LocalDateTime;";
                break;
            case "CHAR":
            case "VARCHAR":
            default:
                javaType = "Ljava/lang/String;";
                break;
        }
        return javaType;
    }
}
