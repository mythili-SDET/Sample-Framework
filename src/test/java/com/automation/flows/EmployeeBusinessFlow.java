package com.automation.app.flows;

import com.automation.api.core.clients.ApiClient;
import com.automation.api.core.endpoints.ApiEndpoints;
import com.automation.api.core.models.Employee;
import com.automation.utils.DBUtils;
import com.automation.db.SqlQueries;
import io.restassured.response.Response;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class EmployeeBusinessFlow {

    private final ApiClient apiClient = new ApiClient();
    private Response lastResponse;
    private Employee createdEmployee;
    private final Map<String, Object> cache = new HashMap<>();

    public void createEmployee(Employee employee, String token) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", token);
        lastResponse = apiClient.post(ApiEndpoints.EMPLOYEE_CREATE, employee, headers);
        createdEmployee = lastResponse.as(Employee.class);
        cache.put("lastCreatedEmployee", createdEmployee);
    }

    public void searchEmployeeById(String employeeId, String token) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", token);
        Map<String, Object> query = new HashMap<>();
        query.put("id", employeeId);
        lastResponse = apiClient.get(ApiEndpoints.EMPLOYEE_SEARCH, null, query, headers);
        cache.put("lastSearchResponse", lastResponse);
    }

    public void persistLastEmployeeToDb() {
        if (lastResponse == null) return;
        String json = lastResponse.asString();
        try {
            DBUtils.executeUpdate(SqlQueries.INSERT_EMPLOYEE_CACHE, json);
        } catch (SQLException e) {
            throw new RuntimeException("Failed to persist employee response to DB", e);
        }
    }

    public void writeLastEmployeeToJsonStore() {
        if (lastResponse == null) return;
        String json = lastResponse.asString();
        cache.put("lastEmployeeJson", json);
    }

    public Response getLastResponse() {
        return lastResponse;
    }

    public Employee getCreatedEmployee() {
        return createdEmployee;
    }
}


