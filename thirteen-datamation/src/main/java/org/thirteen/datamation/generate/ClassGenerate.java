package org.thirteen.datamation.generate;

import org.objectweb.asm.*;

import java.io.FileOutputStream;

public class ClassGenerate extends ClassLoader implements Opcodes {

    public static void main(String[] args) throws Exception {
        ClassWriter cw = new ClassWriter(0);
        //类名
        cw.visit(V1_7, ACC_PUBLIC, "org/thirteen/datamation/model/po/_766ComLeakInfo", null, "java/lang/Object", null);
        //注释
        AnnotationVisitor av = cw.visitAnnotation("Lorg/springframework/data/mongodb/core/mapping/Document;", true);
        //注释参数
        av.visit("collection", "uc_members");
        av.visitEnd();
        //构造函数
        MethodVisitor mv = cw.visitMethod(ACC_PUBLIC, "<init>", "()V", null, null);
        mv.visitVarInsn(ALOAD, 0);
        mv.visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>", "()V");
        mv.visitInsn(RETURN);
        mv.visitMaxs(1, 1);
        mv.visitEnd();


        //字段
        FieldVisitor fv = cw.visitField(ACC_PUBLIC, "description", "Ljava/lang/String;", null, null);
        fv.visitEnd();

//        for (FieldInfo f : entity.getFields()) {
//            String fieldName = f.getName();
//            String typeOf = Type.getType(f.getClazzType()).getDescriptor();
//            cw.visitField(ACC_PRIVATE, fieldName, typeOf, null, 0).visitEnd();
//            // getMethod
//            String getMethodName = "get" + StringUtils.capitalize(fieldName);
//            mv = cw.visitMethod(ACC_PUBLIC, getMethodName, "()" + typeOf, null, null);
//            mv.visitCode();
//            mv.visitVarInsn(ALOAD, 0);
//            mv.visitFieldInsn(GETFIELD, className, fieldName, typeOf);
//            mv.visitInsn(loadAndReturnOf(typeOf)[1]);
//            mv.visitMaxs(2, 1);
//            mv.visitEnd();
//            String setMethodName = "set" + StringUtils.capitalize(fieldName);
//            // setMethod
//            mv = cw.visitMethod(ACC_PUBLIC, setMethodName, "(" + typeOf + ")V", null, null);
//            mv.visitCode();
//            mv.visitVarInsn(ALOAD, 0);
//            mv.visitVarInsn(loadAndReturnOf(typeOf)[0], 1);
//            mv.visitFieldInsn(PUTFIELD, className, fieldName, typeOf);
//            mv.visitInsn(RETURN);
//            mv.visitMaxs(3, 3);
//            mv.visitEnd();
//        }

        byte[] code = cw.toByteArray();

        //将二进制流写到本地磁盘上
        FileOutputStream fos = new FileOutputStream(ClassLoader.getSystemResource("").toURI().getPath() + "/org/thirteen/datamation/model/po/_766ComLeakInfo.class");
        fos.write(code);
        fos.close();

        ClassGenerate loader = new ClassGenerate();
        Class<?> clazz = loader.defineClass(null, code, 0, code.length);
        Object beanObj = clazz.getConstructor().newInstance();

        clazz.getField("description").set(beanObj, "Adobe客户信息泄露!");

        String nameString = (String) clazz.getField("description").get(beanObj);
        System.out.println("filed value : " + nameString);
    }

    private static int[] loadAndReturnOf(String typeof) {
        if (typeof.equals("I") || typeof.equals("Z")) {
            return new int[]{ILOAD, IRETURN};
        } else if (typeof.equals("J")) {
            return new int[]{LLOAD, LRETURN};
        } else if (typeof.equals("D")) {
            return new int[]{DLOAD, DRETURN};
        } else if (typeof.equals("F")) {
            return new int[]{FLOAD, FRETURN};
        } else {
            return new int[]{ALOAD, ARETURN};
        }
    }
}
