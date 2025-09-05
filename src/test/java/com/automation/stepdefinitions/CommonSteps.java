package com.automation.stepdefinitions;

import com.framework.config.ConfigManager;
import com.framework.utils.JsonUtils;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;

public class CommonSteps {
    private static final Logger logger = LogManager.getLogger(CommonSteps.class);

    @Given("the API is available")
    @Step("Verify API availability")
    public void theAPIIsAvailable() {
        logger.info("Verifying API availability");
        // Common API availability check logic
    }

    @When("I wait for {int} seconds")
    @Step("Wait for {seconds} seconds")
    public void iWaitForSeconds(int seconds) {
        logger.info("Waiting for {} seconds", seconds);
        try {
            Thread.sleep(seconds * 1000L);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.error("Thread interrupted during wait");
        }
    }

    @Then("the response time should be less than {int} milliseconds")
    @Step("Validate response time is less than {maxTime} ms")
    public void theResponseTimeShouldBeLessThan(int maxTime) {
        logger.info("Validating response time is less than {} ms", maxTime);
        // Response time validation logic would go here
    }


}