package org.thirteen.authorization.model.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.thirteen.authorization.model.po.base.BaseRecordPO;

import javax.persistence.Column;
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
@Table(name = "`sys_dict`")
public class SysDictPO extends BaseRecordPO<String> {

    private static final long serialVersionUID = 1L;
    /**
     * 字典名称
     */
    @Column(name = "`name`")
    private String name;
    /**
     * 0：禁用；1启用
     */
    @Column(name = "`is_active`")
    private String isActive;
    /**
     * 业务类型ID
     */
    @Column(name = "`biz_type_id`")
    private String bizTypeId;
    /**
     * 0：正常；1：删除
     */
    @Column(name = "`del_flag`")
    private String delFlag;

}