package com.wishstream.core.exception;

public class ValidationException extends Throwable {
    public ValidationException(String message) {
        super(message);
    }
    public ValidationException(){
        super();
    }
}
