package org.thirteen.authorization.model.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.thirteen.authorization.model.po.base.BaseDeletePO;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;

/**
 * @author Aaron.Sun
 * @description 登录日志实体类
 * @date Created in 11:02 2018/9/14
 * @modified by
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "sys_log_login")
@org.hibernate.annotations.Table(appliesTo = "sys_log_login", comment = "登录日志信息表")
public class SysLogLoginPO extends BaseDeletePO {

    private static final long serialVersionUID = 1L;
    @Column(name = "request_path", columnDefinition = "VARCHAR(500) COMMENT '请求地址'")
    private String requestPath;
    @Column(name = "login_time", columnDefinition = "DATETIME COMMENT '登陆时间'")
    private LocalDateTime loginTime;
    @Column(name = "account", columnDefinition = "VARCHAR(20) COMMENT '账号'")
    private String account;
    @Column(name = "status", columnDefinition = "INT COMMENT '状态码'")
    private Integer status;
    @Column(name = "message", columnDefinition = "VARCHAR(1000) COMMENT '信息'")
    private String message;
    @Column(name = "country", columnDefinition = "VARCHAR(50) COMMENT '国家'")
    private String country;
    @Column(name = "province", columnDefinition = "VARCHAR(50) COMMENT '省份'")
    private String province;
    @Column(name = "city", columnDefinition = "VARCHAR(50) COMMENT '城市'")
    private String city;

}