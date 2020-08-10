package org.thirteen.datamation.generate.po;

import org.thirteen.datamation.generate.AbstractClassConver;
import org.thirteen.datamation.generate.AnnotationInfo;
import org.thirteen.datamation.generate.ClassInfo;
import org.thirteen.datamation.generate.FieldInfo;
import org.thirteen.datamation.model.po.DmColumnPO;
import org.thirteen.datamation.model.po.DmTablePO;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PoConver extends AbstractClassConver {

    @Override
    protected ClassInfo tableToClass(DmTablePO table) {
        ClassInfo classInfo = new ClassInfo();
        classInfo.setClassName(lineToHumpAndStartWithCapitalize(table.getCode()));
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
        StringBuilder sb = new StringBuilder();
        sb.append(column.getDbType());
        if (column.getLength() != null && column.getLength() > 0) {
            sb.append("(").append(column.getLength()).append(")");
        }
        if (column.getNotNull() != null && column.getNotNull() == 1) {
            sb.append(" NOT NULL ");
        }
        sb.append(" COMMENT '").append(column.getName()).append(" ").append(column.getRemark()).append("'");
        columnAnno.add("columnDefinition", sb.toString());
        annotationInfos.add(columnAnno);
        return annotationInfos;
    }

    @Override
    protected FieldInfo columnToField(DmColumnPO column) {
        FieldInfo fieldInfo = new FieldInfo();
        fieldInfo.setName(lineToHump(column.getCode()));
        fieldInfo.setFieldClass(column.getJavaType().replaceAll("\\.", "\\/"));
        fieldInfo.setAccess("private");
        return fieldInfo;
    }

    public static void main(String[] args) {
        DmTablePO po = new DmTablePO();
        po.setCode("rental_stock");
        po.setName("库存");
        po.setStatus((byte) 1);
        po.setCreateBy("admin");
        po.setCreateTime(LocalDateTime.now());
        po.setDelFlag((byte) 1);
        DmColumnPO column = new DmColumnPO();
        column.setCode("id");
        column.setName("主键");
        column.setJavaType("Ljava/lang/String;");
        column.setDbType("VARCHAR");
        column.setLength(32);
        column.setNotNull((byte) 1);
        po.setStatus((byte) 1);
        column.setCreateBy("admin");
        column.setCreateTime(LocalDateTime.now());
        column.setRemark("主键唯一");
        column.setDelFlag((byte) 1);
        po.setColumns(new ArrayList<>());
        po.getColumns().add(column);
        PoConver poConver = new PoConver();
        PoGenerate poGenerate = new PoGenerate();
        try {
            poGenerate.writeClass(poConver.getClassInfo(po));
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }
}
