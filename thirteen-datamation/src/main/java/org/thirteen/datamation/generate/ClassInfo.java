package org.thirteen.datamation.generate;

import javassist.bytecode.MethodInfo;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

/**
 * @author Aaron.Sun
 * @description 生成一个class所需要的所有信息
 * @date Created in 17:36 2020/8/5
 * @modified By
 */
public class ClassInfo implements Serializable {

    /** 类名 */
    private String className;
    /** 类的访问标志 */
    private String access;
    /** 父类名 */
    private String superName;
    /** 实现的接口 */
    String[] interfaces;
    /** 类注解信息集合 */
    private List<AnnotationInfo> annotationInfos;
    /** 类方法信息集合 */
    private List<MethodInfo> methodInfos;
    /** 类字段信息集合 */
    private List<FieldInfo> fieldInfos;

    public ClassInfo() {
        this.superName = "java/lang/Object";
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

    public List<MethodInfo> getMethodInfos() {
        return methodInfos;
    }

    public void setMethodInfos(List<MethodInfo> methodInfos) {
        this.methodInfos = methodInfos;
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
            ", superName='" + superName + '\'' +
            ", interfaces=" + Arrays.toString(interfaces) +
            ", annotationInfos=" + annotationInfos +
            ", methodInfos=" + methodInfos +
            ", fieldInfos=" + fieldInfos +
            '}';
    }
}
