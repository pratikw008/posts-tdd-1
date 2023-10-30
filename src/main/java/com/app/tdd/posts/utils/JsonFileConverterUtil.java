package com.app.tdd.posts.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;

public interface JsonFileConverterUtil {
	
	public static <T> List<T> convertJsonToList(String fileName, Class<T> clazz) {
		List<T>  t = null;
		try(InputStream inputStream = TypeReference.class.getResourceAsStream(fileName)) {
			ObjectMapper objectMapper = new ObjectMapper();
			CollectionType collectionType = objectMapper.getTypeFactory().constructCollectionType(List.class, clazz);
			t = objectMapper.readValue(inputStream, collectionType);
			
		} catch (IOException e) {
			throw new RuntimeException("Failed to Read JSON file: "+fileName);
		}
		return t;
	}
}
