package com.automation.exceptions;

// Exception for bad API responses
public class ApiResponseException extends RuntimeException {
    public ApiResponseException(String message) { super(message); }
}
