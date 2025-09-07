package com.automation.exceptions;

/**
 * Exception thrown when database connection fails
 */
public class DBConnectionException extends DBException {
    
    public DBConnectionException(String message) {
        super(message);
    }
    
    public DBConnectionException(String message, Throwable cause) {
        super(message, cause);
    }
}
