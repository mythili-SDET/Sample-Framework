package com.automation.stepdefinitions;

import com.automation.core.DatabaseBaseTest;
import com.automation.hooks.TestContext;
import io.cucumber.java.en.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Database Step Definitions for Cucumber scenarios
 * Implements steps for database testing and validation
 */
public class DatabaseStepDefinitions extends DatabaseBaseTest {
    private static final Logger logger = LogManager.getLogger(DatabaseStepDefinitions.class);
    
    private final TestContext testContext;
    private List<Map<String, Object>> queryResults;
    private int updateResult;

    public DatabaseStepDefinitions(TestContext testContext) {
        this.testContext = testContext;
    }

    // Query execution steps
    @When("I execute the query:")
    public void i_execute_the_query(String query) {
        queryResults = executeSelectQuery(query);
        testContext.setTestData("queryResults", queryResults);
        testContext.setTestData("lastQuery", query);
        logger.info("Executed query: {}", query);
    }

    @When("I execute the query {string}")
    public void i_execute_the_query_string(String query) {
        queryResults = executeSelectQuery(query);
        testContext.setTestData("queryResults", queryResults);
        testContext.setTestData("lastQuery", query);
        logger.info("Executed query: {}", query);
    }

    @When("I execute the parameterized query {string} with parameters:")
    public void i_execute_the_parameterized_query_with_parameters(String query, io.cucumber.datatable.DataTable dataTable) {
        List<String> parameters = dataTable.asList();
        Object[] paramArray = parameters.toArray();
        queryResults = executeSelectQuery(query, paramArray);
        testContext.setTestData("queryResults", queryResults);
        testContext.setTestData("lastQuery", query);
        testContext.setTestData("lastParameters", paramArray);
        logger.info("Executed parameterized query: {} with {} parameters", query, parameters.size());
    }

    @When("I execute the update query:")
    public void i_execute_the_update_query(String updateQuery) {
        updateResult = executeUpdateQuery(updateQuery);
        testContext.setTestData("updateResult", updateResult);
        testContext.setTestData("lastUpdateQuery", updateQuery);
        logger.info("Executed update query: {}", updateQuery);
    }

    @When("I execute the update query {string}")
    public void i_execute_the_update_query_string(String updateQuery) {
        updateResult = executeUpdateQuery(updateQuery);
        testContext.setTestData("updateResult", updateResult);
        testContext.setTestData("lastUpdateQuery", updateQuery);
        logger.info("Executed update query: {}", updateQuery);
    }

    @When("I execute the parameterized update query {string} with parameters:")
    public void i_execute_the_parameterized_update_query_with_parameters(String updateQuery, io.cucumber.datatable.DataTable dataTable) {
        List<String> parameters = dataTable.asList();
        Object[] paramArray = parameters.toArray();
        updateResult = executeUpdateQuery(updateQuery, paramArray);
        testContext.setTestData("updateResult", updateResult);
        testContext.setTestData("lastUpdateQuery", updateQuery);
        testContext.setTestData("lastParameters", paramArray);
        logger.info("Executed parameterized update query: {} with {} parameters", updateQuery, parameters.size());
    }

    @When("I execute the stored procedure {string}")
    public void i_execute_the_stored_procedure(String procedureName) {
        queryResults = executeStoredProcedure(procedureName);
        testContext.setTestData("queryResults", queryResults);
        testContext.setTestData("lastProcedure", procedureName);
        logger.info("Executed stored procedure: {}", procedureName);
    }

    @When("I execute the stored procedure {string} with parameters:")
    public void i_execute_the_stored_procedure_with_parameters(String procedureName, io.cucumber.datatable.DataTable dataTable) {
        List<String> parameters = dataTable.asList();
        Object[] paramArray = parameters.toArray();
        queryResults = executeStoredProcedure(procedureName, paramArray);
        testContext.setTestData("queryResults", queryResults);
        testContext.setTestData("lastProcedure", procedureName);
        testContext.setTestData("lastParameters", paramArray);
        logger.info("Executed stored procedure: {} with {} parameters", procedureName, parameters.size());
    }

    // Result validation steps
    @Then("the query should return {int} row(s)")
    public void the_query_should_return_rows(int expectedRowCount) {
        Assert.assertEquals(queryResults.size(), expectedRowCount, 
            "Query returned " + queryResults.size() + " rows, expected " + expectedRowCount);
        logger.info("Validated query returned {} rows", expectedRowCount);
    }

    @Then("the query should return at least {int} row(s)")
    public void the_query_should_return_at_least_rows(int minRowCount) {
        Assert.assertTrue(queryResults.size() >= minRowCount, 
            "Query returned " + queryResults.size() + " rows, expected at least " + minRowCount);
        logger.info("Validated query returned at least {} rows", minRowCount);
    }

    @Then("the query should return no rows")
    public void the_query_should_return_no_rows() {
        Assert.assertTrue(queryResults.isEmpty(), 
            "Query returned " + queryResults.size() + " rows, expected 0");
        logger.info("Validated query returned no rows");
    }

    @Then("the first row should contain:")
    public void the_first_row_should_contain(io.cucumber.datatable.DataTable dataTable) {
        Assert.assertFalse(queryResults.isEmpty(), "No rows returned from query");
        
        Map<String, String> expectedData = dataTable.asMap(String.class, String.class);
        Map<String, Object> actualRow = queryResults.get(0);
        
        for (Map.Entry<String, String> entry : expectedData.entrySet()) {
            String column = entry.getKey();
            String expectedValue = entry.getValue();
            Object actualValue = actualRow.get(column);
            
            Assert.assertNotNull(actualValue, "Column " + column + " not found in result");
            Assert.assertEquals(actualValue.toString(), expectedValue, 
                "Column " + column + " value mismatch");
        }
        
        // Store first row data in context
        testContext.setTestData("firstRowData", actualRow);
        logger.info("Validated first row contains expected data");
    }

    @Then("the column {string} in first row should be {string}")
    public void the_column_in_first_row_should_be(String columnName, String expectedValue) {
        Assert.assertFalse(queryResults.isEmpty(), "No rows returned from query");
        
        Map<String, Object> firstRow = queryResults.get(0);
        Object actualValue = firstRow.get(columnName);
        
        Assert.assertNotNull(actualValue, "Column " + columnName + " not found in result");
        Assert.assertEquals(actualValue.toString(), expectedValue, 
            "Column " + columnName + " value mismatch");
        
        testContext.setTestData("extracted_" + columnName, actualValue);
        logger.info("Validated column {} = {}", columnName, expectedValue);
    }

    @Then("the column {string} in first row should be number {int}")
    public void the_column_in_first_row_should_be_number(String columnName, int expectedValue) {
        Assert.assertFalse(queryResults.isEmpty(), "No rows returned from query");
        
        Map<String, Object> firstRow = queryResults.get(0);
        Object actualValue = firstRow.get(columnName);
        
        Assert.assertNotNull(actualValue, "Column " + columnName + " not found in result");
        Assert.assertEquals(((Number) actualValue).intValue(), expectedValue, 
            "Column " + columnName + " value mismatch");
        
        testContext.setTestData("extracted_" + columnName, actualValue);
        logger.info("Validated column {} = {}", columnName, expectedValue);
    }

    @Then("the update should affect {int} row(s)")
    public void the_update_should_affect_rows(int expectedAffectedRows) {
        Assert.assertEquals(updateResult, expectedAffectedRows, 
            "Update affected " + updateResult + " rows, expected " + expectedAffectedRows);
        logger.info("Validated update affected {} rows", expectedAffectedRows);
    }

    @Then("the update should affect at least {int} row(s)")
    public void the_update_should_affect_at_least_rows(int minAffectedRows) {
        Assert.assertTrue(updateResult >= minAffectedRows, 
            "Update affected " + updateResult + " rows, expected at least " + minAffectedRows);
        logger.info("Validated update affected at least {} rows", minAffectedRows);
    }

    // Data validation steps
    @Then("the table {string} should contain {int} row(s)")
    public void the_table_should_contain_rows(String tableName, int expectedRowCount) {
        int actualRowCount = getRowCount(tableName);
        Assert.assertEquals(actualRowCount, expectedRowCount, 
            "Table " + tableName + " contains " + actualRowCount + " rows, expected " + expectedRowCount);
        testContext.setTestData("tableRowCount_" + tableName, actualRowCount);
        logger.info("Validated table {} contains {} rows", tableName, expectedRowCount);
    }

    @Then("the table {string} should contain at least {int} row(s)")
    public void the_table_should_contain_at_least_rows(String tableName, int minRowCount) {
        int actualRowCount = getRowCount(tableName);
        Assert.assertTrue(actualRowCount >= minRowCount, 
            "Table " + tableName + " contains " + actualRowCount + " rows, expected at least " + minRowCount);
        testContext.setTestData("tableRowCount_" + tableName, actualRowCount);
        logger.info("Validated table {} contains at least {} rows", tableName, minRowCount);
    }

    @Then("the table {string} should be empty")
    public void the_table_should_be_empty(String tableName) {
        int actualRowCount = getRowCount(tableName);
        Assert.assertEquals(actualRowCount, 0, 
            "Table " + tableName + " contains " + actualRowCount + " rows, expected 0");
        testContext.setTestData("tableRowCount_" + tableName, actualRowCount);
        logger.info("Validated table {} is empty", tableName);
    }

    @Then("a record should exist in table {string} where {string}")
    public void a_record_should_exist_in_table_where(String tableName, String condition) {
        boolean exists = recordExists(tableName, condition);
        Assert.assertTrue(exists, "No record found in table " + tableName + " where " + condition);
        logger.info("Validated record exists in table {} where {}", tableName, condition);
    }

    @Then("no record should exist in table {string} where {string}")
    public void no_record_should_exist_in_table_where(String tableName, String condition) {
        boolean exists = recordExists(tableName, condition);
        Assert.assertFalse(exists, "Record found in table " + tableName + " where " + condition + " when none expected");
        logger.info("Validated no record exists in table {} where {}", tableName, condition);
    }

    @Then("the data in table {string} where {string} should be:")
    public void the_data_in_table_where_should_be(String tableName, String condition, io.cucumber.datatable.DataTable dataTable) {
        Map<String, String> expectedData = dataTable.asMap(String.class, String.class);
        Map<String, Object> expectedDataObjects = new HashMap<>();
        
        for (Map.Entry<String, String> entry : expectedData.entrySet()) {
            expectedDataObjects.put(entry.getKey(), entry.getValue());
        }
        
        boolean isValid = validateData(tableName, expectedDataObjects, condition);
        Assert.assertTrue(isValid, "Data validation failed for table " + tableName + " where " + condition);
        logger.info("Validated data in table {} where {}", tableName, condition);
    }

    // Transaction steps
    @Given("I begin a database transaction")
    public void i_begin_a_database_transaction() {
        beginTransaction();
        testContext.setTestData("transactionStarted", true);
        logger.info("Database transaction started");
    }

    @When("I commit the transaction")
    public void i_commit_the_transaction() {
        commitTransaction();
        testContext.setTestData("transactionStarted", false);
        logger.info("Database transaction committed");
    }

    @When("I rollback the transaction")
    public void i_rollback_the_transaction() {
        rollbackTransaction();
        testContext.setTestData("transactionStarted", false);
        logger.info("Database transaction rolled back");
    }

    // Utility steps
    @When("I extract column {string} from first row and store as {string}")
    public void i_extract_column_from_first_row_and_store_as(String columnName, String variableName) {
        Assert.assertFalse(queryResults.isEmpty(), "No rows returned from query");
        
        Map<String, Object> firstRow = queryResults.get(0);
        Object value = firstRow.get(columnName);
        
        Assert.assertNotNull(value, "Column " + columnName + " not found in result");
        
        testContext.setTestData(variableName, value);
        testContext.setSessionData(variableName, value);
        logger.info("Extracted column {} from first row and stored as {}: {}", columnName, variableName, value);
    }

    @When("I get single value from query {string} column {string} and store as {string}")
    public void i_get_single_value_from_query_column_and_store_as(String query, String columnName, String variableName) {
        Object value = getSingleValue(query, columnName);
        testContext.setTestData(variableName, value);
        testContext.setSessionData(variableName, value);
        logger.info("Got single value from query column {} and stored as {}: {}", columnName, variableName, value);
    }

    @Then("I print the query results")
    public void i_print_the_query_results() {
        logger.info("Query results: {}", queryResults);
        System.out.println("Query results:");
        for (int i = 0; i < queryResults.size(); i++) {
            System.out.println("Row " + (i + 1) + ": " + queryResults.get(i));
        }
    }

    @Then("I print the row count for table {string}")
    public void i_print_the_row_count_for_table(String tableName) {
        int rowCount = getRowCount(tableName);
        logger.info("Table {} row count: {}", tableName, rowCount);
        System.out.println("Table " + tableName + " row count: " + rowCount);
    }
}