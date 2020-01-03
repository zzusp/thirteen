package org.thirteen.authorization.service.support.base;

import org.springframework.util.Assert;
import org.thirteen.authorization.common.utils.StringUtil;
import org.thirteen.authorization.exceptions.EntityErrorException;

import javax.persistence.Table;
import java.lang.reflect.*;

/**
 * @author Aaron.Sun
 * @description 通过反射获取model（PO、VO等对象）的信息，如：是否包含某属性，是否包含某方法，反射调用某方法等
 * @date Created in 10:26 2019/12/27
 * @modified by
 */
public class ModelInformation<T, PK> {

    /** 当前泛型真实类型的Class */
    private Class<T> realClass;
    /** 当前泛型真实类型的Class */
    private Class<PK> pkClass;
    /** 模型对应的表名，仅PO模型存在表名 */
    public String tableName;
    /** 当前泛型对象中的所有属性，包含父类中的属性 */
    public Field[] fields;

    /**
     * 通过反射获取子类确定的泛型类
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    ModelInformation() {
        Type genType = getClass().getGenericSuperclass();
        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
        realClass = (Class<T>) params[0];
        pkClass = (Class<PK>) params[1];
        Table table = realClass.getAnnotation(Table.class);
        if (table != null) {
            tableName = table.name();
        }
        fields = realClass.getFields();
    }

    /**
     * 判断是否包含指定字段，不包含则抛出异常
     *
     * @param field 指定字段
     * @return 是否包含指定字段
     */
    public boolean contains(String field) {
        Assert.notNull(field, "The given field must not be null!");
        boolean flag = true;
        try {
            realClass.getField(field);
        } catch (NoSuchFieldException e) {
            flag = false;
        }
        return flag;
    }

    /**
     * 获取指定字段的set方法
     *
     * @param field          指定字段
     * @param parameterTypes set方法的入参类型
     * @return 目标类中指定字段的set方法
     * @throws EntityErrorException 目标类中无指定字段异常
     */
    public Method getSetter(String field, Class<?>... parameterTypes) throws EntityErrorException {
        Assert.notNull(field, "The given field must not be null!");
        Method method;
        // 字段的set方法名
        String setter = "set" + StringUtil.capitalize(field);
        try {
            method = realClass.getMethod(setter, parameterTypes);
        } catch (NoSuchMethodException e) {
            throw new EntityErrorException(e.getMessage());
        }
        return method;
    }

    /**
     * 获取指定字段的get方法
     *
     * @param field          指定字段
     * @param parameterTypes get方法的入参类型
     * @return 目标类中指定字段的get方法
     * @throws EntityErrorException 目标类中无指定字段异常
     */
    public Method getGetter(String field, Class<?>... parameterTypes) throws EntityErrorException {
        Assert.notNull(field, "The given field must not be null!");
        Method method;
        // 字段的get方法名
        String getter = "get" + StringUtil.capitalize(field);
        try {
            method = realClass.getMethod(getter, parameterTypes);
        } catch (NoSuchMethodException e) {
            throw new EntityErrorException(e.getMessage());
        }
        return method;
    }

    /**
     * 调用指定的set方法
     *
     * @param field          指定字段
     * @param parameterTypes set方法的入参类型
     * @param obj            目标类对象
     * @param args           set方法的入参
     */
    public void invokeSet(String field, Class<?>[] parameterTypes, Object obj, Object... args) {
        Assert.notNull(field, "The given field must not be null!");
        Assert.notNull(obj, "Object must not be null!");
        invokeSet(getSetter(field, parameterTypes), obj, args);
    }

    /**
     * 调用指定的get方法
     *
     * @param field          指定字段
     * @param parameterTypes get方法的入参类型
     * @param obj            目标类对象
     * @param args           get方法的入参
     */
    public Object invokeGet(String field, Class<?>[] parameterTypes, Object obj, Object... args) {
        Assert.notNull(field, "The given field must not be null!");
        Assert.notNull(obj, "Object must not be null!");
        return invokeGet(getGetter(field, parameterTypes), obj, args);
    }

    /**
     * 调用指定的set方法
     *
     * @param setMethod set方法
     * @param obj       目标类对象
     * @param args      set方法的入参
     */
    public void invokeSet(Method setMethod, Object obj, Object... args) {
        Assert.notNull(obj, "Object must not be null!");
        try {
            setMethod.invoke(obj, args);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new EntityErrorException(e.getMessage(), e.getCause());
        }
    }

    /**
     * 调用指定的get方法
     *
     * @param getMethod get方法
     * @param obj       目标类对象
     * @param args      get方法的入参
     */
    public Object invokeGet(Method getMethod, Object obj, Object... args) {
        Assert.notNull(obj, "Object must not be null!");
        try {
            return getMethod.invoke(obj, args);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new EntityErrorException(e.getMessage(), e.getCause());
        }
    }

    /**
     * 获取对象的真实类型
     *
     * @return 对象的真实类型
     */
    public Class<T> getRealClass() {
        return this.realClass;
    }

    /**
     * 获取主键的真实类型
     *
     * @return 主键的真实类型
     */
    public Class<PK> getPkClass() {
        return this.pkClass;
    }

}
