package com.automation.exceptions;

/**
 * Exception thrown when UI element is found but not interactable
 */
public class ElementNotInteractableException extends UIException {
    
    public ElementNotInteractableException(String message) {
        super(message);
    }
    
    public ElementNotInteractableException(String message, Throwable cause) {
        super(message, cause);
    }
}
