package com.automation.hooks;

import com.automation.config.ConfigManager;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.cucumber.java.AfterStep;
import io.cucumber.java.BeforeStep;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import com.automation.driver.DriverManager;
import io.cucumber.java.Scenario;
import io.qameta.allure.*;
import io.restassured.RestAssured;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TestHooks {
    private static final Logger logger = LogManager.getLogger(TestHooks.class);

    @Before
    @Step("Setup test environment for scenario")
    public void setUp(Scenario scenario) {
        logger.info("Starting scenario: {}", scenario.getName());

        // Add scenario information to Allure report
        Allure.getLifecycle().updateTestCase(testResult -> {
            testResult.setName(scenario.getName());
            testResult.setDescription("Cucumber Scenario: " + scenario.getName());
        });

        // Set base configuration
        RestAssured.baseURI = ConfigManager.getInstance().getAPIBaseUrl();
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();

        // Add environment information
        Allure.addAttachment("Base URI", ConfigManager.getInstance().getAPIBaseUrl());
        Allure.addAttachment("Environment", System.getProperty("env", "qa"));

        // Add scenario tags to report
        for (String tag : scenario.getSourceTagNames()) {
            Allure.label("tag", tag.replace("@", ""));
        }
    }

    @After
    @Step("Cleanup test environment for scenario")
    public void tearDown(Scenario scenario) {
        if (scenario.isFailed()) {
            logger.error("Scenario failed: {}", scenario.getName());

            // Add failure information to Allure report
            Allure.addAttachment("Failure Information", 
                               "Scenario: " + scenario.getName() + "\nStatus: FAILED");

        } else {
            logger.info("Scenario passed: {}", scenario.getName());

            Allure.addAttachment("Success Information", 
                               "Scenario: " + scenario.getName() + "\nStatus: PASSED");
        }

        // Clean up resources
        RestAssured.reset();

        logger.info("Cleanup completed for scenario: {}", scenario.getName());
    }

    @BeforeStep
    public void beforeStep(Scenario scenario) {
        Allure.step("Starting step: " + scenario.getName());
    }

    @AfterStep
    public void afterStep(Scenario scenario) {
        WebDriver driver = DriverManager.getDriver();
        if (driver != null) {
            byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
            Allure.getLifecycle().addAttachment("Step Screenshot", "image/png", "png", screenshot);
        }
    }
}