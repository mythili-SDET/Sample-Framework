package com.automation.exceptions;

/**
 * Exception thrown when UI element is not found
 */
public class ElementNotFoundException extends UIException {
    
    public ElementNotFoundException(String message) {
        super(message);
    }
    
    public ElementNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
