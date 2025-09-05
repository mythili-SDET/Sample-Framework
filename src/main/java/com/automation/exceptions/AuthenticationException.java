package com.automation.exceptions;

// Exception for authentication issues
public class AuthenticationException extends RuntimeException {
    public AuthenticationException(String message) { super(message); }
}
