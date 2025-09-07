package com.automation.exceptions;

/**
 * Base exception for UI automation failures
 * Covers element interactions, navigation, and browser-related errors
 */
public class UIException extends RuntimeException {
    
    public UIException(String message) {
        super(message);
    }
    
    public UIException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public UIException(Throwable cause) {
        super(cause);
    }
}
