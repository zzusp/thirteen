package org.thirteen.authorization.model.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.thirteen.authorization.model.po.base.BaseTreeSortPO;

import javax.persistence.Column;
import javax.persistence.Table;

/**
 * @author Aaron.Sun
 * @description 应用实体类
 * @date Created in 11:02 2018/9/14
 * @modified by
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "`sys_application`")
public class SysApplicationPO extends BaseTreeSortPO<String> {

    private static final long serialVersionUID = 1L;
    /**
     * 图标
     */
    @Column(name = "`icon`")
    private String icon;
    /**
     * 路径
     */
    @Column(name = "`url`")
    private String url;
    /**
     * 0：微服务应用；1：应用接口；2：应用菜单；3：应用菜单组
     */
    @Column(name = "`type`")
    private String type;
    /**
     * 0：禁用；1启用
     */
    @Column(name = "`is_active`")
    private String isActive;
    /**
     * 0：正常；1：删除
     */
    @Column(name = "`del_flag`")
    private String delFlag;

}