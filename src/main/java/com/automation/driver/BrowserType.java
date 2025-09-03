package com.automation.driver;


public enum BrowserType {
    CHROME,
    FIREFOX,
    EDGE;

    public static BrowserType fromString(String browser) {
        try {
            return BrowserType.valueOf(browser.toUpperCase());
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid Browser: " + browser);
        }
    }
}
