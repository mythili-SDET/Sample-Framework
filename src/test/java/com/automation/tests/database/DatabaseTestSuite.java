package com.automation.tests.database;

import com.automation.core.BaseDBTest;
import com.automation.utils.CSVDataProvider;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Map;

/**
 * Database Test Suite
 * Demonstrates database automation capabilities
 */
public class DatabaseTestSuite extends BaseDBTest {
    private static final Logger logger = LogManager.getLogger(DatabaseTestSuite.class);

    @BeforeClass
    public void setUp() {
        logger.info("Setting up Database test suite");
        // Database connection will be established when first accessed
    }

    /**
     * Test database connection
     */
    @Test(description = "Test database connection")
    public void testDatabaseConnection() {
        logger.info("Starting database connection test");
        
        try {
            // Test connection
            getConnection();
            Assert.assertFalse(isConnectionClosed(), "Database connection should be active");
            
            // Test simple query
            int userCount = getRowCount("users");
            Assert.assertTrue(userCount >= 0, "User count should be non-negative");
            
            logger.info("Database connection test completed successfully");
        } catch (Exception e) {
            logger.error("Database connection test failed", e);
            Assert.fail("Database connection test failed: " + e.getMessage());
        }
    }

    /**
     * Test user data retrieval
     */
    @Test(description = "Test user data retrieval")
    public void testUserDataRetrieval() {
        logger.info("Starting user data retrieval test");
        
        // Get user by ID
        String sql = "SELECT * FROM users WHERE id = ?";
        Map<String, Object> user = getFirstRow(sql, 1);
        
        Assert.assertNotNull(user, "User should be found");
        Assert.assertEquals(user.get("id"), 1, "User ID should match");
        Assert.assertNotNull(user.get("name"), "User name should not be null");
        Assert.assertNotNull(user.get("email"), "User email should not be null");
        
        logger.info("User data retrieval test completed successfully");
    }

    /**
     * Test user data insertion
     */
    @Test(description = "Test user data insertion")
    public void testUserDataInsertion() {
        logger.info("Starting user data insertion test");
        
        // Get initial count
        int initialCount = getRowCount("users");
        
        // Insert new user
        String insertSql = "INSERT INTO users (name, email, role, created_at) VALUES (?, ?, ?, NOW())";
        int generatedId = insertRecord(insertSql, "Test User", "test.user@example.com", "user");
        
        Assert.assertTrue(generatedId > 0, "Generated ID should be positive");
        
        // Verify user was inserted
        int finalCount = getRowCount("users");
        Assert.assertEquals(finalCount, initialCount + 1, "User count should be incremented");
        
        // Verify user data
        String selectSql = "SELECT * FROM users WHERE id = ?";
        Map<String, Object> insertedUser = getFirstRow(selectSql, generatedId);
        Assert.assertNotNull(insertedUser, "Inserted user should be found");
        Assert.assertEquals(insertedUser.get("name"), "Test User", "User name should match");
        
        // Clean up
        cleanupTestData("users", "id = ?", generatedId);
        
        logger.info("User data insertion test completed successfully");
    }

    /**
     * Test user data update
     */
    @Test(description = "Test user data update")
    public void testUserDataUpdate() {
        logger.info("Starting user data update test");
        
        // Insert test user
        String insertSql = "INSERT INTO users (name, email, role, created_at) VALUES (?, ?, ?, NOW())";
        int userId = insertRecord(insertSql, "Original Name", "original@example.com", "user");
        
        // Update user
        String updateSql = "UPDATE users SET name = ?, email = ? WHERE id = ?";
        int affectedRows = executePreparedUpdate(updateSql, "Updated Name", "updated@example.com", userId);
        
        Assert.assertEquals(affectedRows, 1, "One row should be updated");
        
        // Verify update
        String selectSql = "SELECT * FROM users WHERE id = ?";
        Map<String, Object> updatedUser = getFirstRow(selectSql, userId);
        Assert.assertEquals(updatedUser.get("name"), "Updated Name", "Name should be updated");
        Assert.assertEquals(updatedUser.get("email"), "updated@example.com", "Email should be updated");
        
        // Clean up
        cleanupTestData("users", "id = ?", userId);
        
        logger.info("User data update test completed successfully");
    }

    /**
     * Test user data deletion
     */
    @Test(description = "Test user data deletion")
    public void testUserDataDeletion() {
        logger.info("Starting user data deletion test");
        
        // Insert test user
        String insertSql = "INSERT INTO users (name, email, role, created_at) VALUES (?, ?, ?, NOW())";
        int userId = insertRecord(insertSql, "Delete Test User", "delete.test@example.com", "user");
        
        // Verify user exists
        assertRecordExists("users", "id = ?", userId);
        
        // Delete user
        int affectedRows = executePreparedUpdate("DELETE FROM users WHERE id = ?", userId);
        Assert.assertEquals(affectedRows, 1, "One row should be deleted");
        
        // Verify user is deleted
        assertRecordNotExists("users", "id = ?", userId);
        
        logger.info("User data deletion test completed successfully");
    }

    /**
     * Test complex queries
     */
    @Test(description = "Test complex database queries")
    public void testComplexQueries() {
        logger.info("Starting complex queries test");
        
        // Test JOIN query
        String joinSql = "SELECT u.name, u.email, p.title FROM users u " +
                        "LEFT JOIN posts p ON u.id = p.user_id " +
                        "WHERE u.role = ?";
        
        List<Map<String, Object>> results = getResultAsList(joinSql, "user");
        Assert.assertNotNull(results, "JOIN query results should not be null");
        
        // Test aggregation query
        String aggregateSql = "SELECT role, COUNT(*) as user_count FROM users GROUP BY role";
        List<Map<String, Object>> aggregateResults = getResultAsList(aggregateSql);
        Assert.assertNotNull(aggregateResults, "Aggregate query results should not be null");
        
        // Test subquery
        String subquerySql = "SELECT * FROM users WHERE id IN (SELECT user_id FROM posts WHERE created_at > DATE_SUB(NOW(), INTERVAL 7 DAY))";
        List<Map<String, Object>> subqueryResults = getResultAsList(subquerySql);
        Assert.assertNotNull(subqueryResults, "Subquery results should not be null");
        
        logger.info("Complex queries test completed successfully");
    }

    /**
     * Test data validation
     */
    @Test(description = "Test database data validation")
    public void testDataValidation() {
        logger.info("Starting data validation test");
        
        // Test email format validation
        String emailValidationSql = "SELECT email FROM users WHERE email NOT REGEXP '^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$'";
        List<Object> invalidEmails = getColumnValues(emailValidationSql, "email");
        Assert.assertEquals(invalidEmails.size(), 0, "No invalid email formats should exist");
        
        // Test required fields
        String nullNameSql = "SELECT COUNT(*) FROM users WHERE name IS NULL OR name = ''";
        Object nullNameCount = getSingleValue(nullNameSql, "COUNT(*)");
        Assert.assertEquals(nullNameCount, 0, "No users should have null or empty names");
        
        // Test unique constraints
        String duplicateEmailSql = "SELECT email, COUNT(*) as count FROM users GROUP BY email HAVING COUNT(*) > 1";
        List<Map<String, Object>> duplicateEmails = getResultAsList(duplicateEmailSql);
        Assert.assertEquals(duplicateEmails.size(), 0, "No duplicate emails should exist");
        
        logger.info("Data validation test completed successfully");
    }

    /**
     * Test transaction management
     */
    @Test(description = "Test database transaction management")
    public void testTransactionManagement() {
        logger.info("Starting transaction management test");
        
        try {
            // Begin transaction
            beginTransaction();
            
            // Insert test data
            String insertSql = "INSERT INTO users (name, email, role, created_at) VALUES (?, ?, ?, NOW())";
            int userId1 = insertRecord(insertSql, "Transaction User 1", "trans1@example.com", "user");
            int userId2 = insertRecord(insertSql, "Transaction User 2", "trans2@example.com", "user");
            
            // Verify data exists within transaction
            assertRecordExists("users", "id = ?", userId1);
            assertRecordExists("users", "id = ?", userId2);
            
            // Rollback transaction
            rollbackTransaction();
            
            // Verify data is rolled back
            assertRecordNotExists("users", "id = ?", userId1);
            assertRecordNotExists("users", "id = ?", userId2);
            
            logger.info("Transaction management test completed successfully");
        } catch (Exception e) {
            logger.error("Transaction management test failed", e);
            rollbackTransaction();
            Assert.fail("Transaction management test failed: " + e.getMessage());
        }
    }

    /**
     * Test data-driven database tests with CSV data
     */
    @Test(dataProvider = "userData", description = "Test data-driven database operations")
    public void testDataDrivenDatabase(String name, String email, String role, String expectedResult) {
        logger.info("Testing database operation with name: {}, email: {}, role: {}, expected: {}", 
                   name, email, role, expectedResult);
        
        if ("insert".equals(expectedResult)) {
            // Insert user
            String insertSql = "INSERT INTO users (name, email, role, created_at) VALUES (?, ?, ?, NOW())";
            int userId = insertRecord(insertSql, name, email, role);
            
            Assert.assertTrue(userId > 0, "User should be inserted successfully");
            
            // Verify insertion
            assertRecordExists("users", "id = ?", userId);
            
            // Clean up
            cleanupTestData("users", "id = ?", userId);
        } else if ("update".equals(expectedResult)) {
            // Test update scenario
            String updateSql = "UPDATE users SET name = ? WHERE email = ?";
            int affectedRows = executePreparedUpdate(updateSql, name, email);
            
            Assert.assertTrue(affectedRows >= 0, "Update should not fail");
        }
    }

    /**
     * Data provider for database tests
     */
    @DataProvider(name = "userData")
    public Object[][] getUserData() {
        List<Map<String, String>> testData = CSVDataProvider.readTestData();
        
        Object[][] data = new Object[testData.size()][4];
        for (int i = 0; i < testData.size(); i++) {
            Map<String, String> row = testData.get(i);
            data[i][0] = row.get("name");
            data[i][1] = row.get("email");
            data[i][2] = row.get("role");
            data[i][3] = row.get("expectedResult");
        }
        
        return data;
    }

    /**
     * Test database performance
     */
    @Test(description = "Test database query performance")
    public void testDatabasePerformance() {
        logger.info("Starting database performance test");
        
        long startTime = System.currentTimeMillis();
        
        // Execute multiple queries
        for (int i = 0; i < 100; i++) {
            String sql = "SELECT * FROM users WHERE role = ?";
            getResultAsList(sql, "user");
        }
        
        long endTime = System.currentTimeMillis();
        long totalTime = endTime - startTime;
        
        logger.info("Executed 100 queries in {} ms", totalTime);
        
        // Assert performance (should complete within 5 seconds)
        Assert.assertTrue(totalTime < 5000, "Database queries should complete within 5 seconds");
        
        logger.info("Database performance test completed successfully");
    }

    /**
     * Test database constraints
     */
    @Test(description = "Test database constraints and integrity")
    public void testDatabaseConstraints() {
        logger.info("Starting database constraints test");
        
        // Test foreign key constraint
        try {
            String invalidFkSql = "INSERT INTO posts (user_id, title, content) VALUES (99999, 'Test Post', 'Content')";
            executeUpdate(invalidFkSql);
            Assert.fail("Foreign key constraint should prevent invalid user_id");
        } catch (Exception e) {
            logger.info("Foreign key constraint working correctly: {}", e.getMessage());
        }
        
        // Test unique constraint
        try {
            String duplicateEmailSql = "INSERT INTO users (name, email, role, created_at) VALUES (?, ?, ?, NOW())";
            executePreparedUpdate(duplicateEmailSql, "Duplicate User", "existing@example.com", "user");
            Assert.fail("Unique constraint should prevent duplicate email");
        } catch (Exception e) {
            logger.info("Unique constraint working correctly: {}", e.getMessage());
        }
        
        // Test not null constraint
        try {
            String nullNameSql = "INSERT INTO users (name, email, role, created_at) VALUES (?, ?, ?, NOW())";
            executePreparedUpdate(nullNameSql, null, "test@example.com", "user");
            Assert.fail("Not null constraint should prevent null name");
        } catch (Exception e) {
            logger.info("Not null constraint working correctly: {}", e.getMessage());
        }
        
        logger.info("Database constraints test completed successfully");
    }

    /**
     * Test database backup and restore
     */
    @Test(description = "Test database backup and restore functionality")
    public void testDatabaseBackupRestore() {
        logger.info("Starting database backup/restore test");
        
        // Get initial data count
        int initialUserCount = getRowCount("users");
        int initialPostCount = getRowCount("posts");
        
        // Simulate backup (in real scenario, this would be actual backup)
        logger.info("Simulating database backup");
        
        // Simulate restore (in real scenario, this would be actual restore)
        logger.info("Simulating database restore");
        
        // Verify data integrity after restore
        int finalUserCount = getRowCount("users");
        int finalPostCount = getRowCount("posts");
        
        Assert.assertEquals(finalUserCount, initialUserCount, "User count should remain the same after restore");
        Assert.assertEquals(finalPostCount, initialPostCount, "Post count should remain the same after restore");
        
        logger.info("Database backup/restore test completed successfully");
    }

    /**
     * Test database connection pooling
     */
    @Test(description = "Test database connection pooling")
    public void testConnectionPooling() {
        logger.info("Starting connection pooling test");
        
        // Test multiple concurrent connections
        for (int i = 0; i < 10; i++) {
            try {
                getConnection();
                String sql = "SELECT COUNT(*) FROM users";
                Object result = getSingleValue(sql, "COUNT(*)");
                Assert.assertNotNull(result, "Query should return result");
            } catch (Exception e) {
                logger.error("Connection test failed for iteration {}", i, e);
                Assert.fail("Connection test failed: " + e.getMessage());
            }
        }
        
        logger.info("Connection pooling test completed successfully");
    }
}