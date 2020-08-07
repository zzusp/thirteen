package org.thirteen.datamation.generate;

import javassist.util.proxy.DefineClassHelper;
import org.objectweb.asm.*;
import org.springframework.util.StringUtils;
import org.thirteen.datamation.model.po.DmColumnPO;
import org.thirteen.datamation.model.po.DmTablePO;

import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.PrivilegedExceptionAction;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.List;

public class ClassGenerate extends ClassLoader implements Opcodes {

    private static final ProtectionDomain PROTECTION_DOMAIN;

    /**
     * 生成class
     *
     * @param neighbor 邻类，与生成的class同一目录
     * @param classInfo 类信息
     * @throws IllegalAccessException 非法访问异常
     */
    public static Class<?> generate(ClassInfo classInfo)
        throws IllegalAccessException {
        return defineClass(this.getClass(), getClassByteArray(classInfo));
    }

    /**
     * 生成class
     *
     * @param neighbor 邻类，与生成的class同一目录
     * @param classInfo 类信息
     * @throws IllegalAccessException 非法访问异常
     */
    public static Class<?> generate(Class<?> neighbor, ClassInfo classInfo)
        throws IllegalAccessException {
        return defineClass(neighbor, getClassByteArray(classInfo));
    }

    /**
     * 生成class并写入class文件
     *
     * @param classInfo 类信息
     * @throws IOException IO异常
     * @throws URISyntaxException 路径句法异常
     */
    public static void writeClass(ClassInfo classInfo) throws IOException, URISyntaxException {
        //将二进制流写到本地磁盘上
        FileOutputStream fos = new FileOutputStream(ClassLoader.getSystemResource("").toURI().getPath()
            + classInfo.getFullName() + ".class");
        fos.write(getClassByteArray(classInfo));
        fos.close();
    }

    /**
     * 解析字节码数组，获取class定义
     *
     * @param neighbor 邻类，与生成的class同一目录
     * @param b 字节码数组
     * @return class定义
     * @throws IllegalAccessException 非法访问异常
     */
    public static Class<?> defineClass(Class<?> neighbor, byte[] b)
        throws IllegalAccessException {
        DefineClassHelper.class.getModule().addReads(neighbor.getModule());
        MethodHandles.Lookup lookup = MethodHandles.lookup();
        MethodHandles.Lookup prvlookup = MethodHandles.privateLookupIn(neighbor, lookup);
        return prvlookup.defineClass(b);
    }

    /**
     * 生成class字节码数组
     *
     * @param classInfo 类信息
     * @return class字节码数组
     */
    private static byte[] getClassByteArray(ClassInfo classInfo) {
        ClassWriter cw = new ClassWriter(0);
        // 设置类基本属性
        // 参数：版本号，类的访问标志，类名（包含路径），签名，父类，内部接口
        cw.visit(52, accessOf(classInfo.getAccess()), classInfo.getFullName(), null,
            classInfo.getSuperName(), new String[] { "java/io/Serializable" });
        // 注解处理
        annotationHandle(cw, classInfo.getAnnotationInfos());

        initMethodHandle(cw);
        // 字段处理（包含get/set方法）
        fieldHandle(cw, classInfo.getFieldInfos(), classInfo.getFullName());
        // cw结束
        cw.visitEnd();
        // 转为字节数组
        return cw.toByteArray();
    }


    /**
     * 注解处理
     *
     * @param cw 类构建器
     * @param annotationInfos 注解信息集合
     */
    private static void annotationHandle(ClassWriter cw, List<AnnotationInfo> annotationInfos) {
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
    private static void fieldHandle(ClassWriter cw, List<FieldInfo> fieldInfos, String className) {
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

    private static void initMethodHandle(ClassWriter cw) {
        // 构造函数
        MethodVisitor mv = cw.visitMethod(ACC_PUBLIC, "<init>", "()V", null, null);
        mv.visitVarInsn(ALOAD, 0);
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
    private static void setMethodHandle(ClassWriter cw, String fieldName, String typeOf, String className) {
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
    private static void getMethodHandle(ClassWriter cw, String fieldName, String typeOf, String className) {
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

    /**
     * 根据类获取保护域
     *
     * @param source 类
     * @return 保护域
     */
    public static ProtectionDomain getProtectionDomain(final Class<?> source) {
        return source == null ? null : (ProtectionDomain)AccessController
            .doPrivileged((PrivilegedAction<?>) source::getProtectionDomain);
    }

    static {
        ProtectionDomain protectionDomain;
        Method defineClassUnsafe;
        Object unsafe;
        try {
            protectionDomain = getProtectionDomain(ClassGenerate.class);
            unsafe = AccessController.doPrivileged((PrivilegedExceptionAction<?>) () -> {
                Class<?> u = Class.forName("sun.misc.Unsafe");
                Field theUnsafe = u.getDeclaredField("theUnsafe");
                theUnsafe.setAccessible(true);
                return theUnsafe.get(null);
            });
            Class<?> u = Class.forName("java.lang.invoke.MethodHandles");
            defineClassUnsafe = u.getMethod("defineClass", byte[].class);
        } catch (Throwable var8) {
            protectionDomain = null;
            defineClassUnsafe = null;
            unsafe = null;
        }
        PROTECTION_DOMAIN = protectionDomain;
        DEFINE_CLASS_UNSAFE = defineClassUnsafe;
        UNSAFE = unsafe;
    }

    public static void main(String[] args) throws Exception {
        ClassInfo classInfo = new ClassInfo();
        classInfo.setPackagePath("org/thirteen/datamation/model/po");
        classInfo.setClassName("DmTestPO");
        classInfo.setAccess("public");
        classInfo.setInterfaces(new String[] {"java/io/Serializable"});

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

        ClassGenerate.writeClass(classInfo);
        Class<?> c = ClassGenerate.generate(classInfo, ClassLoader.class.getClassLoader());
        System.out.println(c.getMethod("getId").invoke(c.getDeclaredConstructor().newInstance()));
    }


}
