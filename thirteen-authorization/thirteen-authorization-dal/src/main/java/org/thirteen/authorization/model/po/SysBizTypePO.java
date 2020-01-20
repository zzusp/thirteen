package org.thirteen.authorization.model.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.thirteen.authorization.model.po.base.BaseRecordPO;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author Aaron.Sun
 * @description 业务类型实体类
 * @date Created in 11:02 2018/9/14
 * @modified by
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@Entity
@Table(name = "sys_biz_type")
@org.hibernate.annotations.Table(appliesTo = "sys_biz_type", comment = "业务类型信息表")
public class SysBizTypePO extends BaseRecordPO {

    private static final long serialVersionUID = 1L;

}