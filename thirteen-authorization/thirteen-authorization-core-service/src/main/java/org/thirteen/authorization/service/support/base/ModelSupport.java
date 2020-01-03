package org.thirteen.authorization.service.support.base;

import org.springframework.util.Assert;
import org.thirteen.authorization.common.utils.StringUtil;
import org.thirteen.authorization.exceptions.EntityErrorException;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Aaron.Sun
 * @description 模型对象帮助类
 * @date Created in 18:04 2019/12/27
 * @modified by
 */
public class ModelSupport<T, PK> {

    /** 主键ID字段 */
    public static final String ID_FIELD = "id";
    /** 创建者字段 */
    public static final String CREATE_BY_FIELD = "createBy";
    /** 创建时间字段 */
    public static final String CREATE_TIME_FIELD = "createTime";
    /** 更新者字段 */
    public static final String UPDATE_BY_FIELD = "updateBy";
    /** 更新时间字段 */
    public static final String UPDATE_TIME_FIELD = "updateTime";
    /** 逻辑删除字段 */
    public static final String DEL_FLAG_FIELD = "delFlag";
    /** 删除标记（0：正常；1：删除） */
    public static final String DEL_FLAG_NORMAL = "0";
    public static final String DEL_FLAG_DELETE = "1";

    /** 模型对象信息 */
    private ModelInformation<T, PK> modelInformation;

    public ModelSupport(ModelInformation<T, PK> modelInformation) {
        this.modelInformation = modelInformation;
    }

    /**
     * 初始化对象中的创建者/创建时间/更新者/更新时间字段
     *
     * @param obj    对象
     * @param isSave 是否为新增操作
     * @return 对象（已初始化创建者/创建时间/更新者/更新时间字段）
     */
    public T getSaveModel(T obj, boolean isSave) {
        return getModel(obj, isSave, LocalDateTime.now());
    }

    /**
     * 初始化对象集合中对象的创建者/创建时间/更新者/更新时间字段
     *
     * @param objs   对象集合
     * @param isSave 是否为新增操作
     * @return 对象集合（已初始化创建者/创建时间/更新者/更新时间字段）
     */
    public List<T> getSaveModels(List<T> objs, boolean isSave) {
        Assert.notEmpty(objs, "Object collection must not be empty!");
        LocalDateTime now = LocalDateTime.now();
        for (T obj : objs) {
            getModel(obj, isSave, now);
        }
        return objs;
    }

    /**
     * 初始化对象中的创建者/创建时间/更新者/更新时间字段
     *
     * @param obj    对象
     * @param isSave 是否为新增操作
     * @param now    操作时间
     * @return 对象（已初始化创建者/创建时间/更新者/更新时间字段）
     */
    private T getModel(T obj, boolean isSave, LocalDateTime now) {
        Assert.notNull(obj, "Object must not be null!");
        String timeFlag = UPDATE_TIME_FIELD;
        if (isSave) {
            timeFlag = CREATE_TIME_FIELD;
            // 判断是否存在逻辑删除字段，如果存在，则设置为未删除
            if (modelInformation.contains(DEL_FLAG_FIELD)) {
                Object delFlag = modelInformation.invokeGet(DEL_FLAG_FIELD, new Class[]{String.class}, obj);
                if (delFlag == null) {
                    modelInformation.invokeSet(DEL_FLAG_FIELD, new Class[]{String.class}, obj, DEL_FLAG_NORMAL);
                }
            }
        }
        // TODO 设置创建/更新者（账号）
        // 如果存在创建/更新时间字段，设置创建/更新时间
        if (modelInformation.contains(timeFlag)) {
            Object time = modelInformation.invokeGet(timeFlag, new Class[]{LocalDateTime.class}, obj);
            if (time == null) {
                modelInformation.invokeSet(timeFlag, new Class[]{LocalDateTime.class}, obj, now);
            }
        }
        return obj;
    }

    /**
     * 动态拼接insert sql（仅包含非空列）
     *
     * @param obj 对象
     * @return insert sql（仅包含非空列）
     */
    public String getInsertSelectiveSql(T obj) {
        Assert.notEmpty(modelInformation.fields, "Field collection must not be empty!");
        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO ").append(modelInformation.tableName).append("(");
        List<String> fields = new ArrayList<>();
        List<String> values = new ArrayList<>();
        // 循环中字段get方法返回值
        Object value;
        for (Field field : modelInformation.fields) {
            value = modelInformation.invokeGet(field.getName(), new Class[]{field.getType()}, obj);
            // 判断get方法返回值是否为null
            if (value != null) {
                // 如果不为null，存入集合
                fields.add(field.getName());
                // 判断数据类型是否为LocalDateTime
                if (field.getType() == java.time.LocalDateTime.class) {
                    values.add(((LocalDateTime) value).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                } else {
                    values.add(String.valueOf(value));
                }
            }
        }
        sql.append(StringUtil.join(fields, ",")).append(")");
        sql.append(" VALUES (");
        sql.append(StringUtil.join(values, ",")).append(")");
        return sql.toString();
    }

    /**
     * 动态拼接update sql（仅包含非空列）
     *
     * @param obj 对象
     * @return update sql（仅包含非空列）
     */
    public String getUpdateSelectiveSql(T obj) {
        Assert.notEmpty(modelInformation.fields, "Field collection must not be empty!");
        StringBuilder sql = new StringBuilder();
        sql.append("Update ").append(modelInformation.tableName).append(" SET ");
        List<String> equations = new ArrayList<>();
        // 循环中字段get方法返回值
        Object value;
        // 循环中的等式
        StringBuilder equation;
        for (Field field : modelInformation.fields) {
            equation = new StringBuilder();
            value = modelInformation.invokeGet(field.getName(), new Class[]{field.getType()}, obj);
            // 判断get方法返回值是否为null
            if (value != null) {
                // 如果不为null，存入集合
                equation.append(field.getName()).append("=");
                // 判断数据类型是否为LocalDateTime
                if (field.getType() == java.time.LocalDateTime.class) {
                    equation.append(((LocalDateTime) value).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                } else {
                    equation.append(value);
                }
                equations.add(equation.toString());
            }
        }
        sql.append(StringUtil.join(equations, ",")).append(")");
        return sql.toString();
    }


    /**
     * 获取创建model对象的对象
     *
     * @return 创建model对象的对象
     */
    public ModelBuilder builder() {
        return new ModelBuilder();
    }

    /**
     * 内部类（创建model对象）
     */
    public class ModelBuilder {
        /** ID */
        private PK id;

        /**
         * 设置ID
         *
         * @param id 主键ID
         * @return 当前对象
         */
        public ModelBuilder id(PK id) {
            this.id = id;
            return this;
        }

        /**
         * 创建model对象，并设置ID
         *
         * @return model对象
         */
        public T build() {
            try {
                T obj = modelInformation.getRealClass().getDeclaredConstructor().newInstance();
                if (id != null) {
                    modelInformation.invokeSet(ID_FIELD, new Class[]{modelInformation.getPkClass()}, obj, this.id);
                }
                return obj;
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                throw new EntityErrorException(e.getMessage());
            }
        }
    }

}
