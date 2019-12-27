package org.thirteen.authorization.service.support.base;

import org.thirteen.authorization.exceptions.EntityErrorException;

import java.lang.reflect.InvocationTargetException;

/**
 * @author Aaron.Sun
 * @description 模型对象帮助类
 * @date Created in 18:04 2019/12/27
 * @modified by
 */
public class ModelSupport<T, PK> {

    /**
     * 模型对象信息
     */
    private ModelInformation<T, PK> modelInformation;

    public ModelSupport(ModelInformation<T, PK> modelInformation) {
        this.modelInformation = modelInformation;
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
        /**
         * ID
         */
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
                    modelInformation.invokeSet("id", new Class[]{modelInformation.getPkClass()}, obj, this.id);
                }
                return obj;
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                throw new EntityErrorException(e.getMessage());
            }
        }
    }

}
