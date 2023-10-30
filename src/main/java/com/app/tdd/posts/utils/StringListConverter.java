package com.app.tdd.posts.utils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class StringListConverter implements AttributeConverter<List<String>, String> {

	private final static String DELIMITER = ",";
	
	@Override
	public String convertToDatabaseColumn(List<String> stringList) {
		if(stringList == null || stringList.isEmpty())
			return "";
		
		return stringList.stream().collect(Collectors.joining(DELIMITER));
	}

	@Override
	public List<String> convertToEntityAttribute(String dbData) {
		if(dbData == null || dbData.trim().isEmpty())
			return Collections.emptyList();
		
		return Arrays.asList(dbData.split(DELIMITER));
	}

}
