package org.thirteen.datamation.core.generate.internal;

import org.hibernate.jpa.boot.internal.EntityManagerFactoryBuilderImpl;
import org.objectweb.asm.*;
import org.thirteen.datamation.core.generate.AbstractClassGenerator;
import org.thirteen.datamation.core.generate.ClassInfo;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Aaron.Sun
 * @description 动态修改EntityManagerFactoryBuilderImpl类中的私有构造方法，在字节码层面添加自定义代码，实现hibernate可以加载到asm动态生成的class
 * @date Created in 14:58 2020/9/16
 * @modified By
 */
public class EntityManagerFactoryBuilderImplGenerator extends AbstractClassGenerator {

    public EntityManagerFactoryBuilderImplGenerator() {
        super(EntityManagerFactoryBuilderImplGenerator.class);
    }

    @Override
    protected byte[] getClassByteArray(ClassInfo classInfo) {
        return new byte[0];
    }

    @Override
    protected byte[] getClassByteArray(Set<String> mappers) {
        try {
            // 主要逻辑如下
            // 1. 通过ClassReader读取原class文件
            // 2. 通过自定义的ClassAdapter重写自定义方法
            //  2.1 修改EntityManagerFactoryBuilderImpl类中的私有构造函数
            //   2.1.1 将实体类完整路径，添加到metadataSources对象中（调用addAnnotatedClassName方法）
            // 3. 生成修改后的EntityManagerFactoryBuilderImpl class字节码
            ClassReader classReader = new ClassReader(EntityManagerFactoryBuilderImpl.class.getName());
            ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
            ClassAdapter ca = new ClassAdapter(cw, this.defaultPackage, mappers);
            classReader.accept(ca, ClassReader.EXPAND_FRAMES);

//            MethodVisitor mv = cw.visitMethod(ACC_STATIC, "<clinit>", "()V", null, null);
//            Label l0 = new Label();
//            mv.visitLineNumber(70, l0);
//            mv.visitLdcInsn("org.hibernate.jpa.boot.internal.EntityManagerFactoryBuilderImpl");
//            mv.visitMethodInsn(INVOKESTATIC, "org/hibernate/internal/HEMLogging", "messageLogger",
//                "(Ljava/lang/String;)Lorg/hibernate/internal/EntityManagerMessageLogger;", false);
//            mv.visitFieldInsn(PUTSTATIC, defaultPackage + "EntityManagerFactoryBuilderImpl", "LOG",
//                "Lorg/hibernate/internal/EntityManagerMessageLogger;");
//            mv.visitInsn(RETURN);
//            mv.visitMaxs(1, 0);
//            mv.visitEnd();
            return cw.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new byte[0];
    }

    static class ClassAdapter extends ClassVisitor implements Opcodes {
        private static final String TARGET_PACKAGE = "org/hibernate/jpa/boot/internal/";
        private static final String TARGET_CLASS_NAME = "EntityManagerFactoryBuilderImpl";
        private static final String TARGET_NAME = "<init>";
        private static final String TARGET_DESC = "("
            + "Lorg/hibernate/jpa/boot/spi/PersistenceUnitDescriptor;"
            + "Ljava/util/Map;"
            + "Ljava/lang/ClassLoader;"
            + "Lorg/hibernate/boot/registry/classloading/spi/ClassLoaderService;"
            + ")V";

        private static final String TARGET_FIELD_NAME = "LOG";
        private static final String TARGET_FIELD_DESC = "Lorg/hibernate/internal/EntityManagerMessageLogger;";

        private final String defaultPackage;
        private final Set<String> mappers;

        public ClassAdapter(ClassVisitor classVisitor, String defaultPackage, Set<String> mappers) {
            super(ASM8, classVisitor);
            this.defaultPackage = defaultPackage;
            this.mappers = mappers;
        }

        @Override
        public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
            if (name.contains(TARGET_PACKAGE) && name.contains(TARGET_CLASS_NAME)) {
                name = name.replace(TARGET_PACKAGE, defaultPackage);
            }
            super.visit(version, access, name, signature, superName, interfaces);
        }

        @Override
        public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
            MethodVisitor mv = super.visitMethod(access, name, descriptor, signature, exceptions);
            if (ACC_PRIVATE == access && TARGET_NAME.equals(name) && TARGET_DESC.equals(descriptor)) {
                return new MethodAdapter(mv, mappers);
            }
            if ("<clinit>".equals(name)) {
                return new StaticMethodAdapter(mv, defaultPackage + TARGET_CLASS_NAME);
            }
            return mv;
        }

        static class MethodAdapter extends MethodVisitor {
            private static final String TARGET_OWNER = "org/hibernate/boot/MetadataSources";
            private static final String TARGET_NAME = "<init>";
            private static final String TARGET_DESC = "(Lorg/hibernate/service/ServiceRegistry;)V";
            private static final String ADD_METHOD_NAME = "addAnnotatedClassName";
            private static final String ADD_METHOD_DESC = "(Ljava/lang/String;)Lorg/hibernate/boot/MetadataSources";

            private final Set<String> mappers;

            public MethodAdapter(MethodVisitor mv, Set<String> mappers) {
                super(ASM8, mv);
                this.mappers = mappers;
            }

            @Override
            public void visitMethodInsn(int opcode, String owner, String name, String desc, boolean isInterface) {
                super.visitMethodInsn(opcode, owner, name, desc, isInterface);
                if (TARGET_OWNER.equals(owner) && TARGET_NAME.equals(name) && TARGET_DESC.equals(desc)) {
                    this.visitVarInsn(ASTORE, 9);
                    addMapper(mappers);
                    this.visitVarInsn(ALOAD, 9);
                }
            }

            @Override
            public void visitMaxs(int maxStack, int maxLocals) {
                super.visitMaxs(maxStack, maxLocals);
            }

            private void addMapper(Set<String> mappers) {
                for (String mapper : mappers) {
                    this.visitVarInsn(ALOAD, 9);
                    this.visitLdcInsn(mapper);
                    this.visitMethodInsn(INVOKEVIRTUAL, TARGET_OWNER,
                        ADD_METHOD_NAME, ADD_METHOD_DESC, false);
                    this.visitInsn(POP);
                }
            }
        }

        static class StaticMethodAdapter extends MethodVisitor {
            private final String className;

            public StaticMethodAdapter(MethodVisitor methodVisitor, String className) {
                super(ASM8, methodVisitor);
                this.className = className;
            }

            @Override
            public void visitLdcInsn(Object value) {
                if (value instanceof Type) {
                    value = "org.thirteen.datamation.core.generate.internal.EntityManagerFactoryBuilderImpl";
                }
                super.visitLdcInsn(value);
            }

            @Override
            public void visitFieldInsn(int opcode, String owner, String name, String descriptor) {
                if (PUTSTATIC == opcode && "LOG".equals(name)
                    && "org/hibernate/jpa/boot/internal/EntityManagerFactoryBuilderImpl".equals(owner)) {
                    owner = "org/thirteen/datamation/core/generate/internal/EntityManagerFactoryBuilderImpl";
                }
                super.visitFieldInsn(opcode, owner, name, descriptor);
            }

            @Override
            public void visitMethodInsn(int opcode, String owner, String name, String descriptor, boolean isInterface) {
                if (INVOKESTATIC == opcode && "org/hibernate/internal/HEMLogging".equals(owner)) {
                    descriptor = "(Ljava/lang/String;)Lorg/hibernate/internal/EntityManagerMessageLogger;";
                }
                super.visitMethodInsn(opcode, owner, name, descriptor, isInterface);
            }

            @Override
            public void visitMaxs(int maxStack, int maxLocals) {
                super.visitMaxs(maxStack, maxLocals);
            }
        }
    }

    public static void main(String[] args) throws Exception {
        EntityManagerFactoryBuilderImplGenerator generator = new EntityManagerFactoryBuilderImplGenerator();
        Set<String> mappers = new HashSet<>();
        mappers.add("org.xxx.xxx");
        mappers.add("org.xxx.yyy");
        generator.writeClass("DmEntityManagerFactoryBuilderImpl", mappers);
    }
}
