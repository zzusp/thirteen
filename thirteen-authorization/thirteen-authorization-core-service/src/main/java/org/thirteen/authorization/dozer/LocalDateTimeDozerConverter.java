package org.thirteen.authorization.dozer;

import org.dozer.DozerConverter;

import java.time.LocalDateTime;

public class LocalDateTimeDozerConverter extends DozerConverter<LocalDateTime, LocalDateTime> {

    public LocalDateTimeDozerConverter(Class<LocalDateTime> source, Class<LocalDateTime> destination) {
        super(source, destination);
    }

    @Override
    public LocalDateTime convertTo(LocalDateTime source, LocalDateTime destination) {
        return LocalDateTime.of(destination.toLocalDate(), destination.toLocalTime());
    }

    @Override
    public LocalDateTime convertFrom(LocalDateTime source, LocalDateTime destination) {
        return LocalDateTime.of(source.toLocalDate(), source.toLocalTime());
    }
}
