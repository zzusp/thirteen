package org.thirteen.authorization.service.support.base;

import org.springframework.util.Assert;
import org.thirteen.authorization.common.utils.StringUtil;
import org.thirteen.authorization.exceptions.DataNotFoundException;
import org.thirteen.authorization.exceptions.EntityErrorException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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
     * @param obj       对象
     * @param userField 创建者/更新者字段
     * @param timeField 创建者时间/更新时间字段
     * @param isSave    是否为新增操作
     * @return 对象（已初始化创建者/创建时间/更新者/更新时间字段）
     */
    public T getModel(T obj, String userField, String timeField, boolean isSave) {
        return getModel(obj, userField, timeField, isSave, LocalDateTime.now());
    }

    /**
     * 初始化对象中的创建者/创建时间/更新者/更新时间字段
     *
     * @param obj       对象
     * @param userField 创建者/更新者字段
     * @param timeField 创建者时间/更新时间字段
     * @param isSave    是否为新增操作
     * @param now       操作时间
     * @return 对象（已初始化创建者/创建时间/更新者/更新时间字段）
     */
    public T getModel(T obj, String userField, String timeField, boolean isSave, LocalDateTime now) {
        Assert.notNull(obj, "Object must not be null!");
        // TODO 设置创建者/更新者（账号）
        // 如果存在创建时间/更新时间字段，设置创建时间/更新时间
        if (StringUtil.isNotEmpty(timeField) && modelInformation.contains(timeField)) {
            Object time = modelInformation.invokeGet(timeField, new Class[]{LocalDateTime.class}, obj);
            if (time == null) {
                modelInformation.invokeSet(timeField, new Class[]{LocalDateTime.class}, obj, now);
            }
        }
        // 如果存为新增操作，则判断是否存在逻辑删除字段，如果存在，则设置为未删除
        if (isSave && modelInformation.contains(DEL_FLAG_FIELD)) {
            Object time = modelInformation.invokeGet(DEL_FLAG_FIELD, new Class[]{String.class}, obj);
            if (time == null) {
                modelInformation.invokeSet(DEL_FLAG_FIELD, new Class[]{String.class}, obj, DEL_FLAG_NORMAL);
            }
        }
        return obj;
    }

    /**
     * 初始化对象集合中对象的创建者/创建时间/更新者/更新时间字段
     *
     * @param objs      对象集合
     * @param userField 创建者/更新者字段
     * @param timeField 创建者时间/更新时间字段
     * @param isSave    是否为新增操作
     * @return 对象集合（已初始化创建者/创建时间/更新者/更新时间字段）
     */
    public List<T> getModels(List<T> objs, String userField, String timeField, boolean isSave) {
        Assert.notEmpty(objs, "Object collection must not be empty!");
        LocalDateTime now = LocalDateTime.now();
        for (T obj : objs) {
            getModel(obj, userField, timeField, isSave, now);
        }
        return objs;
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
