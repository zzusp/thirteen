package org.thirteen.datamation.core.generate;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import static org.springframework.util.CollectionUtils.isEmpty;
import static org.thirteen.datamation.core.DmCodes.*;

/**
 * @author Aaron.Sun
 * @description 生成一个class所需要的所有信息
 * @date Created in 17:36 2020/8/5
 * @modified By
 */
public class ClassInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 类名
     */
    private String className;
    /**
     * 类的访问标志
     */
    private String access;
    /**
     * 签名（设置泛型值时使用）
     */
    private String signature;
    /**
     * 父类名
     */
    private String superName;
    /**
     * 实现的接口
     */
    String[] interfaces;
    /**
     * 类注解信息集合
     */
    private List<AnnotationInfo> annotationInfos;
    /**
     * 类字段信息集合
     */
    private List<FieldInfo> fieldInfos;

    public ClassInfo() {
        this.superName = "java/lang/Object";
    }

    /**
     * 获取实体类主键字段，驼峰命名形式（不支持联合主键）
     *
     * @return 实体类主键字段，驼峰命名形式
     */
    public String getIdField() {
        return getFieldByColumnType(COLUMN_TYPE_ID);
    }

    /**
     * 获取实体类删除标识字段，驼峰命名形式
     *
     * @return 实体类删除标识字段，驼峰命名形式
     */
    public String getDelFlagField() {
        return getFieldByColumnType(COLUMN_TYPE_DEL_FLAG);
    }

    /**
     * 获取实体类版本号字段，驼峰命名形式
     *
     * @return 实体类版本号字段，驼峰命名形式
     */
    public String getVersionField() {
        return getFieldByColumnType(COLUMN_TYPE_VERSION);
    }

    /**
     * 根据字段类型，获取字段名称
     *
     * @param columnType 字段类型
     * @return 字段名称
     */
    private String getFieldByColumnType(Byte columnType) {
        if (columnType == null) {
            return null;
        }
        if (isEmpty(fieldInfos)) {
            return null;
        }
        String field = null;
        for (FieldInfo fieldInfo : fieldInfos) {
            if (columnType.equals(fieldInfo.getColumnType())) {
                field = fieldInfo.getName();
                break;
            }
        }
        return field;
    }

    /**
     * 判断是否包含删除标识字段
     *
     * @return 是否包含删除标识字段
     */
    public boolean containsDelFlag() {
        return getDelFlagField() != null;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getAccess() {
        return access;
    }

    public void setAccess(String access) {
        this.access = access;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getSuperName() {
        return superName;
    }

    public void setSuperName(String superName) {
        this.superName = superName;
    }

    public String[] getInterfaces() {
        return interfaces;
    }

    public void setInterfaces(String[] interfaces) {
        this.interfaces = interfaces;
    }

    public List<AnnotationInfo> getAnnotationInfos() {
        return annotationInfos;
    }

    public void setAnnotationInfos(List<AnnotationInfo> annotationInfos) {
        this.annotationInfos = annotationInfos;
    }

    public List<FieldInfo> getFieldInfos() {
        return fieldInfos;
    }

    public void setFieldInfos(List<FieldInfo> fieldInfos) {
        this.fieldInfos = fieldInfos;
    }

    @Override
    public String toString() {
        return "ClassInfo{" +
            "className='" + className + '\'' +
            ", access='" + access + '\'' +
            ", signature='" + signature + '\'' +
            ", superName='" + superName + '\'' +
            ", interfaces=" + Arrays.toString(interfaces) +
            ", annotationInfos=" + annotationInfos +
            ", fieldInfos=" + fieldInfos +
            '}';
    }
}
