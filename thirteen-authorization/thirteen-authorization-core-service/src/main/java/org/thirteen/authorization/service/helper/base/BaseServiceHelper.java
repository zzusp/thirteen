package org.thirteen.authorization.service.helper.base;

import org.thirteen.authorization.common.utils.StringUtil;
import org.thirteen.authorization.exceptions.EntityErrorException;
import org.thirteen.authorization.model.po.base.BasePO;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author Aaron.Sun
 * @description 通用Service层帮助类
 * @date Created in 21:43 2018/1/10
 * @modified by
 */
public class BaseServiceHelper {

    /**
     * 由泛型真实类型的class反射实例化
     *
     * @param clazz 泛型真实类型的Class
     * @param <T>   对象类型
     * @return 实例化对象
     */
    public static <T> T newInstance(Class<T> clazz) {
        try {
            return clazz.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new EntityErrorException(clazz.getPackageName() + clazz.getName() + "实例化失败");
        }
    }

    /**
     * 由泛型真实类型的class反射实例化PO对象，并设置ID
     *
     * @param clazz 泛型真实类型的Class
     * @param id    主键ID
     * @param <T>   对象类型
     * @param <PK>  主键类型
     * @return 实例化对象
     */
    public static <T extends BasePO<PK>, PK> T newPoInstance(Class<T> clazz, PK id) {
        T obj = newInstance(clazz);
        obj.setId(id);
        return obj;
    }

    /**
     * 判断目标类中是否包含指定字段，不包含则抛出异常
     *
     * @param clazz 目标类
     * @param field 指定字段
     * @return 目标类中是否包含指定字段
     */
    public static boolean checkField(Class<?> clazz, String field) {
        boolean flag = true;
        try {
            clazz.getDeclaredField(field);
        } catch (NoSuchFieldException e) {
            flag = false;
        }
        return flag;
    }

    /**
     * 获取目标类中指定字段的set方法
     *
     * @param clazz          目标类
     * @param field          指定字段
     * @param parameterTypes set方法的入参类型
     * @return 目标类中指定字段的set方法
     * @throws EntityErrorException 目标类中无指定字段异常
     */
    public static Method getFieldSetMethod(Class<?> clazz, String field, Class<?>... parameterTypes)
        throws EntityErrorException {
        Method method;
        // 字段的set方法名
        String setter = "set" + StringUtil.capitalize(field);
        try {
            method = clazz.getMethod(setter, parameterTypes);
        } catch (NoSuchMethodException e) {
            throw new EntityErrorException(clazz.getPackageName() + clazz.getName() + "无" + setter + "()方法");
        }
        return method;
    }

    /**
     * 调用目标类对象中指定的set方法
     *
     * @param setMethod set方法
     * @param obj       目标类对象
     * @param args      set方法的入参
     */
    public static void invokeFieldSetMethod(Method setMethod, Object obj, Object... args) {
        try {
            setMethod.invoke(obj, args);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new EntityErrorException("调用" + setMethod.getName() + "失败", e.getCause());
        }
    }

}
