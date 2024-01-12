package com.thebizio.commonmodule.convertor;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thebizio.commonmodule.dto.brand.AppDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.AttributeConverter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ListConvertorForAppDto implements AttributeConverter<List<AppDto>, String> {
    Logger logger = LoggerFactory.getLogger(ListConvertorForAppDto.class);

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public String convertToDatabaseColumn(List<AppDto> attribute) {
        String data = null;
//		objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        try {
            data = objectMapper.writeValueAsString(attribute);
        } catch (final JsonProcessingException e) {
            logger.error("JSON writing error", e);
        }

        return data;
    }

    @Override
    public List<AppDto> convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isEmpty()) return Collections.emptyList();

        List<AppDto> data = new ArrayList<>();
        try {

            data = objectMapper.readValue(dbData, new TypeReference<List<AppDto>>() {
            });
        } catch (final IOException e) {
            logger.error("JSON reading error", e);
        }
        return data;
    }
}
