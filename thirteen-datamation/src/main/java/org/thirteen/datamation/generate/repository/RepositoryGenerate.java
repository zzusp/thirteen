package org.thirteen.datamation.generate.repository;

import org.objectweb.asm.ClassWriter;
import org.thirteen.datamation.generate.AbstractClassGenerate;
import org.thirteen.datamation.generate.ClassInfo;

/**
 * @author Aaron.Sun
 * @description 生成repository类
 * @date Created in 16:19 2020/8/10
 * @modified By
 */
public class RepositoryGenerate extends AbstractClassGenerate {

    public RepositoryGenerate() {
        super(RepositoryGenerate.class);
    }

    /**
     * 生成 repository class字节码数组
     *
     * @param classInfo 类信息
     * @return class字节码数组
     */
    @Override
    protected byte[] getClassByteArray(ClassInfo classInfo) {
        ClassWriter cw = new ClassWriter(0);
        // 设置类基本属性
        // 参数：版本号，类的访问标志，类名（包含路径），签名，父类，内部接口
        cw.visit(52, accessOf(classInfo.getAccess()), this.defaultPackage + classInfo.getClassName(), null,
            classInfo.getSuperName(), classInfo.getInterfaces());
        // 注解处理
        annotationHandle(cw, classInfo.getAnnotationInfos());
        // cw结束
        cw.visitEnd();
        // 转为字节数组
        return cw.toByteArray();
    }
}
