package com.automation.exceptions;

// Exception for validation failures
public class ValidationException extends RuntimeException {
    public ValidationException(String message) { super(message); }
}
