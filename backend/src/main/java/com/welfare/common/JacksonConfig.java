package com.welfare.common;

import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.format.DateTimeFormatter;

@Configuration
public class JacksonConfig {

    private static final String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
    private static final String DATE_PATTERN = "yyyy-MM-dd";
    private static final String TIME_PATTERN = "HH:mm:ss";

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer() {
        return builder -> {
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(DATE_TIME_PATTERN);
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(DATE_PATTERN);
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern(TIME_PATTERN);

            builder.serializers(new LocalDateTimeSerializer(dateTimeFormatter));
            builder.serializers(new LocalDateSerializer(dateFormatter));
            builder.serializers(new LocalTimeSerializer(timeFormatter));

            builder.deserializers(new LocalDateTimeDeserializer(dateTimeFormatter));
            builder.deserializers(new LocalDateDeserializer(dateFormatter));
            builder.deserializers(new LocalTimeDeserializer(timeFormatter));
        };
    }
}
