package org.thirteen.datamation.model.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author Aaron.Sun
 * @description 数据化表间关联关系信息
 * @date Created in 14:17 2020/9/27
 * @modified by
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "dm_relation")
@org.hibernate.annotations.Table(appliesTo = "dm_relation", comment = "数据化表间关联关系信息")
public class DmRelationPO implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GenericGenerator(name = "pk_uuid", strategy = "uuid")
    @GeneratedValue(generator = "pk_uuid")
    @Column(name = "id", unique = true, columnDefinition = "CHAR(32) NOT NULL COMMENT '实体主键（唯一标识）'")
    private String id;
    @Column(name = "source_table_code", columnDefinition = "CHAR(20) NOT NULL COMMENT '来源表'")
    private String sourceTableCode;
    @Column(name = "source_column_code", columnDefinition = "CHAR(20) NOT NULL COMMENT '来源字段'")
    private String sourceColumnCode;
    @Column(name = "target_table_code", columnDefinition = "CHAR(20) NOT NULL COMMENT '目标表'")
    private String targetTableCode;
    @Column(name = "target_column_code", columnDefinition = "CHAR(20) NOT NULL COMMENT '目标字段'")
    private String targetColumnCode;
    @Column(name = "relation_type", columnDefinition = "TINYINT(1) COMMENT '关联类型 0：一对一；1：一对多'")
    private Byte relationType;

}
