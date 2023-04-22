package com.thebizio.commonmodule.convertor;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.AttributeConverter;
import java.io.IOException;
import java.util.Collections;
import java.util.Map;

public class HashMapConvertor implements AttributeConverter<Map<String, Object>, String> {

	Logger logger = LoggerFactory.getLogger(HashMapConvertor.class);

	@Autowired
	private ObjectMapper objectMapper;

	@Override
	public String convertToDatabaseColumn(Map<String, Object> attribute) {
		String data = null;
		try {
			data = objectMapper.writeValueAsString(attribute);
		} catch (final JsonProcessingException e) {
			logger.error("JSON writing error", e);
		}

		return data;
	}

	@Override
	public Map<String, Object> convertToEntityAttribute(String dbData) {
		if (dbData == null || dbData.isEmpty()) return Collections.emptyMap();
		
		Map<String, Object> data = null;
		try {
			data = objectMapper.readValue(dbData, Map.class);
		} catch (final IOException e) {
			logger.error("JSON reading error", e);
		}

		return data;
	}

}
