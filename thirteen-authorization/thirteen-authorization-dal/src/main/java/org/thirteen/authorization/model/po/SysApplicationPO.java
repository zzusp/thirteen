package org.thirteen.authorization.model.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.thirteen.authorization.model.po.base.BaseTreeSortPO;

import javax.persistence.Column;
import javax.persistence.Entity;
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
@Entity
@Table(name = "sys_application")
@org.hibernate.annotations.Table(appliesTo = "sys_application", comment = "应用信息表")
public class SysApplicationPO extends BaseTreeSortPO {

    private static final long serialVersionUID = 1L;
    @Column(name = "icon", columnDefinition = "VARCHAR(50) COMMENT '图标'")
    private String icon;
    @Column(name = "url", columnDefinition = "VARCHAR(255) COMMENT '路径'")
    private String url;
    @Column(name = "type", columnDefinition = "CHAR(1) COMMENT '应用类型 " +
        "0：微服务应用；1：应用接口；2：应用菜单；3：应用菜单组'")
    private String type;

}