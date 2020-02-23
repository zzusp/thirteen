package org.thirteen.authorization.constant;

/**
 * @author Aaron.Sun
 * @description 全局变量定义
 * @date Created in 16:57 2020/2/22
 * @modified by
 */
public class GlobalConstants {

    /** 保存用户信息到SESSION */
    public static final String SESSION_KEY = "shiro_session";
    public static final String CURRENT_USER = "current_user";
    public static final String REMEBER_ME = "true";
    /** 默认密码 */
    public static final String DEFAULT_PASSWORD = "88888888";
    /** 默认盐的长度 */
    public static final Integer SALT_LENGTH = 16;
    /** cookie的key */
    public static final String COOKIE_KEY = "RENTALJSESSIONID";
    /** 超管角色Code */
    public static final String ADMIN_CODE = "admin";
    /** 启用标记 0：禁用；1启用 */
    public static final String ACTIVE_OFF = "0";
    public static final String ACTIVE_ON = "1";
    /**
     * 权限类型（0：需登录；1：需认证；2：需授权）
     * 注：授权需登陆
     */
    public static final String PERMISSION_LOGIN = "0";
    public static final String PERMISSION_AUTHOR = "1";
    public static final String PERMISSION_PERMS = "2";


}
