package org.thirteen.datamation.core.generate.po;

import org.springframework.util.StringUtils;
import org.thirteen.datamation.core.generate.AbstractClassConverter;
import org.thirteen.datamation.core.generate.AnnotationInfo;
import org.thirteen.datamation.core.generate.ClassInfo;
import org.thirteen.datamation.core.generate.FieldInfo;
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
public class PoConverter extends AbstractClassConverter {

    @Override
    protected ClassInfo tableToClass(DmTablePO table) {
        ClassInfo classInfo = new ClassInfo();
        classInfo.setClassName(lineToHumpAndStartWithCapitalize(table.getCode()) + PO_SUFFIX);
        classInfo.setAccess("public");
        classInfo.setInterfaces(new String[]{"java/io/Serializable"});
        return classInfo;
    }

    @Override
    protected List<AnnotationInfo> tableToAnnotation(DmTablePO table) {
        List<AnnotationInfo> annotationInfos = new ArrayList<>();
        // entity注解
        AnnotationInfo entityAnno = new AnnotationInfo();
        entityAnno.setDesc("javax.persistence.Entity");
        // table注解
        AnnotationInfo tableAnno = new AnnotationInfo();
        tableAnno.setDesc("javax.persistence.Table");
        tableAnno.add("name", table.getCode());
        // hibernate的table注解，生成table时可添加上表描述
        AnnotationInfo hibernateTableAnno = new AnnotationInfo();
        hibernateTableAnno.setDesc("org.hibernate.annotations.Table");
        hibernateTableAnno.add("appliesTo", table.getCode());
        hibernateTableAnno.add("comment", table.getName());
        // 添加到集合中
        annotationInfos.add(entityAnno);
        annotationInfos.add(tableAnno);
        annotationInfos.add(hibernateTableAnno);
        return annotationInfos;
    }

    @Override
    protected List<AnnotationInfo> columnToAnnotation(DmColumnPO column) {
        List<AnnotationInfo> annotationInfos = new ArrayList<>();
        // column注解
        AnnotationInfo columnAnno = new AnnotationInfo();
        columnAnno.setDesc("javax.persistence.Column");
        columnAnno.add("name", column.getCode());
        // 拼接columnDefinition
        StringBuilder sb = new StringBuilder();
        sb.append(column.getDbType());
        if (column.getLength() != null && column.getLength() > 0) {
            sb.append("(").append(column.getLength()).append(")");
        }
        if (column.getNotNull() != null && column.getNotNull() == 1) {
            sb.append(" NOT NULL ");
        }
        sb.append(" COMMENT '").append(column.getName());
        if (StringUtils.isEmpty(column.getRemark())) {
            sb.append(" ").append(column.getRemark());
        }
        sb.append("'");
        // 添加到集合中
        columnAnno.add("columnDefinition", sb.toString());
        annotationInfos.add(columnAnno);
        return annotationInfos;
    }

    @Override
    protected FieldInfo columnToField(DmColumnPO column) {
        FieldInfo fieldInfo = new FieldInfo();
        fieldInfo.setName(lineToHump(column.getCode()));
        fieldInfo.setAccess("private");
        return fieldInfo;
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
//        PoConverter poConverter = new PoConverter();
//        PoGenerator poGenerate = new PoGenerator();
//        try {
//            poGenerate.writeClass(poConverter.getClassInfo(po));
//        } catch (IOException | URISyntaxException e) {
//            e.printStackTrace();
//        }
    }
}
