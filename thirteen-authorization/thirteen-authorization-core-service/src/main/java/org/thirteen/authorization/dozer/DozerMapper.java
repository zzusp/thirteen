package org.thirteen.authorization.dozer;

import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;
import org.dozer.loader.api.BeanMappingBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static org.dozer.loader.api.TypeMappingOptions.mapEmptyString;
import static org.dozer.loader.api.TypeMappingOptions.mapNull;

/**
 * @author Aaron.Sun
 * @description 针对对象转换器dozer添加一层封装，避免传入对象为空时报错
 * @date Created in 10:44 2018/1/19
 * @modified By
 */
@Component("dozerMapper")
public class DozerMapper {

    private final Mapper mapper;

    @Autowired
    public DozerMapper(Mapper mapper) {
        this.mapper = mapper;
    }

    /**
     * 基于Dozer转换对象的类型.
     *
     * @param source           源对象
     * @param destinationClass 目标对象类型
     * @param <T>              数据类型
     * @return 目标对象
     */
    public <T> T map(Object source, Class<T> destinationClass) {
        if (source == null) {
            return null;
        }
        return this.mapper.map(source, destinationClass);
    }

    /**
     * 基于Dozer转换Collection中对象的类型
     *
     * @param sourceList       源对象集合
     * @param destinationClass 目标对象类型
     * @param <T>              数据类型
     * @return 目标对象集合
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public <T> List<T> mapList(Collection sourceList, Class<T> destinationClass) {
        List<T> destinationList = new ArrayList<>();
        if (sourceList == null || sourceList.size() <= 0) {
            return destinationList;
        }
        return (List<T>) sourceList.stream().map(source -> this.map(source, destinationClass)).collect(Collectors.toList());
    }

    /**
     * 基于Dozer将对象A的值拷贝到对象B中
     *
     * @param source            源对象
     * @param destinationObject 目标对象
     */
    public void copy(Object source, Object destinationObject) {
        if (source == null) {
            destinationObject = null;
        }
        this.mapper.map(source, destinationObject);
    }

    /**
     * 基于Dozer将对象A的值拷贝到对象B中，不包含null
     * 高并发场景慎用！！！
     *
     * @param source            源对象
     * @param destinationObject 目标对象
     * @param mapNull           是否拷贝null值
     * @param mapEmptyString    是否拷贝空字符串
     */
    @SuppressWarnings("unchecked")
    public void copy(final Object source, final Object destinationObject, final boolean mapNull,
                     final boolean mapEmptyString) {
        // WeakReference弱引用
        // 当一个对象仅仅被weak reference指向, 而没有任何其他strong reference指向的时候, 如果GC运行, 那么这个对象就会被回收
        WeakReference weakReference = new WeakReference(new DozerBeanMapper());
        // 获得weak reference引用的object
        DozerBeanMapper mapper = (DozerBeanMapper) weakReference.get();
        // 断言为非空
        assert mapper != null;
        // 添加映射规则（是否拷贝null值，是否拷贝空字符串）
        mapper.addMapping(new BeanMappingBuilder() {
            @Override
            protected void configure() {
                mapping(source.getClass(), destinationObject.getClass(), mapNull(mapNull), mapEmptyString(mapEmptyString));
            }
        });
        // 拷贝
        mapper.map(source, destinationObject);
        // 销毁
        mapper.destroy();
        // 清理
        weakReference.clear();
    }

}
