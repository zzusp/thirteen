package org.thirteen.datamation.core;

/**
 * @author Aaron.Sun
 * @description Datamation常用code
 * @date Created in 15:21 2020/8/20
 * @modified By
 */
public interface DmCodes {

    // 字段

    /** 表中删除标识列 字段名称 */
    String DEL_FLAG_KEY = "delFlag";

    // 字段值

    /** 删除标识：未删除 */
    Byte DEL_FLAG_NORMAL = 0;
    /** 删除标识：已删除 */
    Byte DEL_FLAG_DELETE = 1;

    // 判断条件

    /** field = value */
    String EQUAL = "equal";
    /** filed != value */
    String NOT_EQUAL = "notEqual";
    /** filed > value  value为Number类型数据 */
    String GT = "gt";
    /** filed >= value  value为Number类型数据 */
    String GE = "ge";
    /** filed < value  value为Number类型数据 */
    String LT = "lt";
    /** filed <= value  value为Number类型数据 */
    String LE = "le";
    /** filed > value  value为Comparable类型数据 */
    String GREATER_THAN = "greaterThan";
    /** filed >= value  value为Comparable类型数据 */
    String GREATER_THAN_OR_EQUAL_TO = "greaterThanOrEqualTo";
    /** filed < value  value为Comparable类型数据 */
    String LESS_THEN = "lessThan";
    /** filed <= value  value为Comparable类型数据 */
    String LESS_THAN_OR_EQUAL_TO = "lessThanOrEqualTo";
    /** filed like value */
    String LIKE = "like";
    /** filed not like value */
    String NOT_LIKE = "notLike";
    /** filed in (...values) */
    String IN = "in";

    // 条件间关系

    /** filed != value and filed like value */
    String AND = "and";
    /** filed != value or filed like value */
    String OR = "or";

    // 排序

    /** 升序 */
    String ASC = "asc";
    /** 降序 */
    String DESC = "desc";
}
