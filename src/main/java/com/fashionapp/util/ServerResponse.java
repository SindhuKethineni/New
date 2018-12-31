package com.fashionapp.util;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;

public class ServerResponse<T> {
		public Map<String, Object> getEntityResponse(String message, int statusCode, boolean successful, T data) {
			Map<String, Object> errorResponse = new HashMap<String, Object>();
			errorResponse.put("message", message);
			errorResponse.put("statusCode", statusCode);
			errorResponse.put("successful", successful);
			errorResponse.put("data", data);

			return errorResponse;
		}

		public Map<String, Object> getSuccessResponse(String message, T data) {
			Map<String, Object> errorResponse = new HashMap<String, Object>();
			errorResponse.put("message", message);
			errorResponse.put("statusCode", HttpStatus.OK.value());
			errorResponse.put("successful", true);
			errorResponse.put("data", data);

			return errorResponse;
		}

		public Map<String, Object> getNotAceptableResponse(String message, T data) {
			Map<String, Object> errorResponse = new HashMap<String, Object>();
			errorResponse.put("message", message);
			errorResponse.put("statusCode", HttpStatus.NOT_ACCEPTABLE.value());
			errorResponse.put("successful", false);
			errorResponse.put("data", data);

			return errorResponse;
		}

		public Map<String, Object> getDuplicateResponse(String message, T data) {
			Map<String, Object> errorResponse = new HashMap<String, Object>();
			errorResponse.put("message", message);
			errorResponse.put("statusCode", HttpStatus.CONFLICT.value());
			errorResponse.put("successful", false);
			errorResponse.put("data", data);

			return errorResponse;
		}
		
		public Map<String, Object> getNotFoundResponse(String message, T data) {
			Map<String, Object> errorResponse = new HashMap<String, Object>();
			errorResponse.put("message", message);
			errorResponse.put("statusCode", HttpStatus.NOT_FOUND.value());
			errorResponse.put("successful", false);
			errorResponse.put("data", data);

			return errorResponse;
		}

}
