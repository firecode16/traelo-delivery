package com.traelo.delivery.util;

import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class JsonOrderConverter implements AttributeConverter<List<String>, String> {
	private final ObjectMapper mapper = new ObjectMapper();

	@Override
	public String convertToDatabaseColumn(List<String> attribute) {
		try {
			return mapper.writeValueAsString(attribute);
		} catch (Exception e) {
			throw new RuntimeException("Error serializando resumen de orden", e);
		}
	}

	@Override
	public List<String> convertToEntityAttribute(String dbData) {
		try {
			return mapper.readValue(dbData, new TypeReference<List<String>>() { });
		} catch (Exception e) {
			throw new RuntimeException("Error deserializando resumen de orden", e);
		}
	}
}
