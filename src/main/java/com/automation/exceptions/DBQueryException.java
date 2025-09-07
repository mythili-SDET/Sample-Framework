package com.automation.exceptions;

/**
 * Exception thrown when database query execution fails
 */
public class DBQueryException extends DBException {
    
    public DBQueryException(String message) {
        super(message);
    }
    
    public DBQueryException(String message, Throwable cause) {
        super(message, cause);
    }
}
