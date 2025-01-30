package com.example.utils;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

public class ResponseHandler {

    // Generic response method
    public static ResponseEntity<Object> generateResponse(String message, HttpStatus status, Object data, Map<String, Object> extraInfo) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", status.value());
        response.put("message", message);
        response.put("data", data);

        if (extraInfo != null) {
            response.putAll(extraInfo);
        }

        return new ResponseEntity<>(response, status);
    }

    // Overloaded methods for different use cases

    // Success response with data
    public static ResponseEntity<Object> success(String message, Object data) {
        return generateResponse(message, HttpStatus.OK, data, null);
    }

    // Success response with message only
    public static ResponseEntity<Object> success(String message) {
        return generateResponse(message, HttpStatus.OK, null, null);
    }

    // Success response with extra info
    public static ResponseEntity<Object> success(String message, Object data, Map<String, Object> extraInfo) {
        return generateResponse(message, HttpStatus.OK, data, extraInfo);
    }

    // Created response (201)
    public static ResponseEntity<Object> created(String message, Object data) {
        return generateResponse(message, HttpStatus.CREATED, data, null);
    }

    // Created response with extra info
    public static ResponseEntity<Object> created(String message, Object data, Map<String, Object> extraInfo) {
        return generateResponse(message, HttpStatus.CREATED, data, extraInfo);
    }

    // No content response (204)
    public static ResponseEntity<Object> noContent() {
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // Error response with message only
    public static ResponseEntity<Object> error(String message, HttpStatus status) {
        return generateResponse(message, status, null, null);
    }

    // Error response with extra info
    public static ResponseEntity<Object> error(String message, HttpStatus status, Map<String, Object> extraInfo) {
        return generateResponse(message, status, null, extraInfo);
    }

    // Error response with message and data
    public static ResponseEntity<Object> error(String message, HttpStatus status, Object data) {
        return generateResponse(message, status, data, null);
    }

    // Unauthorized (401)
    public static ResponseEntity<Object> unauthorized(String message) {
        return generateResponse(message, HttpStatus.UNAUTHORIZED, null, null);
    }

    // Forbidden (403)
    public static ResponseEntity<Object> forbidden(String message) {
        return generateResponse(message, HttpStatus.FORBIDDEN, null, null);
    }

    // Not Found (404)
    public static ResponseEntity<Object> notFound(String message) {
        return generateResponse(message, HttpStatus.NOT_FOUND, null, null);
    }

    // Conflict (409)
    public static ResponseEntity<Object> conflict(String message) {
        return generateResponse(message, HttpStatus.CONFLICT, null, null);
    }

    // Internal Server Error (500)
    public static ResponseEntity<Object> internalServerError(String message) {
        return generateResponse(message, HttpStatus.INTERNAL_SERVER_ERROR, null, null);
    }
}
