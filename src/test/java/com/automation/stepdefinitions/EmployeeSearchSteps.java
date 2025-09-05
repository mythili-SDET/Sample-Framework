package com.automation.stepdefinitions;

import com.framework.api.base.ResponseValidator;
import com.framework.api.model.Employee;
import com.framework.app.flows.EmployeeBusinessFlow;
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
        employeeFlow.searchEmployeeById(token);
    }

    @Then("the employee first name should be {string}")
    public void verifyEmployeeFirstName(String expectedFirstName) {
        var response = employeeFlow.getLastResponse();
        String firstName = ResponseValidator.getValueByJsonPath(response, "[0].firstName");
        Assert.assertEquals(firstName, expectedFirstName);
    }
}
