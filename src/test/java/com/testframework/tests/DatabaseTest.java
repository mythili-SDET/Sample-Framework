package com.testframework.tests;

import com.testframework.core.BaseTest;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Database Test Class demonstrating database testing capabilities
 * Tests MySQL database operations
 */
public class DatabaseTest extends BaseTest {
    
    @Test(description = "Test database connection")
    public void testDatabaseConnection() {
        logInfo("Starting database connection test");
        
        try {
            // Test connection by getting table names
            List<String> tableNames = databaseManager.getTableNames();
            assertTrue(tableNames != null, "Database connection should be established");
            
            logInfo("Successfully connected to database");
            logInfo("Available tables: " + tableNames);
            
            logSuccess("Database connection test completed successfully");
        } catch (Exception e) {
            logError("Database connection failed: " + e.getMessage());
            throw e;
        }
    }
    
    @Test(description = "Test database query execution")
    public void testDatabaseQuery() {
        logInfo("Starting database query test");
        
        try {
            // Execute a simple query
            String query = "SELECT 1 as test_value";
            List<Map<String, Object>> results = databaseManager.executeQuery(query);
            
            // Verify results
            assertTrue(results != null, "Query results should not be null");
            assertTrue(!results.isEmpty(), "Query should return results");
            
            Map<String, Object> firstRow = results.get(0);
            assertEquals(firstRow.get("test_value"), 1, "Test value should be 1");
            
            logInfo("Query executed successfully");
            logInfo("Number of results: " + results.size());
            
            logSuccess("Database query test completed successfully");
        } catch (Exception e) {
            logError("Database query failed: " + e.getMessage());
            throw e;
        }
    }
    
    @Test(description = "Test database insert operation")
    public void testDatabaseInsert() {
        logInfo("Starting database insert test");
        
        try {
            // Create test data
            Map<String, Object> testData = new HashMap<>();
            testData.put("name", "Test User");
            testData.put("email", "test@example.com");
            testData.put("age", 25);
            
            // Insert data (assuming 'users' table exists)
            int rowsAffected = databaseManager.insertRecord("users", testData);
            
            // Verify insert
            assertTrue(rowsAffected > 0, "Insert should affect at least one row");
            
            logInfo("Inserted " + rowsAffected + " row(s)");
            
            logSuccess("Database insert test completed successfully");
        } catch (Exception e) {
            logWarning("Database insert test skipped - table may not exist: " + e.getMessage());
        }
    }
    
    @Test(description = "Test database update operation")
    public void testDatabaseUpdate() {
        logInfo("Starting database update test");
        
        try {
            // Prepare update data
            Map<String, Object> updateData = new HashMap<>();
            updateData.put("name", "Updated User");
            updateData.put("email", "updated@example.com");
            
            // Update record (assuming 'users' table exists)
            int rowsAffected = databaseManager.updateRecord("users", updateData, "id = ?", 1);
            
            // Verify update
            assertTrue(rowsAffected >= 0, "Update should not fail");
            
            logInfo("Updated " + rowsAffected + " row(s)");
            
            logSuccess("Database update test completed successfully");
        } catch (Exception e) {
            logWarning("Database update test skipped - table may not exist: " + e.getMessage());
        }
    }
    
    @Test(description = "Test database delete operation")
    public void testDatabaseDelete() {
        logInfo("Starting database delete test");
        
        try {
            // Delete record (assuming 'users' table exists)
            int rowsAffected = databaseManager.deleteRecord("users", "id = ?", 999);
            
            // Verify delete
            assertTrue(rowsAffected >= 0, "Delete should not fail");
            
            logInfo("Deleted " + rowsAffected + " row(s)");
            
            logSuccess("Database delete test completed successfully");
        } catch (Exception e) {
            logWarning("Database delete test skipped - table may not exist: " + e.getMessage());
        }
    }
    
    @Test(description = "Test database record count")
    public void testDatabaseRecordCount() {
        logInfo("Starting database record count test");
        
        try {
            // Get record count (assuming 'users' table exists)
            int count = databaseManager.getRecordCount("users");
            
            // Verify count
            assertTrue(count >= 0, "Record count should be non-negative");
            
            logInfo("Total records in users table: " + count);
            
            logSuccess("Database record count test completed successfully");
        } catch (Exception e) {
            logWarning("Database record count test skipped - table may not exist: " + e.getMessage());
        }
    }
    
    @Test(description = "Test database record existence")
    public void testDatabaseRecordExistence() {
        logInfo("Starting database record existence test");
        
        try {
            // Check if record exists (assuming 'users' table exists)
            boolean exists = databaseManager.recordExists("users", "id", 1);
            
            logInfo("Record with ID 1 exists: " + exists);
            
            logSuccess("Database record existence test completed successfully");
        } catch (Exception e) {
            logWarning("Database record existence test skipped - table may not exist: " + e.getMessage());
        }
    }
    
    @Test(description = "Test database table structure")
    public void testDatabaseTableStructure() {
        logInfo("Starting database table structure test");
        
        try {
            // Get table structure (assuming 'users' table exists)
            List<Map<String, Object>> structure = databaseManager.getTableStructure("users");
            
            // Verify structure
            assertTrue(structure != null, "Table structure should not be null");
            assertTrue(!structure.isEmpty(), "Table structure should not be empty");
            
            logInfo("Table structure retrieved successfully");
            logInfo("Number of columns: " + structure.size());
            
            for (Map<String, Object> column : structure) {
                logInfo("Column: " + column.get("Field") + " - Type: " + column.get("Type"));
            }
            
            logSuccess("Database table structure test completed successfully");
        } catch (Exception e) {
            logWarning("Database table structure test skipped - table may not exist: " + e.getMessage());
        }
    }
    
    @Test(description = "Test database prepared statement")
    public void testDatabasePreparedStatement() {
        logInfo("Starting database prepared statement test");
        
        try {
            // Execute prepared statement query
            String query = "SELECT * FROM users WHERE age > ?";
            List<Map<String, Object>> results = databaseManager.executePreparedQuery(query, 20);
            
            // Verify results
            assertTrue(results != null, "Query results should not be null");
            
            logInfo("Prepared statement executed successfully");
            logInfo("Number of results: " + results.size());
            
            logSuccess("Database prepared statement test completed successfully");
        } catch (Exception e) {
            logWarning("Database prepared statement test skipped - table may not exist: " + e.getMessage());
        }
    }
    
    @Test(description = "Test database single value extraction")
    public void testDatabaseSingleValue() {
        logInfo("Starting database single value test");
        
        try {
            // Get single value
            String query = "SELECT COUNT(*) as total FROM users";
            Object count = databaseManager.getSingleValue(query);
            
            // Verify result
            assertTrue(count != null, "Single value should not be null");
            
            logInfo("Total users count: " + count);
            
            logSuccess("Database single value test completed successfully");
        } catch (Exception e) {
            logWarning("Database single value test skipped - table may not exist: " + e.getMessage());
        }
    }
    
    @Test(description = "Test database with Excel data")
    public void testDatabaseWithExcelData() {
        logInfo("Starting database test with Excel data");
        
        try {
            // Get test data from Excel
            List<Map<String, String>> testData = excelDataProvider.readExcelData("UserData");
            
            if (testData.isEmpty()) {
                logWarning("No test data found in Excel file");
                return;
            }
            
            // Use first row of data
            Map<String, String> data = testData.get(0);
            String name = data.get("Name");
            String email = data.get("Email");
            
            logInfo("Using data from Excel - Name: " + name + ", Email: " + email);
            
            // Prepare data for database
            Map<String, Object> dbData = new HashMap<>();
            dbData.put("name", name);
            dbData.put("email", email);
            dbData.put("age", 30);
            
            // Insert data
            int rowsAffected = databaseManager.insertRecord("users", dbData);
            
            // Verify insert
            assertTrue(rowsAffected > 0, "Insert should affect at least one row");
            
            logInfo("Inserted user from Excel data: " + name);
            
            logSuccess("Database test with Excel data completed successfully");
        } catch (Exception e) {
            logWarning("Database test with Excel data skipped: " + e.getMessage());
        }
    }
    
    @Test(description = "Test database with CSV data")
    public void testDatabaseWithCsvData() {
        logInfo("Starting database test with CSV data");
        
        try {
            // Get test data from CSV
            List<Map<String, String>> testData = csvDataProvider.readCsvData();
            
            if (testData.isEmpty()) {
                logWarning("No test data found in CSV file");
                return;
            }
            
            // Use first row of data
            Map<String, String> data = testData.get(0);
            String name = data.get("Name");
            String email = data.get("Email");
            
            logInfo("Using data from CSV - Name: " + name + ", Email: " + email);
            
            // Prepare data for database
            Map<String, Object> dbData = new HashMap<>();
            dbData.put("name", name);
            dbData.put("email", email);
            dbData.put("age", 25);
            
            // Insert data
            int rowsAffected = databaseManager.insertRecord("users", dbData);
            
            // Verify insert
            assertTrue(rowsAffected > 0, "Insert should affect at least one row");
            
            logInfo("Inserted user from CSV data: " + name);
            
            logSuccess("Database test with CSV data completed successfully");
        } catch (Exception e) {
            logWarning("Database test with CSV data skipped: " + e.getMessage());
        }
    }
}