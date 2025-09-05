package com.automation.tests;

import com.framework.config.ConfigManager;
import io.qameta.allure.Allure;
import io.restassured.RestAssured;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.BeforeSuite;

public class BaseTest {
    protected static final Logger logger = LogManager.getLogger(BaseTest.class);

    @BeforeSuite
    public void suiteSetup() {
        logger.info("Setting up test suite...");

        // Set base configuration
        RestAssured.baseURI = ConfigManager.getBaseUrl();
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();

        // Add environment info to Allure
        Allure.addAttachment("Environment", ConfigManager.getProperty("environment"));
        Allure.addAttachment("Base URL", ConfigManager.getBaseUrl());

        logger.info("Test suite setup completed");
    }
}