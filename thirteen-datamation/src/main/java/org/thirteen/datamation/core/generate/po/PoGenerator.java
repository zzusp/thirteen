package org.thirteen.datamation.core.generate.po;

import org.objectweb.asm.ClassWriter;
import org.thirteen.datamation.core.generate.AbstractClassGenerator;
import org.thirteen.datamation.core.generate.AnnotationInfo;
import org.thirteen.datamation.core.generate.ClassInfo;
import org.thirteen.datamation.core.generate.FieldInfo;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Aaron.Sun
 * @description 生成po类
 * @date Created in 16:19 2020/8/10
 * @modified By
 */
public class PoGenerator extends AbstractClassGenerator {

    public PoGenerator() {
        super(PoGenerator.class);
    }

    public PoGenerator(Class<?> neighbor) {
        super(neighbor);
    }

    /**
     * 生成 po class字节码数组
     *
     * @param classInfo 类信息
     * @return class字节码数组
     */
    @Override
    protected byte[] getClassByteArray(ClassInfo classInfo) {
        ClassWriter cw = new ClassWriter(0);
        // 设置类基本属性
        // 参数：版本号，类的访问标志，类名（包含路径），签名，父类，内部接口
        cw.visit(57, accessOf(classInfo.getAccess()), this.defaultPackage + classInfo.getClassName(),
            classInfo.getSignature(), classInfo.getSuperName(), classInfo.getInterfaces());
        // 注解处理
        annotationHandle(cw, classInfo.getAnnotationInfos());
        // 无参构造函数处理
        initMethodHandle(cw);
        // 字段处理（包含get/set方法）
        fieldHandle(cw, classInfo.getFieldInfos(), this.defaultPackage + classInfo.getClassName());
        // toString
        toStringMethodHandle(cw, classInfo.getFieldInfos(), this.defaultPackage + classInfo.getClassName());
        // cw结束
        cw.visitEnd();
        // 转为字节数组
        return cw.toByteArray();
    }

    @Override
    protected byte[] getClassByteArray(Set<String> mappers) {
        return new byte[0];
    }

    public static void main(String[] args) throws Exception {
//        ClassInfo classInfo = new ClassInfo();
//        classInfo.setClassName("DmTestPO");
//        classInfo.setAccess("public");
//        classInfo.setInterfaces(new String[]{"java/io/Serializable"});
//
//        List<AnnotationInfo> annotationInfos = new ArrayList<>();
//        AnnotationInfo tableAnno = new AnnotationInfo();
//        tableAnno.setDesc("javax.persistence.Table");
//        tableAnno.add("name", "dm_test");
//        annotationInfos.add(tableAnno);
//        classInfo.setAnnotationInfos(annotationInfos);
//
//        List<FieldInfo> fieldInfos = new ArrayList<>();
//        FieldInfo idField = new FieldInfo();
//        idField.setName("id");
//        idField.setFieldClass("Ljava/lang/String;");
//        idField.setAccess("private");
//        fieldInfos.add(idField);
//        classInfo.setFieldInfos(fieldInfos);
//
//        PoGenerator poGenerate = new PoGenerator();
//        poGenerate.writeClass(classInfo);
//        Class<?> c = poGenerate.generate(classInfo);
//        System.out.println(c.getMethod("getId").invoke(c.getDeclaredConstructor().newInstance()));
    }

}
