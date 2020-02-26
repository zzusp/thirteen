package org.thirteen.authorization.model.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.thirteen.authorization.model.po.base.BaseRecordPO;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author Aaron.Sun
 * @description 数据字典实体类
 * @date Created in 11:02 2018/9/14
 * @modified by
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "sys_dict")
@org.hibernate.annotations.Table(appliesTo = "sys_dict", comment = "数据字典信息表")
public class SysDictPO extends BaseRecordPO {

    private static final long serialVersionUID = 1L;
    @Column(name = "biz_type_code", columnDefinition = "CHAR(20) COMMENT '业务类型编码'")
    private String bizTypeCode;

}