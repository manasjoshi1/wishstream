package com.wishstream.core.exception;

import com.wishstream.core.utils.ResponseHandler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<Map<String, String>> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fieldError -> {
                    Map<String, String> errorDetails = new HashMap<>();
                    errorDetails.put("field", fieldError.getField());
                    errorDetails.put("value", fieldError.getRejectedValue() != null ? fieldError.getRejectedValue().toString() : "null");
                    errorDetails.put("message", fieldError.getDefaultMessage());
                    return errorDetails;
                })
                .collect(Collectors.toList());



        return ResponseHandler.error("Validation failed", HttpStatus.BAD_REQUEST, errors);
    }
}
