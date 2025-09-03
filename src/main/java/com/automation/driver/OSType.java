package com.automation.driver;


public enum OSType {
    WINDOWS,
    MAC,
    LINUX;

    public static OSType fromString(String os) {
        try {
            return OSType.valueOf(os.toUpperCase());
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid OS: " + os);
        }
    }
}
