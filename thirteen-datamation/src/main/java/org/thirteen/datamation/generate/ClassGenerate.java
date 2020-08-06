package org.thirteen.datamation.generate;

import org.objectweb.asm.*;
import org.springframework.util.StringUtils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class ClassGenerate extends ClassLoader implements Opcodes {

    public void generate(ClassInfo classInfo) throws IOException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException, URISyntaxException {
        ClassWriter cw = new ClassWriter(0);
        String fullName = classInfo.getPackagePath() + "/" + classInfo.getClassName();
        // 设置类基本属性
        // 参数：版本号，类的访问标志，类名（包含路径），签名，父类，内部接口
        cw.visit(V1_7, accessOf(classInfo.getAccess()), fullName, null, classInfo.getSuperName(), null);
        // 注解处理
        annotationHandle(cw, classInfo.getAnnotationInfos());
        // 字段处理（包含get/set方法）
        fieldHandle(cw, classInfo.getFieldInfos(), fullName);
        // cw结束
        cw.visitEnd();
        // 转为字节数组
        byte[] code = cw.toByteArray();
        //将二进制流写到本地磁盘上
        FileOutputStream fos = new FileOutputStream(ClassLoader.getSystemResource("").toURI().getPath() + fullName + ".class");
        fos.write(code);
        fos.close();
    }


    /**
     * 注解处理
     *
     * @param cw 类构建器
     * @param annotationInfos 注解信息集合
     */
    private void annotationHandle(ClassWriter cw, List<AnnotationInfo> annotationInfos) {
        if (annotationInfos != null && annotationInfos.size() > 0) {
            // 注释
            AnnotationVisitor av;
            for (AnnotationInfo info : annotationInfos) {
                // 注解开始
                av = cw.visitAnnotation(info.getDesc(), info.getVisible());
                if (info.getParams() != null && info.getParams().size() > 0) {
                    for (AnnotationInfo.Param param : info.getParams()) {
                        // 设置注解参数
                        av.visit(param.getName(), param.getValue());
                    }
                }
                // 注解结束
                av.visitEnd();
            }
        }
    }

    /**
     * 字段处理
     *
     * @param cw 类构建器
     * @param fieldInfos 字段信息集合
     * @param className 完整类名
     */
    private void fieldHandle(ClassWriter cw, List<FieldInfo> fieldInfos, String className) {
        if (fieldInfos != null && fieldInfos.size() > 0) {
            String fieldName;
            String typeOf;
            for (FieldInfo info : fieldInfos) {
                fieldName = info.getName();
                typeOf = Type.getType(info.getFieldClass()).getDescriptor();
                // 字段
                cw.visitField(accessOf(info.getAccess()), fieldName, typeOf, null, 0).visitEnd();
                // set方法
                setMethodHandle(cw, fieldName, typeOf, className);
                // get方法
                getMethodHandle(cw, fieldName, typeOf, className);
            }
        }
    }

    /**
     * 字段set方法
     *
     * @param cw 类构建器
     * @param fieldName 字段名称
     * @param typeOf 字段类型
     * @param className 完整类名
     */
    private void setMethodHandle(ClassWriter cw, String fieldName, String typeOf, String className) {
        String setMethodName = "set" + StringUtils.capitalize(fieldName);
        MethodVisitor mv = cw.visitMethod(ACC_PUBLIC, setMethodName, "(" + typeOf + ")V", null, null);
        mv.visitCode();
        mv.visitVarInsn(ALOAD, 0);
        mv.visitVarInsn(loadAndReturnOf(typeOf)[0], 1);
        mv.visitFieldInsn(PUTFIELD, className, fieldName, typeOf);
        mv.visitInsn(RETURN);
        mv.visitMaxs(2, 2);
        mv.visitEnd();
    }

    /**
     * 字段get方法
     *
     * @param cw 类构建器
     * @param fieldName 字段名称
     * @param typeOf 字段类型
     * @param className 完整类名
     */
    private void getMethodHandle(ClassWriter cw, String fieldName, String typeOf, String className) {
        String getMethodName = "get" + StringUtils.capitalize(fieldName);
        MethodVisitor mv = cw.visitMethod(ACC_PUBLIC, getMethodName, "()" + typeOf, null, null);
        mv.visitCode();
        mv.visitVarInsn(ALOAD, 0);
        mv.visitFieldInsn(GETFIELD, className, fieldName, typeOf);
        mv.visitInsn(loadAndReturnOf(typeOf)[1]);
        mv.visitMaxs(2, 1);
        mv.visitEnd();
    }

    /**
     * 根据类型字符串获取对应类型的load或return标识
     *
     * @param typeof 类型字符串
     * @return load或return标识
     */
    private static int[] loadAndReturnOf(String typeof) {
        switch (typeof) {
            case "I":
            case "Z":
                return new int[]{ILOAD, IRETURN};
            case "J":
                return new int[]{LLOAD, LRETURN};
            case "D":
                return new int[]{DLOAD, DRETURN};
            case "F":
                return new int[]{FLOAD, FRETURN};
            default:
                return new int[]{ALOAD, ARETURN};
        }
    }

    /**
     * 根据访问标志字符串获取对应类型的访问标识
     *
     * @param access 访问标志字符串
     * @return 访问标识
     */
    private static int accessOf(String access) {
        switch (access) {
            case "private":
                return ACC_PRIVATE;
            case "protected":
                return ACC_PROTECTED;
            case "static":
                return ACC_STATIC;
            case "final":
                return ACC_FINAL;
            case "super":
                return ACC_SUPER;
            case "public":
            default:
                return ACC_PUBLIC;
        }
    }

    public static void main(String[] args) throws Exception {
        ClassInfo classInfo = new ClassInfo();
        classInfo.setPackagePath("org/thirteen/datamation/model/po");
        classInfo.setClassName("DmTestPO");
        classInfo.setAccess("public");
        classInfo.setSuperName("java.io.Serializable");

        List<AnnotationInfo> annotationInfos = new ArrayList<>();
        AnnotationInfo tableAnno = new AnnotationInfo();
        tableAnno.setDesc("javax.persistence.Table");
        tableAnno.add("name", "dm_test");
        annotationInfos.add(tableAnno);
        classInfo.setAnnotationInfos(annotationInfos);

        List<FieldInfo> fieldInfos = new ArrayList<>();
        FieldInfo idField = new FieldInfo();
        idField.setName("id");
        idField.setFieldClass("Ljava/lang/String;");
        idField.setAccess("private");
        fieldInfos.add(idField);
        classInfo.setFieldInfos(fieldInfos);

        ClassGenerate classGenerate = new ClassGenerate();
        classGenerate.generate(classInfo);
    }


}
