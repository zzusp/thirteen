package org.thirteen.authorization.service.support.base;

import org.springframework.util.Assert;
import org.thirteen.authorization.common.utils.StringUtil;

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
public class ModelSupport<T> {

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

    /** 模型对象信息 */
    private ModelInformation<T> modelInformation;

    public ModelSupport(ModelInformation<T> modelInformation) {
        this.modelInformation = modelInformation;
    }

    public T newInstance() {
        try {
            return modelInformation.getRealClass().getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            return null;
        }
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
}
