package com.automation.database;

import com.automation.database.DatabaseManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;

/**
 * Sample database test class demonstrating user database operations
 * Shows CRUD operations, transactions, and data validation
 */
public class UserDatabaseTest {
    private static final Logger logger = LogManager.getLogger(UserDatabaseTest.class);
    
    private DatabaseManager dbManager;
    private String testUserId;

    @BeforeClass
    public void setupDatabase() {
        logger.info("Setting up database connection for tests");
        
        // Initialize database manager with MySQL (you can change to "postgres" if needed)
        dbManager = new DatabaseManager("mysql");
        
        // Create test table if it doesn't exist
        String createTableSQL = """
            CREATE TABLE IF NOT EXISTS users (
                id INT AUTO_INCREMENT PRIMARY KEY,
                name VARCHAR(255) NOT NULL,
                email VARCHAR(255) UNIQUE NOT NULL,
                age INT,
                role VARCHAR(50),
                status VARCHAR(20) DEFAULT 'active',
                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
            )
        """;
        
        dbManager.executeSQLScript(createTableSQL);
        dbManager.commit();
        
        logger.info("Database setup completed");
    }

    @AfterClass
    public void teardownDatabase() {
        logger.info("Cleaning up database after tests");
        
        // Clean up test data
        if (testUserId != null) {
            dbManager.executeUpdate("DELETE FROM users WHERE id = ?", testUserId);
            dbManager.commit();
        }
        
        // Close database connection
        dbManager.closeConnection();
        
        logger.info("Database cleanup completed");
    }

    @Test(groups = {"smoke", "database"}, description = "Test database connection")
    public void testDatabaseConnection() {
        logger.info("Testing database connection");
        
        Assert.assertTrue(dbManager.isConnectionValid(), "Database connection should be valid");
        
        // Test simple query
        List<Map<String, Object>> result = dbManager.executeQuery("SELECT 1 as test_value");
        Assert.assertFalse(result.isEmpty(), "Test query should return result");
        Assert.assertEquals(result.get(0).get("test_value"), 1, "Test query should return correct value");
        
        logger.info("Database connection test completed successfully");
    }

    @Test(groups = {"database"}, description = "Test create user in database")
    public void testCreateUser() {
        logger.info("Testing user creation in database");
        
        String insertSQL = "INSERT INTO users (name, email, age, role, status) VALUES (?, ?, ?, ?, ?)";
        int affectedRows = dbManager.executeUpdate(insertSQL, 
                                                  "Test User", 
                                                  "test.user@example.com", 
                                                  25, 
                                                  "user", 
                                                  "active");
        
        Assert.assertEquals(affectedRows, 1, "One row should be affected during user creation");
        
        // Get the created user ID
        Object userId = dbManager.getSingleValue("SELECT LAST_INSERT_ID()");
        Assert.assertNotNull(userId, "Created user should have an ID");
        testUserId = userId.toString();
        
        // Verify user was created correctly
        List<Map<String, Object>> createdUser = dbManager.executeQuery(
            "SELECT * FROM users WHERE id = ?", testUserId);
        
        Assert.assertFalse(createdUser.isEmpty(), "Created user should be found in database");
        Map<String, Object> user = createdUser.get(0);
        
        Assert.assertEquals(user.get("name"), "Test User");
        Assert.assertEquals(user.get("email"), "test.user@example.com");
        Assert.assertEquals(user.get("age"), 25);
        Assert.assertEquals(user.get("role"), "user");
        Assert.assertEquals(user.get("status"), "active");
        
        dbManager.commit();
        logger.info("User creation test completed successfully");
    }

    @Test(dependsOnMethods = "testCreateUser", groups = {"database"}, 
          description = "Test read user from database")
    public void testReadUser() {
        logger.info("Testing user retrieval from database");
        
        // Read user by ID
        List<Map<String, Object>> users = dbManager.executeQuery(
            "SELECT * FROM users WHERE id = ?", testUserId);
        
        Assert.assertFalse(users.isEmpty(), "User should be found");
        Map<String, Object> user = users.get(0);
        
        Assert.assertEquals(user.get("name"), "Test User");
        Assert.assertEquals(user.get("email"), "test.user@example.com");
        
        // Read user by email
        List<Map<String, Object>> usersByEmail = dbManager.executeQuery(
            "SELECT * FROM users WHERE email = ?", "test.user@example.com");
        
        Assert.assertFalse(usersByEmail.isEmpty(), "User should be found by email");
        Assert.assertEquals(usersByEmail.get(0).get("id").toString(), testUserId);
        
        logger.info("User retrieval test completed successfully");
    }

    @Test(dependsOnMethods = "testReadUser", groups = {"database"}, 
          description = "Test update user in database")
    public void testUpdateUser() {
        logger.info("Testing user update in database");
        
        String updateSQL = "UPDATE users SET name = ?, age = ?, status = ? WHERE id = ?";
        int affectedRows = dbManager.executeUpdate(updateSQL, 
                                                  "Updated Test User", 
                                                  30, 
                                                  "inactive", 
                                                  testUserId);
        
        Assert.assertEquals(affectedRows, 1, "One row should be affected during user update");
        
        // Verify user was updated correctly
        List<Map<String, Object>> updatedUser = dbManager.executeQuery(
            "SELECT * FROM users WHERE id = ?", testUserId);
        
        Assert.assertFalse(updatedUser.isEmpty(), "Updated user should be found");
        Map<String, Object> user = updatedUser.get(0);
        
        Assert.assertEquals(user.get("name"), "Updated Test User");
        Assert.assertEquals(user.get("age"), 30);
        Assert.assertEquals(user.get("status"), "inactive");
        Assert.assertEquals(user.get("email"), "test.user@example.com"); // Should remain unchanged
        
        dbManager.commit();
        logger.info("User update test completed successfully");
    }

    @Test(groups = {"database"}, description = "Test batch user operations")
    public void testBatchOperations() {
        logger.info("Testing batch user operations");
        
        // Prepare batch data
        List<Object[]> batchData = List.of(
            new Object[]{"Batch User 1", "batch1@example.com", 22, "user", "active"},
            new Object[]{"Batch User 2", "batch2@example.com", 28, "admin", "active"},
            new Object[]{"Batch User 3", "batch3@example.com", 35, "user", "inactive"}
        );
        
        String batchSQL = "INSERT INTO users (name, email, age, role, status) VALUES (?, ?, ?, ?, ?)";
        int[] results = dbManager.executeBatch(batchSQL, batchData);
        
        Assert.assertEquals(results.length, 3, "Batch should process 3 operations");
        
        // Verify all users were created
        long userCount = dbManager.getRowCount("users", "email LIKE ?", "batch%@example.com");
        Assert.assertEquals(userCount, 3, "Three batch users should be created");
        
        // Clean up batch test data
        dbManager.executeUpdate("DELETE FROM users WHERE email LIKE ?", "batch%@example.com");
        dbManager.commit();
        
        logger.info("Batch operations test completed successfully");
    }

    @Test(groups = {"database"}, description = "Test transaction rollback")
    public void testTransactionRollback() {
        logger.info("Testing transaction rollback");
        
        // Get initial user count
        long initialCount = dbManager.getRowCount("users", null);
        
        try {
            // Insert user
            dbManager.executeUpdate("INSERT INTO users (name, email, age, role) VALUES (?, ?, ?, ?)",
                                   "Rollback User", "rollback@example.com", 25, "user");
            
            // Verify user was inserted
            long countAfterInsert = dbManager.getRowCount("users", null);
            Assert.assertEquals(countAfterInsert, initialCount + 1, "User count should increase by 1");
            
            // Simulate error and rollback
            dbManager.rollback();
            
            // Verify user was rolled back
            long countAfterRollback = dbManager.getRowCount("users", null);
            Assert.assertEquals(countAfterRollback, initialCount, "User count should return to initial value");
            
        } catch (Exception e) {
            dbManager.rollback();
            throw e;
        }
        
        logger.info("Transaction rollback test completed successfully");
    }

    @Test(groups = {"database"}, description = "Test data validation constraints")
    public void testDataValidationConstraints() {
        logger.info("Testing database constraints and validation");
        
        // Test unique constraint violation
        try {
            // First insert
            dbManager.executeUpdate("INSERT INTO users (name, email, age, role) VALUES (?, ?, ?, ?)",
                                   "Unique Test 1", "unique@example.com", 25, "user");
            
            // Second insert with same email (should fail)
            boolean exceptionThrown = false;
            try {
                dbManager.executeUpdate("INSERT INTO users (name, email, age, role) VALUES (?, ?, ?, ?)",
                                       "Unique Test 2", "unique@example.com", 30, "admin");
                dbManager.commit();
            } catch (Exception e) {
                exceptionThrown = true;
                dbManager.rollback();
                logger.info("Expected unique constraint violation occurred: {}", e.getMessage());
            }
            
            Assert.assertTrue(exceptionThrown, "Unique constraint violation should throw exception");
            
            // Clean up
            dbManager.executeUpdate("DELETE FROM users WHERE email = ?", "unique@example.com");
            dbManager.commit();
            
        } catch (Exception e) {
            dbManager.rollback();
            logger.error("Error in constraint test", e);
        }
        
        logger.info("Data validation constraints test completed");
    }

    @Test(groups = {"database"}, description = "Test complex queries and aggregations")
    public void testComplexQueries() {
        logger.info("Testing complex queries and aggregations");
        
        // Insert test data for aggregation
        dbManager.executeUpdate("INSERT INTO users (name, email, age, role, status) VALUES (?, ?, ?, ?, ?)",
                               "Agg User 1", "agg1@example.com", 25, "user", "active");
        dbManager.executeUpdate("INSERT INTO users (name, email, age, role, status) VALUES (?, ?, ?, ?, ?)",
                               "Agg User 2", "agg2@example.com", 30, "admin", "active");
        dbManager.executeUpdate("INSERT INTO users (name, email, age, role, status) VALUES (?, ?, ?, ?, ?)",
                               "Agg User 3", "agg3@example.com", 35, "user", "inactive");
        dbManager.commit();
        
        // Test aggregation query
        List<Map<String, Object>> aggResults = dbManager.executeQuery(
            "SELECT role, COUNT(*) as user_count, AVG(age) as avg_age FROM users WHERE email LIKE ? GROUP BY role",
            "agg%@example.com");
        
        Assert.assertFalse(aggResults.isEmpty(), "Aggregation query should return results");
        
        // Test filtering and sorting
        List<Map<String, Object>> filteredResults = dbManager.executeQuery(
            "SELECT * FROM users WHERE age > ? AND status = ? ORDER BY age DESC",
            20, "active");
        
        Assert.assertFalse(filteredResults.isEmpty(), "Filtered query should return results");
        
        // Verify sorting
        if (filteredResults.size() > 1) {
            int firstAge = (Integer) filteredResults.get(0).get("age");
            int secondAge = (Integer) filteredResults.get(1).get("age");
            Assert.assertTrue(firstAge >= secondAge, "Results should be sorted by age in descending order");
        }
        
        // Clean up test data
        dbManager.executeUpdate("DELETE FROM users WHERE email LIKE ?", "agg%@example.com");
        dbManager.commit();
        
        logger.info("Complex queries test completed successfully");
    }

    @Test(groups = {"database"}, description = "Test database metadata operations")
    public void testDatabaseMetadata() {
        logger.info("Testing database metadata operations");
        
        // Get table columns
        List<String> columns = dbManager.getTableColumns("users");
        Assert.assertFalse(columns.isEmpty(), "Table should have columns");
        Assert.assertTrue(columns.contains("id"), "Table should have id column");
        Assert.assertTrue(columns.contains("name"), "Table should have name column");
        Assert.assertTrue(columns.contains("email"), "Table should have email column");
        
        logger.info("Found table columns: {}", columns);
        
        // Test record existence
        boolean userExists = dbManager.recordExists("SELECT 1 FROM users WHERE id = ?", testUserId);
        Assert.assertTrue(userExists, "Test user should exist");
        
        boolean nonExistentUser = dbManager.recordExists("SELECT 1 FROM users WHERE id = ?", 99999);
        Assert.assertFalse(nonExistentUser, "Non-existent user should not be found");
        
        logger.info("Database metadata test completed successfully");
    }

    @Test(dependsOnMethods = "testUpdateUser", groups = {"database"}, 
          description = "Test delete user from database")
    public void testDeleteUser() {
        logger.info("Testing user deletion from database");
        
        // Verify user exists before deletion
        boolean userExists = dbManager.recordExists("SELECT 1 FROM users WHERE id = ?", testUserId);
        Assert.assertTrue(userExists, "User should exist before deletion");
        
        String deleteSQL = "DELETE FROM users WHERE id = ?";
        int affectedRows = dbManager.executeUpdate(deleteSQL, testUserId);
        
        Assert.assertEquals(affectedRows, 1, "One row should be affected during user deletion");
        
        // Verify user was deleted
        boolean userExistsAfterDelete = dbManager.recordExists("SELECT 1 FROM users WHERE id = ?", testUserId);
        Assert.assertFalse(userExistsAfterDelete, "User should not exist after deletion");
        
        dbManager.commit();
        testUserId = null; // Reset for cleanup
        
        logger.info("User deletion test completed successfully");
    }
}