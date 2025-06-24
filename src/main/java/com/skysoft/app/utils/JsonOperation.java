package com.skysoft.app.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class JsonOperation {
	private final ObjectMapper objectMapper;

	public Object convertJsonToObject(String json, Class<?> clazz) {
		try {
			return objectMapper.readValue(json, clazz);
		} catch (Exception e) {
			log.error("Error while converting json to object", e);
			return null;
		}
	}

	public String convertObjectToJson(Object object) {
		try {
			return objectMapper.writeValueAsString(object);
		} catch (Exception e) {
			log.error("Error while converting object to json", e);
			return null;
		}
	}

	public String getJsonFromObject(Object object) {
		try {
			return objectMapper.writeValueAsString(object);
		} catch (Exception e) {
			log.error("Error while getting json from object", e);
			return null;
		}
	}

	public JsonNode getJsonTreeNode(String json, String value) {
		try {
			return objectMapper.readTree(json).get(value);
		} catch (Exception e) {
			log.error("Error while getting json tree node", e);
			return null;
		}
	}
}
