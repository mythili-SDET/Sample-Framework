package com.automation.driver;


public enum ExecutionMode {
    LOCAL,
    CICD;

    public static ExecutionMode fromString(String mode) {
        try {
            return ExecutionMode.valueOf(mode.toUpperCase());
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid execution mode: " + mode);
        }
    }
}
