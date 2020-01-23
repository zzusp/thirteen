package org.thirteen.authorization.dozer;

import org.dozer.DozerConverter;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * @author Aaron.Sun
 * @description 自定义dozer对象转换配置，针对LocalDateTime与LocalDateTime间的转换
 * @date Created in 17:10 2020/12/7
 * @modified by
 */
public class LocalDateTimeDozerConverter extends DozerConverter<LocalDateTime, LocalDateTime> {

    public LocalDateTimeDozerConverter() {
        super(LocalDateTime.class, LocalDateTime.class);
    }

    @Override
    public LocalDateTime convertTo(LocalDateTime source, LocalDateTime destination) {
        return Objects.isNull(destination) ? null : LocalDateTime.of(destination.toLocalDate(), destination.toLocalTime());
    }

    @Override
    public LocalDateTime convertFrom(LocalDateTime source, LocalDateTime destination) {
        return Objects.isNull(source) ? null : LocalDateTime.of(source.toLocalDate(), source.toLocalTime());
    }
}
