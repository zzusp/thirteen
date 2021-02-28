package org.thirteen.datamation.auth.constant;

/**
 * @author Aaron.Sun
 * @description Datamation Auth常用code
 * @date Created in 17:37 2021/2/13
 * @modified By
 */
public class DmAuthCodes {

    private DmAuthCodes() {
    }

    // 表名

    /**
     * 用户表表名
     */
    public static final String AUTH_USER = "auth_user";
    /**
     * 角色表表名
     */
    public static final String AUTH_ROLE = "auth_role";
    /**
     * 权限表表名
     */
    public static final String AUTH_PERMISSION = "auth_permission";
    /**
     * 组织表表名
     */
    public static final String AUTH_GROUP = "auth_group";
    /**
     * 部门表表名
     */
    public static final String AUTH_DEPT = "auth_dept";
    /**
     * 应用表表名
     */
    public static final String AUTH_APP = "auth_app";
    /**
     * 用户角色关联表表名
     */
    public static final String AUTH_USER_ROLE = "auth_user_role";
    /**
     * 部门角色关联表表名
     */
    public static final String AUTH_DEPT_ROLE = "auth_dept_role";
    /**
     * 角色权限关联表表名
     */
    public static final String AUTH_ROLE_PERMISSION = "auth_role_permission";
    /**
     * 角色应用关联表表名
     */
    public static final String AUTH_ROLE_APP = "auth_role_app";

    // 字段名

    /**
     * 账号
     */
    public static final String ACCOUNT = "account";
    /**
     * 编码
     */
    public static final String CODE = "code";
    /**
     * 状态
     */
    public static final String STATUS = "status";
    /**
     * 盐
     */
    public static final String SALT = "salt";
    /**
     * 密码
     */
    public static final String PASSWORD = "password";
    /**
     * 创建人
     */
    public static final String CREATE_BY = "createBy";
    /**
     * 创建时间
     */
    public static final String CREATE_TIME = "createTime";
    /**
     * 修改人
     */
    public static final String UPDATE_BY = "updateBy";
    /**
     * 修改时间
     */
    public static final String UPDATE_TIME = "updateTime";
    /**
     * 备注说明
     */
    public static final String REMARK = "remark";
    /**
     * 角色编码
     */
    public static final String ROLE_CODE = "roleCode";
    /**
     * 权限编码
     */
    public static final String PERMISSION_CODE = "permissionCode";
    /**
     * 部门编码
     */
    public static final String DEPT_CODE = "deptCode";
    /**
     * 组织编码
     */
    public static final String GROUP_CODE = "groupCode";
    /**
     * 应用编码
     */
    public static final String APP_CODE = "appCode";
    /**
     * 类型
     */
    public static final String TYPE = "type";
    /**
     * 路径
     */
    public static final String URL = "url";
    /**
     * 显示顺序
     */
    public static final String ORDER_NUM = "orderNum";
    /**
     * 头像
     */
    public static final String PHOTO = "photo";
    /**
     * 邮箱
     */
    public static final String EMAIL = "email";
    /**
     * 性别
     */
    public static final String GENDER = "gender";
    /**
     * 手机
     */
    public static final String MOBILE = "mobile";

    // 字段值

    /**
     * 启用
     */
    public static final Byte STATUS_ON = 1;
    /**
     * 禁用
     */
    public static final Byte STATUS_OFF = 0;
    /**
     * 0：需登录
     */
    public static final Byte PERMISSION_LOGIN = 0;
    /**
     * 1：需认证
     */
    public static final Byte PERMISSION_AUTHOR = 1;
    /**
     * 2：需授权
     */
    public static final Byte PERMISSION_PERMS = 2;

    // 其它

    /**
     * 超管角色Code
     */
    public static final String ADMIN_CODE = "admin";
    /**
     * 盐的长度
     */
    public static final Integer SALT_LENGTH = 16;
    /**
     * 盐的长度
     */
    public static final String DEFAULT_PASSWORD = "88888888";
    /**
     * 当前用户的部门key
     */
    public static final String DEPT_KEY = "dept";
    /**
     * 当前用户的组织key
     */
    public static final String GROUP_KEY = "group";
    /**
     * 当前用户的角色key
     */
    public static final String ROLE_KEY = "roles";
    /**
     * 当前用户的权限key
     */
    public static final String PERMISSION_KEY = "permissions";
    /**
     * 当前用户的应用key
     */
    public static final String APP_KEY = "apps";
}
