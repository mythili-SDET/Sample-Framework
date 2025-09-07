package com.automation.stepdefinitions;

import com.automation.api.core.base.ResponseValidator;
import com.automation.api.core.models.Employee;
import com.automation.app.flows.EmployeeBusinessFlow;
import io.cucumber.java.en.*;
import org.testng.Assert;

public class EmployeeSearchSteps {

    private EmployeeBusinessFlow employeeFlow = new EmployeeBusinessFlow();

    private String token = "Bearer eyYourAuthTokenHere";  // Replace with dynamic or config token

    @When("I create an employee with first name {string} and last name {string}")
    public void iCreateEmployee(String firstName, String lastName) {
        Employee employee = new Employee(null, firstName, lastName);
        employeeFlow.createEmployee(employee, token);
    }

    @Then("the employee is created successfully")
    public void verifyEmployeeCreated() {
        Employee created = employeeFlow.getCreatedEmployee();
        Assert.assertNotNull(created.getId(), "Created employee ID should not be null");
    }

    @When("I search the employee by ID")
    public void searchEmployeeById() {
        Employee created = employeeFlow.getCreatedEmployee();
        employeeFlow.searchEmployeeById(created.getId(), token);
        employeeFlow.persistLastEmployeeToDb();
        employeeFlow.writeLastEmployeeToJsonStore();
    }

    @Then("the employee first name should be {string}")
    public void verifyEmployeeFirstName(String expectedFirstName) {
        var response = employeeFlow.getLastResponse();
        String firstName = ResponseValidator.getValueByJsonPath(response, "[0].firstName");
        Assert.assertEquals(firstName, expectedFirstName);
    }
    private String key, value;

    @Given("I search for employee using {string} with value {string}")
    public void searchEmployeeWithKeyValue(String key, String value) {
        this.key = key;
        this.value = value;

        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put(key, value);

        response = apiClient.get("/employees/search", null, queryParams, headers);
    }

    @Then("the response status code should be {int}")
    public void verifyStatusCode(int expectedCode) {
        assertEquals(response.getStatusCode(), expectedCode);
    }

    @Then("the response should contain {string}")
public void responseShouldContainValue(String expectedValue) {
    String responseBody = response.getBody().asString();
    assertTrue(responseBody.contains(expectedValue), 
        "Response does not contain expected value: " + expectedValue);
}


}
