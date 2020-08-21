package org.thirteen.datamation.core.generate.repository;

import aj.org.objectweb.asm.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.thirteen.datamation.core.exception.DatamationException;
import org.thirteen.datamation.core.generate.AbstractClassConverter;
import org.thirteen.datamation.core.generate.AnnotationInfo;
import org.thirteen.datamation.core.generate.ClassInfo;
import org.thirteen.datamation.core.generate.FieldInfo;
import org.thirteen.datamation.core.generate.po.PoConverter;
import org.thirteen.datamation.core.generate.po.PoGenerator;
import org.thirteen.datamation.model.po.DmColumnPO;
import org.thirteen.datamation.model.po.DmTablePO;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Aaron.Sun
 * @description 数据库配置信息转为生成类所需要的信息
 * @date Created in 15:54 2020/8/11
 * @modified By
 */
public class RepositoryConverter extends AbstractClassConverter {

    private static final Logger logger = LoggerFactory.getLogger(RepositoryGenerator.class);

    /** po类完整路径 */
    private final String poClassName;
    /** 主键的java类型 */
    private final String pkJavaType;

    public RepositoryConverter(Class<?> poClass) {
        this(Type.getInternalName(poClass), "Ljava/lang/String;");
    }

    public RepositoryConverter(String poClassName, String pkJavaType) {
        // po是否存在校验。未找到PO类时，应中断运行，所以抛出中断异常
        try {
            RepositoryGenerator.class.getClassLoader().loadClass(poClassName.replaceAll("/", "."));
        } catch (ClassNotFoundException e) {
            logger.error("po class not found: {}", poClassName);
            throw new DatamationException("po class not found: " + poClassName, e);
        }
        this.poClassName = poClassName;
        this.pkJavaType = pkJavaType;
    }

    @Override
    protected ClassInfo tableToClass(DmTablePO table) {
        // 接口
        String interfaceStr = Type.getInternalName(BaseRepository.class);
        // 签名（泛型。Ljava/lang/Object;表示超类或超接口）
        String signature = "Ljava/lang/Object;L" + interfaceStr + "<L" + poClassName + ";" + pkJavaType + ">;";

        ClassInfo classInfo = new ClassInfo();
        classInfo.setClassName(lineToHumpAndStartWithCapitalize(table.getCode()) + REPOSITORY_SUFFIX);
        classInfo.setAccess("interface");
        classInfo.setSignature(signature);
        classInfo.setInterfaces(new String[]{interfaceStr});
        return classInfo;
    }

    @Override
    protected List<AnnotationInfo> tableToAnnotation(DmTablePO table) {
        List<AnnotationInfo> annotationInfos = new ArrayList<>();
        // repository注解
        AnnotationInfo repositoryAnno = new AnnotationInfo();
        repositoryAnno.setDesc("org.springframework.stereotype.Repository");
        annotationInfos.add(repositoryAnno);
        return annotationInfos;
    }

    @Override
    protected List<AnnotationInfo> columnToAnnotation(DmColumnPO column) {
        return null;
    }

    @Override
    protected FieldInfo columnToField(DmColumnPO column) {
        return null;
    }

    public static void main(String[] args) {
//        DmTablePO po = new DmTablePO();
//        po.setCode("rental_stock");
//        po.setName("库存");
//        po.setStatus((byte) 1);
//        po.setCreateBy("admin");
//        po.setCreateTime(LocalDateTime.now());
//        po.setDelFlag((byte) 1);
//
//        DmColumnPO id = new DmColumnPO();
//        id.setCode("id");
//        id.setName("主键");
//        id.setDbType("VARCHAR");
//        id.setLength(32);
//        id.setNotNull((byte) 1);
//        id.setStatus((byte) 1);
//        id.setCreateBy("admin");
//        id.setCreateTime(LocalDateTime.now());
//        id.setRemark(null);
//        id.setDelFlag((byte) 1);
//
//        DmColumnPO code = new DmColumnPO();
//        code.setCode("code");
//        code.setName("编码");
//        code.setDbType("VARCHAR");
//        code.setLength(20);
//        code.setNotNull((byte) 1);
//        code.setStatus((byte) 1);
//        code.setCreateBy("admin");
//        code.setCreateTime(LocalDateTime.now());
//        code.setRemark("编码唯一");
//        code.setDelFlag((byte) 1);
//
//        DmColumnPO createTime = new DmColumnPO();
//        createTime.setCode("create_time");
//        createTime.setName("创建时间");
//        createTime.setDbType("DATETIME");
//        createTime.setNotNull((byte) 0);
//        createTime.setStatus((byte) 1);
//        createTime.setCreateBy("admin");
//        createTime.setCreateTime(LocalDateTime.now());
//        createTime.setDelFlag((byte) 1);
//
//        po.getColumns().add(id);
//        po.getColumns().add(code);
//        po.getColumns().add(createTime);
//
//        PoConverter poConverter = new PoConverter();
//        PoGenerator poGenerate = new PoGenerator();
//
//        RepositoryConverter repositoryConverter;
//        RepositoryGenerator repositoryGenerate = new RepositoryGenerator();
//        try {
//            Class<?> poClass = poGenerate.generate(poConverter.getClassInfo(po));
//
//            repositoryConverter = new RepositoryConverter(Type.getInternalName(poClass), "Ljava/lang/String;");
//
//            repositoryGenerate.writeClass(repositoryConverter.getClassInfo(po));
//        } catch (IOException | URISyntaxException | InterruptedException | IllegalAccessException e) {
//            e.printStackTrace();
//        }
    }
}
