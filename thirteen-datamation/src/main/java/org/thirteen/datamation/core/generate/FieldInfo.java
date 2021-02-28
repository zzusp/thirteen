package org.thirteen.datamation.core.generate;

import java.io.Serializable;
import java.util.List;

/**
 * @author Aaron.Sun
 * @description 生成一个字段所需要的所有信息
 * @date Created in 17:27 2020/8/6
 * @modified By
 */
public class FieldInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 字段名称
     */
    private String name;
    /**
     * 字段class
     */
    private String fieldClass;
    /**
     * 字段的访问标志
     */
    private String access;
    /**
     * 字段类型
     */
    private Byte columnType;
    /**
     * 类注解信息集合
     */
    private List<AnnotationInfo> annotationInfos;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFieldClass() {
        return fieldClass;
    }

    public void setFieldClass(String fieldClass) {
        this.fieldClass = fieldClass;
    }

    public String getAccess() {
        return access;
    }

    public void setAccess(String access) {
        this.access = access;
    }

    public Byte getColumnType() {
        return columnType;
    }

    public void setColumnType(Byte columnType) {
        this.columnType = columnType;
    }

    public List<AnnotationInfo> getAnnotationInfos() {
        return annotationInfos;
    }

    public void setAnnotationInfos(List<AnnotationInfo> annotationInfos) {
        this.annotationInfos = annotationInfos;
    }

    @Override
    public String toString() {
        return "FieldInfo{" +
            "name='" + name + '\'' +
            ", fieldClass='" + fieldClass + '\'' +
            ", access='" + access + '\'' +
            ", columnType='" + columnType + '\'' +
            ", annotationInfos=" + annotationInfos +
            '}';
    }
}
