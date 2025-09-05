package com.automation.tests;

import com.framework.api.clients.ApiClient;
import com.framework.api.model.Employee;
import com.framework.utils.JsonUtils;
import com.framework.api.base.ResponseValidator;
import io.restassured.response.Response;
import org.testng.annotations.Test;
import org.testng.Assert;

import java.util.Map;

public class EmployeeApiTests extends BaseTest{

    private ApiClient apiClient = new ApiClient();

    @Test
    public void testCreateAndSearchEmployee() {
        String token = "Bearer ey...";  // Get token dynamically or hardcoded for test
        Map<String, String> headers = Map.of("Authorization", token);

        // Create new employee object
        Employee employee = new Employee(null, "Alice", "Smith");

        // POST new employee (POJO serialized internally)
        Response responseCreate = apiClient.post("/employees", employee, headers);
        ResponseValidator.validateStatusCode(responseCreate, 201);

        // Deserialize response to Employee object
        Employee createdEmp = JsonUtils.fromJson(responseCreate.asString(), Employee.class);

        // Search the employee by id
        Map<String, String> params = Map.of("id", createdEmp.getId());
        Response responseSearch = apiClient.get("/employees/search", null, params, headers);
        ResponseValidator.validateStatusCode(responseSearch, 200);

        String firstName = ResponseValidator.getValueByJsonPath(responseSearch, "[0].firstName");
        Assert.assertEquals(firstName, "Alice");
    }
}
