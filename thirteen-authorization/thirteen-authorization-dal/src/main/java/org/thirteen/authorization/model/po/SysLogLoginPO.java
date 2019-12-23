package org.thirteen.authorization.model.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.thirteen.authorization.model.po.base.BasePO;

import javax.persistence.Column;
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
@Table(name = "`sys_log_login`")
public class SysLogLoginPO extends BasePO<String> {

    private static final long serialVersionUID = 1L;
    /**
     * 请求地址
     */
    @Column(name = "`request_path`")
    private String requestPath;
    /**
     * 登陆时间
     */
    @Column(name = "`login_time`")
    private LocalDateTime loginTime;
    /**
     * 账号
     */
    @Column(name = "`account`")
    private String account;
    /**
     * 状态码
     */
    @Column(name = "`status`")
    private Integer status;
    /**
     * 信息
     */
    @Column(name = "`message`")
    private String message;
    /**
     * 国家
     */
    @Column(name = "`country`")
    private String country;
    /**
     * 省份
     */
    @Column(name = "`province`")
    private String province;
    /**
     * 城市
     */
    @Column(name = "`city`")
    private String city;
    /**
     * 0：正常；1：删除
     */
    @Column(name = "`del_flag`")
    private String delFlag;

}