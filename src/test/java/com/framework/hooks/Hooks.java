package com.framework.hooks;

import com.framework.api.ApiBase;
import com.framework.drivers.DriverFactory;
import com.framework.utilities.DBUtil;
import io.cucumber.java.*;
import org.apache.logging.log4j.Logger;
import com.framework.utilities.LoggerUtil;

public class Hooks {

    private static final Logger log = LoggerUtil.getLogger(Hooks.class);
    private final ApiBase apiBase = new ApiBase();

    @Before(order = 0)
    public void globalSetup() {
        log.info("===== Global setup start =====");
        // DB connection is initialized statically; touching class to ensure load
        try {
            DBUtil.getConnection();
            log.info("Database connection pool initialized");
        } catch (Exception e) {
            log.error("DB init failed", e);
        }
    }

    @Before(value = "@ui", order = 1)
    public void uiSetup() {
        DriverFactory.initDriver();
        log.info("WebDriver initiated for UI scenario");
    }

    @Before(value = "@api", order = 1)
    public void apiSetup() {
        apiBase.initApi();
        log.info("API spec initialized for API scenario");
    }

    @After(value = "@ui", order = 1)
    public void uiTearDown() {
        DriverFactory.quitDriver();
        log.info("WebDriver quit after UI scenario");
    }

    @After(order = 0)
    public void globalTearDown() {
        log.info("===== Global tear down finished =====");
    }
}