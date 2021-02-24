package org.thirteen.datamation.core;

/**
 * @author Aaron.Sun
 * @description Datamation常用code
 * @date Created in 15:21 2020/8/20
 * @modified By
 */
public class DmCodes {

    private DmCodes() {
    }

    // 字段

    /**
     * code
     */
    public static final String CODE = "code";

    // 字段值

    /** 字段类型：主键字段 */
    public static final Byte COLUMN_TYPE_ID = 0;
    /** 字段类型：逻辑删除字段 */
    public static final Byte COLUMN_TYPE_DEL_FLAG = 1;
    /** 字段类型：版本号字段 */
    public static final Byte COLUMN_TYPE_VERSION = 2;

    /** 删除标识：未删除 */
    public static final Byte DEL_FLAG_NORMAL = 0;
    /** 删除标识：已删除 */
    public static final Byte DEL_FLAG_DELETE = 1;

    /** 列数据库类型：FLOAT */
    public static final String COLUMN_DB_FLOAT = "FLOAT";
    /** 列数据库类型：DOUBLE */
    public static final String COLUMN_DB_DOUBLE = "DOUBLE";
    /** 列数据库类型：DECIMAL */
    public static final String COLUMN_DB_DECIMAL = "DECIMAL";

    // 判断条件

    /**
     * field = value
     */
    public static final String EQUAL = "equal";
    /**
     * filed != value
     */
    public static final String NOT_EQUAL = "notEqual";
    /**
     * filed > value  value为Number类型数据
     */
    public static final String GT = "gt";
    /**
     * filed >= value  value为Number类型数据
     */
    public static final String GE = "ge";
    /**
     * filed < value  value为Number类型数据
     */
    public static final String LT = "lt";
    /**
     * filed <= value  value为Number类型数据
     */
    public static final String LE = "le";
    /**
     * filed > value  value为Comparable类型数据
     */
    public static final String GREATER_THAN = "greaterThan";
    /**
     * filed >= value  value为Comparable类型数据
     */
    public static final String GREATER_THAN_OR_EQUAL_TO = "greaterThanOrEqualTo";
    /**
     * filed < value  value为Comparable类型数据
     */
    public static final String LESS_THAN = "lessThan";
    /**
     * filed <= value  value为Comparable类型数据
     */
    public static final String LESS_THAN_OR_EQUAL_TO = "lessThanOrEqualTo";
    /**
     * filed like value
     */
    public static final String LIKE = "like";
    /**
     * filed not like value
     */
    public static final String NOT_LIKE = "notLike";
    /**
     * filed in (...values)
     */
    public static final String IN = "in";

    // 条件间关系

    /**
     * filed != value and filed like value
     */
    public static final String AND = "and";
    /**
     * filed != value or filed like value
     */
    public static final String OR = "or";

    // 排序

    /**
     * 升序
     */
    public static final String ASC = "asc";
    /**
     * 降序
     */
    public static final String DESC = "desc";
}
