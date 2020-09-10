package org.thirteen.datamation.core.generate;

import javassist.util.proxy.DefineClassHelper;
import org.objectweb.asm.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.thirteen.datamation.core.exception.DatamationException;
import org.thirteen.datamation.core.generate.repository.BaseRepository;

import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.net.URISyntaxException;
import java.util.Iterator;
import java.util.List;

/**
 * @author Aaron.Sun
 * @description 生成class类。抽象
 * @date Created in 16:19 2020/8/10
 * @modified By
 */
public abstract class AbstractClassGenerator extends ClassLoader implements Opcodes {

    private static final Logger logger = LoggerFactory.getLogger(AbstractClassGenerator.class);

    private static final String TYPEOF_BOOLEAN = "Z";
    /** 默认的class生成路径 */
    protected String defaultPackage;
    /** 生成类的邻类 */
    private final Class<?> neighbor;

    public AbstractClassGenerator() {
        this(AbstractClassGenerator.class);
    }

    public AbstractClassGenerator(Class<?> neighbor) {
        this.neighbor = neighbor;
        this.defaultPackage = neighbor.getPackageName().replaceAll("\\.", "\\/") + "/";
    }

    /**
     * 生成class
     *
     * @param classInfo 类信息
     */
    public Class<?> generate(ClassInfo classInfo) {
        try {
            return defineClass(this.neighbor, getClassByteArray(classInfo));
        } catch (Exception e) {
            logger.error("动态生成失败，class：{}", classInfo.getClassName());
            throw new DatamationException("动态生成失败，class：" + classInfo.getClassName(), e);
        }
    }

    /**
     * 生成class并写入class文件
     *
     * @param classInfo 类信息
     * @throws IOException        IO异常
     * @throws URISyntaxException 路径句法异常
     */
    public void writeClass(ClassInfo classInfo) throws IOException, URISyntaxException {
        String classFilePath = ClassLoader.getSystemResource("").toURI().getPath()
            + this.defaultPackage + classInfo.getClassName() + ".class";
        //将二进制流写到本地磁盘上
        try (FileOutputStream fos = new FileOutputStream(classFilePath)) {
            fos.write(getClassByteArray(classInfo));
        }
    }

    /**
     * 解析字节码数组，获取class定义
     *
     * @param neighbor 邻类，与生成的class同一目录
     * @param b 字节码数组
     * @return class定义
     * @throws IllegalAccessException 非法访问异常
     */
    public Class<?> defineClass(Class<?> neighbor, byte[] b) throws IllegalAccessException {
        DefineClassHelper.class.getModule().addReads(neighbor.getModule());
        MethodHandles.Lookup lookup = MethodHandles.lookup();
        MethodHandles.Lookup prvLookup = MethodHandles.privateLookupIn(neighbor, lookup);
        return prvLookup.defineClass(b);
    }

    /**
     * 生成class字节码数组
     *
     * @param classInfo 类信息
     * @return class字节码数组
     */
    protected abstract byte[] getClassByteArray(ClassInfo classInfo);

    /**
     * 注解处理
     *
     * @param cw 类构建器
     * @param annotationInfos 注解信息集合
     */
    protected void annotationHandle(ClassWriter cw, List<AnnotationInfo> annotationInfos) {
        if (!CollectionUtils.isEmpty(annotationInfos)) {
            // 注解
            AnnotationVisitor av;
            for (AnnotationInfo info : annotationInfos) {
                // 注解开始
                av = cw.visitAnnotation(info.getDesc(), info.getVisible());
                if (!CollectionUtils.isEmpty(info.getParams())) {
                    for (AnnotationInfo.Param param : info.getParams()) {
                        // 设置注解参数
                        if (param.getValue() instanceof String[]) {
                            av = av.visitArray(param.getName());
                            for (String v : (String[]) param.getValue()) {
                                av.visit(null, v);
                            }
                        } else {
                            av.visit(param.getName(), param.getValue());
                        }
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
    protected void fieldHandle(ClassWriter cw, List<FieldInfo> fieldInfos, String className) {
        if (!CollectionUtils.isEmpty(fieldInfos)) {
            String fieldName;
            String typeOf;
            FieldVisitor fv;
            AnnotationVisitor av;
            for (FieldInfo info : fieldInfos) {
                fieldName = info.getName();
                typeOf = Type.getType(info.getFieldClass()).getDescriptor();
                // 字段
                fv = cw.visitField(accessOf(info.getAccess()), fieldName, typeOf, null, 0);
                // 判断字段是否有注解
                if (!CollectionUtils.isEmpty(info.getAnnotationInfos())) {
                    for (AnnotationInfo annotation : info.getAnnotationInfos()) {
                        // 字段注解开始
                        av = fv.visitAnnotation(annotation.getDesc(), annotation.getVisible());
                        if (!CollectionUtils.isEmpty(annotation.getParams())) {
                            for (AnnotationInfo.Param param : annotation.getParams()) {
                                // 设置字段注解参数
                                av.visit(param.getName(), param.getValue());
                            }
                        }
                        // 字段注解结束
                        av.visitEnd();
                    }
                }
                fv.visitEnd();
                // set方法
                setMethodHandle(cw, fieldName, typeOf, className);
                // get方法
                getMethodHandle(cw, fieldName, typeOf, className);
            }
        }
    }

    protected void initMethodHandle(ClassWriter cw) {
        // 构造函数
        MethodVisitor mv = cw.visitMethod(ACC_PUBLIC, "<init>", "()V", null, null);
        // 访问局部变量指令。0表示this
        mv.visitVarInsn(ALOAD, 0);
        // 访问方法指令
        mv.visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>", "()V");
        mv.visitInsn(RETURN);
        mv.visitMaxs(1, 1);
        mv.visitEnd();
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
        String getMethodName = "get";
        // 布尔类型的get方法名是is开头
        if (TYPEOF_BOOLEAN.equals(typeOf)) {
            getMethodName = "is";
        }
        getMethodName = getMethodName + StringUtils.capitalize(fieldName);
        MethodVisitor mv = cw.visitMethod(ACC_PUBLIC, getMethodName, "()" + typeOf, null, null);
        mv.visitCode();
        mv.visitVarInsn(ALOAD, 0);
        mv.visitFieldInsn(GETFIELD, className, fieldName, typeOf);
        mv.visitInsn(loadAndReturnOf(typeOf)[1]);
        mv.visitMaxs(2, 1);
        mv.visitEnd();
    }

    /**
     * toString方法处理
     *
     * @param cw 类构建器
     * @param fieldInfos 字段信息集合
     * @param className 完整类名
     */
    protected void toStringMethodHandle(ClassWriter cw, List<FieldInfo> fieldInfos, String className) {
        if (!CollectionUtils.isEmpty(fieldInfos)) {
            MethodVisitor mv = cw.visitMethod(ACC_PUBLIC, "toString", "()Ljava/lang/String;", null, null);
            // 添加Override注解
            AnnotationVisitor av = mv.visitAnnotation("Ljava/lang/Override;", true);
            av.visitEnd();
            // 开始扫描该方法
            mv.visitCode();
            String fieldName;
            String typeOf;
            // 塞入字符串
            mv.visitLdcInsn(className + "{");
            Iterator<FieldInfo> iterable = fieldInfos.iterator();
            FieldInfo field;
            // 每个字段结尾字符串临时变量
            String endTemp;
            while (iterable.hasNext()) {
                field = iterable.next();
                fieldName = field.getName();
                typeOf = Type.getType(field.getFieldClass()).getDescriptor();
                endTemp = "";
                if (typeOf.startsWith("L")) {
                    mv.visitLdcInsn(fieldName + "='");
                } else {
                    mv.visitLdcInsn(fieldName + "=");
                }
                mv.visitInsn(IADD);
                mv.visitMaxs(2, 3);
                // 访问局部变量指令。0表示this
                mv.visitVarInsn(ALOAD, 0);
                // 访问方法指令
                mv.visitFieldInsn(GETFIELD, className, fieldName, typeOf);
                mv.visitInsn(IADD);
                mv.visitMaxs(2, 3);
                // 拼接每个字段结尾
                if (typeOf.startsWith("L")) {
                    endTemp = "'";
                }
                if (iterable.hasNext()) {
                    endTemp += ", ";
                } else {
                    endTemp += "}";
                }
                mv.visitLdcInsn(endTemp);
                mv.visitInsn(IADD);
                mv.visitMaxs(2, 3);
            }
            // 设置返回
            mv.visitInsn(ARETURN);
            mv.visitMaxs(2, 1);
            mv.visitEnd();
        }
    }

    /**
     * 根据类型字符串获取对应类型的load或return标识
     *
     * @param typeof 类型字符串
     * @return load或return标识
     */
    protected static int[] loadAndReturnOf(String typeof) {
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
    protected static int accessOf(String access) {
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
            case "interface":
                return ACC_PUBLIC + ACC_ABSTRACT + ACC_INTERFACE;
            case "public":
            default:
                return ACC_PUBLIC;
        }
    }

}
