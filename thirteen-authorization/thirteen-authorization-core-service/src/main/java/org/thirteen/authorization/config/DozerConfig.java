package org.thirteen.authorization.config;

import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Aaron.Sun
 * @description dozer配置
 * @date Created in 17:10 2018/12/7
 * @modified by
 */
@Configuration
public class DozerConfig {

    /**
     * mapper配置文件
     * 注意classpath*不可省略！！！
     */
    @Value("${dozer.mapper.files}")
    Resource[] files;

    @Bean
    public DozerBeanMapper dozerBean() {
        List<String> mappings = Arrays.stream(files).map(resource -> {
            try {
                return resource.getURL().toString();
            } catch (IOException e) {
                return null;
            }
        }).collect(Collectors.toList());
        DozerBeanMapper dozerBean = new DozerBeanMapper();
        dozerBean.setMappingFiles(mappings);
        return dozerBean;
    }

    /**
     * 数组合并
     *
     * @param first  第一个数组
     * @param second 第二个数组
     * @param <T>    数据对象
     * @return 合并后的数组
     */
    public static <T> T[] concat(T[] first, T[] second) {
        T[] result = Arrays.copyOf(first, first.length + second.length);
        System.arraycopy(second, 0, result, first.length, second.length);
        return result;
    }

}
