package com.traelo.delivery.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class JsonConverter implements AttributeConverter<Object, String> {

	private final ObjectMapper objectMapper = new ObjectMapper();

	@Override
	public String convertToDatabaseColumn(Object attribute) {
		try {
			return attribute == null ? null : objectMapper.writeValueAsString(attribute);
		} catch (JsonProcessingException e) {
			throw new RuntimeException("Error converting to JSON", e);
		}
	}

	@Override
	public Object convertToEntityAttribute(String dbData) {
		try {
			return dbData == null ? null : objectMapper.readValue(dbData, Object.class);
		} catch (Exception e) {
			throw new RuntimeException("Error converting from JSON", e);
		}
	}
}