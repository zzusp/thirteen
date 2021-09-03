package org.thirteen.datamation.mongodb.model.query;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @author 孙鹏
 * @description 查询参数对象
 * @date Created in 14:58 2021/9/1
 * @modified By
 */
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class DmQuery implements Serializable {

    private static final long serialVersionUID = -2185899644469898950L;

    /** table编码 */
    private String tableCode;
    /** 常用参数：主键；注意：该参数与and or参数不能结合使用 */
    private String id;
    /** 常用参数：主键集合；注意：该参数与and or参数不能结合使用 */
    private List<String> ids;

    /** 条件参数，条件间关系为与 */
    private List<Criteria> and;
    /** 条件参数，条件间关系为或 */
    private List<Criteria> or;
    /** 分页参数 */
    private Page page;
    /** 排序参数 */
    private List<Sort> sorts;
    /** 更新参数 */
    private List<UpdateValue> updateValues;

    @Data
    @EqualsAndHashCode(callSuper = false)
    public static class Criteria implements Serializable {

        private static final long serialVersionUID = 7480053300179505095L;
        public static final String IS = "is";
        public static final String NE = "ne";
        public static final String LT = "lt";
        public static final String LTE = "lte";
        public static final String GT = "gt";
        public static final String GTE = "gte";
        public static final String IN = "in";
        public static final String NIN = "nin";
        public static final String MOD = "mod";
        public static final String SIZE = "size";
        public static final String EXISTS = "exists";
        public static final String TYPE = "type";
        public static final String NOT = "not";
        public static final String REGEX = "regex";
        public static final String GT_AND_LT = "gt&lt";
        public static final String GTE_AND_LT = "gte&lt";
        public static final String GTE_AND_LTE = "gte&lte";
        public static final String GT_AND_LTE = "gt&lte";
        public static final String GT_OR_LT = "gt|lt";
        public static final String GTE_OR_LT = "gte|lt";
        public static final String GTE_OR_LTE = "gte|lte";
        public static final String GT_OR_LTE = "gt|lte";
        public static final String LIKE = "like";
        public static final String CONTAINS = "contains";

        /** 字段 */
        private String field;
        /** 比较操作符，默认为is */
        private String operator;
        /** 字段对应值 */
        private Object value;
        /** 字段对应值的集合，一般用于比较操作符in */
        private List<Object> values;
        /** 是否必选（字段对应值为空或null时，条件是否仍生效），默认为false */
        private boolean required;
        /** 条件参数 */
        private List<Criteria> and;
        /** 条件参数 */
        private List<Criteria> or;

        public Criteria() {
            this.operator = IS;
            this.required = false;
        }
    }

    @Data
    @EqualsAndHashCode(callSuper = false)
    public static class Page implements Serializable {

        private static final long serialVersionUID = 3104508468209955651L;

        /** 当前页码，第一页页码为1 */
        private Integer pageNum;
        /** 每页大小 */
        private Integer pageSize;
    }

    @Data
    @EqualsAndHashCode(callSuper = false)
    public static class Sort implements Serializable {

        private static final long serialVersionUID = -822664279721088576L;
        public static final String ASC = "asc";
        public static final String DESC = "desc";

        /** 字段名 */
        private String field;
        /** 排序 asc（升序）/desc（降序） */
        private String orderBy;
    }

    @Data
    @EqualsAndHashCode(callSuper = false)
    public static class UpdateValue implements Serializable {

        private static final long serialVersionUID = -5295615531378679597L;

        /** 字段名 */
        private String field;
        /** 更改后的值 */
        private Object value;
    }

}
