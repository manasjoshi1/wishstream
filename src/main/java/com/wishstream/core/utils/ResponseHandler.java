package com.wishstream.core.utils;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

public class ResponseHandler {

    // Generic response method with isSuccess and custom error code
    public static ResponseEntity<Object> generateResponse(
            String message, HttpStatus status, boolean isSuccess, Object data, Integer customErrorCode, Map<String, Object> extraInfo) {

        Map<String, Object> response = new HashMap<>();
        response.put("status", status.value());
        response.put("message", message);
        response.put("isSuccess", isSuccess);
        response.put("data", data);

        if (customErrorCode != null) {
            response.put("customErrorCode", customErrorCode);
        }

        if (extraInfo != null) {
            response.putAll(extraInfo);
        }

        return new ResponseEntity<>(response, status);
    }

    // Overloaded methods for different use cases

    // Success response with data
    public static ResponseEntity<Object> success(String message, Object data) {
        return generateResponse(message, HttpStatus.OK, true, data, null, null);
    }

    // Success response with message only
    public static ResponseEntity<Object> success(String message) {
        return generateResponse(message, HttpStatus.OK, true, null, null, null);
    }

    // Success response with extra info
    public static ResponseEntity<Object> success(String message, Object data, Map<String, Object> extraInfo) {
        return generateResponse(message, HttpStatus.OK, true, data, null, extraInfo);
    }

    // Created response (201)
    public static ResponseEntity<Object> created(String message, Object data) {
        return generateResponse(message, HttpStatus.CREATED, true, data, null, null);
    }

    // Created response with extra info
    public static ResponseEntity<Object> created(String message, Object data, Map<String, Object> extraInfo) {
        return generateResponse(message, HttpStatus.CREATED, true, data, null, extraInfo);
    }

    // No content response (204)
    public static ResponseEntity<Object> noContent() {
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // Error response with message only
    public static ResponseEntity<Object> error(String message, HttpStatus status) {
        return generateResponse(message, status, false, null, null, null);
    }

    // Error response with custom error code
    public static ResponseEntity<Object> error(String message, HttpStatus status, int customErrorCode) {
        return generateResponse(message, status, false, null, customErrorCode, null);
    }

    // Error response with extra info
    public static ResponseEntity<Object> error(String message, HttpStatus status, int customErrorCode, Map<String, Object> extraInfo) {
        return generateResponse(message, status, false, null, customErrorCode, extraInfo);
    }

    // Error response with message and data
    public static ResponseEntity<Object> error(String message, HttpStatus status, Object data) {
        return generateResponse(message, status, false, data, null, null);
    }

    // Unauthorized (401)
    public static ResponseEntity<Object> unauthorized(String message, int customErrorCode) {
        return generateResponse(message, HttpStatus.UNAUTHORIZED, false, null, customErrorCode, null);
    }

    // Forbidden (403)
    public static ResponseEntity<Object> forbidden(String message, int customErrorCode) {
        return generateResponse(message, HttpStatus.FORBIDDEN, false, null, customErrorCode, null);
    }

    // Not Found (404)
    public static ResponseEntity<Object> notFound(String message, int customErrorCode) {
        return generateResponse(message, HttpStatus.NOT_FOUND, false, null, customErrorCode, null);
    }

    // Conflict (409)
    public static ResponseEntity<Object> conflict(String message, int customErrorCode) {
        return generateResponse(message, HttpStatus.CONFLICT, false, null, customErrorCode, null);
    }

    // Internal Server Error (500)
    public static ResponseEntity<Object> internalServerError(String message, int customErrorCode) {
        return generateResponse(message, HttpStatus.INTERNAL_SERVER_ERROR, false, null, customErrorCode, null);
    }
}
