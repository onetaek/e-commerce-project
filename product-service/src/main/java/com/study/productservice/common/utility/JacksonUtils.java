package com.study.productservice.common.utility;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JacksonUtils {
	private static final ObjectMapper objectMapper;

	static {
		objectMapper = new ObjectMapper();
		// 알 수 없는 필드 무시 설정
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	}

	/**
	 * 객체를 JSON 문자열로 변환
	 *
	 * @param object 변환할 객체
	 * @return JSON 문자열
	 */
	public static String convertObjectToJsonString(Object object) {
		try {
			return objectMapper.writeValueAsString(object);
		} catch (JsonProcessingException e) {
			throw new RuntimeException("Failed to convert object to JSON string", e);
		}
	}

	/**
	 * JSON 문자열을 객체로 변환
	 *
	 * @param <T>   변환할 객체의 타입
	 * @param json  JSON 문자열
	 * @param clazz 변환할 클래스 타입
	 * @return 변환된 객체
	 */
	public static <T> T convertJsonStringToObject(String json, Class<T> clazz) {
		try {
			// 이스케이프된 JSON 문자열 처리
			if (json.startsWith("\"") && json.endsWith("\"")) {
				// 앞뒤 따옴표 제거 및 이스케이프 문자 처리
				json = json.substring(1, json.length() - 1).replace("\\\"", "\"");
			}
			return objectMapper.readValue(json, clazz);
		} catch (JsonProcessingException e) {
			throw new RuntimeException("Failed to convert JSON string to object", e);
		}
	}

	/**
	 * JSON 문자열을 특정 컬렉션 타입 객체로 변환
	 *
	 * @param <T>      변환할 객체의 타입
	 * @param json     JSON 문자열
	 * @param typeRef  변환할 컬렉션 타입 참조
	 * @return 변환된 객체
	 */
	public static <T> T convertJsonStringToObject(String json, TypeReference<T> typeRef) {
		try {
			return objectMapper.readValue(json, typeRef);
		} catch (JsonProcessingException e) {
			throw new RuntimeException("Failed to convert JSON string to object", e);
		}
	}
}