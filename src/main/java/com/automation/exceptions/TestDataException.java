package com.automation.exceptions;

/**
 * Exception thrown when test data loading or processing fails
 */
public class TestDataException extends RuntimeException {
    
    public TestDataException(String message) {
        super(message);
    }
    
    public TestDataException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public TestDataException(Throwable cause) {
        super(cause);
    }
}
