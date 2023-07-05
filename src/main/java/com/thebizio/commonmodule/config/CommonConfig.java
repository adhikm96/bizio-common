package com.thebizio.commonmodule.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.thebizio.commonmodule.deserializer.TrimmedStringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Configuration
public class CommonConfig {

    @Bean
    public Jackson2ObjectMapperBuilder jackson2ObjectMapperBuilder() {
        return new Jackson2ObjectMapperBuilder();
    }

    @Bean
    ObjectMapper objectMapper(Jackson2ObjectMapperBuilder builder) {
        builder.serializerByType(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        builder.serializerByType(LocalDate.class, new LocalDateSerializer(DateTimeFormatter.ISO_LOCAL_DATE));
        builder.deserializerByType(String.class, new TrimmedStringDeserializer());
        return builder.build();
    }
}
