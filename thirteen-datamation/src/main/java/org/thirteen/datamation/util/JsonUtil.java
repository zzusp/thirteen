package org.thirteen.datamation.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;

import static java.time.format.DateTimeFormatter.ofPattern;

/**
 * @author Aaron.Sun
 * @description JSON转换工具类
 * @date Created in 20:50 2020/6/24
 * @modified by
 */
public class JsonUtil {
    private static final Logger logger = LoggerFactory.getLogger(JsonUtil.class);
    private static final ObjectMapper MAPPER = new ObjectMapper();

    static {
        // 对于空的对象转json的时候不抛出错误
        MAPPER.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        // 允许属性名称没有引号
        MAPPER.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        // 允许单引号
        MAPPER.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
        // 设置输入时忽略在json字符串中存在但在java对象实际没有的属性
        MAPPER.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        // 设置输出时包含属性的风格
        MAPPER.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        SimpleModule module = new SimpleModule();
        module.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(ofPattern("yyyy-MM-dd HH:mm:ss")));
        module.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(ofPattern("yyyy-MM-dd HH:mm:ss")));
        MAPPER.registerModule(module);
    }


    /**
     * 序列化，将对象转化为json字符串
     *
     * @param data 要转化的对象
     * @return json字符串
     */
    public static String toJsonString(Object data) {
        if (data == null) {
            return null;
        }
        String json = null;
        try {
            json = MAPPER.writeValueAsString(data);
        } catch (JsonProcessingException e) {
            logger.error("{} toJsonString error：{}", data.getClass().getSimpleName(), e);
        }
        return json;
    }


    /**
     * 反序列化，将json字符串转化为对象
     *
     * @param json  json字符串
     * @param clazz 目标对象类型
     * @param <T>   泛型，目标对象
     * @return 转化后的对象
     */
    public static <T> T parseObject(String json, Class<T> clazz) {
        T t = null;
        try {
            t = MAPPER.readValue(json, clazz);
        } catch (Exception e) {
            logger.error(" parse json [{}] to class [{}] error：{{}}", json, clazz.getSimpleName(), e);
        }
        return t;
    }

}
