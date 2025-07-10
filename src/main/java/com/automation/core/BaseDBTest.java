package com.automation.core;

import org.testng.Assert;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Base class for Database automation tests
 * Provides common database operations and utilities
 */
public class BaseDBTest {
    protected static final Logger logger = LogManager.getLogger(BaseDBTest.class);
    protected ConfigManager config;
    protected Connection connection;

    public BaseDBTest() {
        this.config = ConfigManager.getInstance();
    }

    /**
     * Establish database connection
     */
    protected Connection getConnection() {
        if (connection == null || isConnectionClosed()) {
            try {
                String url = buildConnectionUrl();
                logger.info("Connecting to database: {}", url);
                
                connection = DriverManager.getConnection(
                    url,
                    config.getDBUsername(),
                    config.getDBPassword()
                );
                
                logger.info("Database connection established successfully");
            } catch (SQLException e) {
                logger.error("Failed to establish database connection", e);
                throw new RuntimeException("Database connection failed", e);
            }
        }
        return connection;
    }

    /**
     * Build database connection URL
     */
    private String buildConnectionUrl() {
        String host = config.getDBHost();
        int port = config.getDBPort();
        String database = config.getDBName();
        
        // Default to MySQL if no specific driver is configured
        return String.format("jdbc:mysql://%s:%d/%s?useSSL=false&serverTimezone=UTC", 
                           host, port, database);
    }

    /**
     * Check if connection is closed
     */
    private boolean isConnectionClosed() {
        try {
            return connection == null || connection.isClosed();
        } catch (SQLException e) {
            return true;
        }
    }

    /**
     * Close database connection
     */
    protected void closeConnection() {
        if (connection != null && !isConnectionClosed()) {
            try {
                connection.close();
                logger.info("Database connection closed successfully");
            } catch (SQLException e) {
                logger.error("Error closing database connection", e);
            }
        }
    }

    /**
     * Execute SELECT query and return ResultSet
     */
    protected ResultSet executeQuery(String sql) {
        try {
            logger.info("Executing query: {}", sql);
            Statement statement = getConnection().createStatement();
            return statement.executeQuery(sql);
        } catch (SQLException e) {
            logger.error("Error executing query: {}", sql, e);
            throw new RuntimeException("Query execution failed", e);
        }
    }

    /**
     * Execute UPDATE, INSERT, DELETE query and return affected rows
     */
    protected int executeUpdate(String sql) {
        try {
            logger.info("Executing update: {}", sql);
            Statement statement = getConnection().createStatement();
            int affectedRows = statement.executeUpdate(sql);
            logger.info("Query affected {} rows", affectedRows);
            return affectedRows;
        } catch (SQLException e) {
            logger.error("Error executing update: {}", sql, e);
            throw new RuntimeException("Update execution failed", e);
        }
    }

    /**
     * Execute prepared statement with parameters
     */
    protected ResultSet executePreparedQuery(String sql, Object... parameters) {
        try {
            logger.info("Executing prepared query: {} with parameters: {}", sql, parameters);
            PreparedStatement statement = getConnection().prepareStatement(sql);
            
            for (int i = 0; i < parameters.length; i++) {
                statement.setObject(i + 1, parameters[i]);
            }
            
            return statement.executeQuery();
        } catch (SQLException e) {
            logger.error("Error executing prepared query: {}", sql, e);
            throw new RuntimeException("Prepared query execution failed", e);
        }
    }

    /**
     * Execute prepared update with parameters
     */
    protected int executePreparedUpdate(String sql, Object... parameters) {
        try {
            logger.info("Executing prepared update: {} with parameters: {}", sql, parameters);
            PreparedStatement statement = getConnection().prepareStatement(sql);
            
            for (int i = 0; i < parameters.length; i++) {
                statement.setObject(i + 1, parameters[i]);
            }
            
            int affectedRows = statement.executeUpdate();
            logger.info("Prepared update affected {} rows", affectedRows);
            return affectedRows;
        } catch (SQLException e) {
            logger.error("Error executing prepared update: {}", sql, e);
            throw new RuntimeException("Prepared update execution failed", e);
        }
    }

    /**
     * Get single value from query result
     */
    protected Object getSingleValue(String sql, String columnName) {
        try (ResultSet rs = executeQuery(sql)) {
            if (rs.next()) {
                return rs.getObject(columnName);
            }
            return null;
        } catch (SQLException e) {
            logger.error("Error getting single value from query", e);
            throw new RuntimeException("Failed to get single value", e);
        }
    }

    /**
     * Get single value from prepared query
     */
    protected Object getSingleValue(String sql, String columnName, Object... parameters) {
        try (ResultSet rs = executePreparedQuery(sql, parameters)) {
            if (rs.next()) {
                return rs.getObject(columnName);
            }
            return null;
        } catch (SQLException e) {
            logger.error("Error getting single value from prepared query", e);
            throw new RuntimeException("Failed to get single value", e);
        }
    }

    /**
     * Get list of values from query result
     */
    protected List<Object> getColumnValues(String sql, String columnName) {
        List<Object> values = new ArrayList<>();
        try (ResultSet rs = executeQuery(sql)) {
            while (rs.next()) {
                values.add(rs.getObject(columnName));
            }
        } catch (SQLException e) {
            logger.error("Error getting column values from query", e);
            throw new RuntimeException("Failed to get column values", e);
        }
        return values;
    }

    /**
     * Get list of values from prepared query
     */
    protected List<Object> getColumnValues(String sql, String columnName, Object... parameters) {
        List<Object> values = new ArrayList<>();
        try (ResultSet rs = executePreparedQuery(sql, parameters)) {
            while (rs.next()) {
                values.add(rs.getObject(columnName));
            }
        } catch (SQLException e) {
            logger.error("Error getting column values from prepared query", e);
            throw new RuntimeException("Failed to get column values", e);
        }
        return values;
    }

    /**
     * Get result as list of maps
     */
    protected List<Map<String, Object>> getResultAsList(String sql) {
        List<Map<String, Object>> results = new ArrayList<>();
        try (ResultSet rs = executeQuery(sql)) {
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();
            
            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                for (int i = 1; i <= columnCount; i++) {
                    String columnName = metaData.getColumnName(i);
                    Object value = rs.getObject(i);
                    row.put(columnName, value);
                }
                results.add(row);
            }
        } catch (SQLException e) {
            logger.error("Error getting result as list", e);
            throw new RuntimeException("Failed to get result as list", e);
        }
        return results;
    }

    /**
     * Get result as list of maps from prepared query
     */
    protected List<Map<String, Object>> getResultAsList(String sql, Object... parameters) {
        List<Map<String, Object>> results = new ArrayList<>();
        try (ResultSet rs = executePreparedQuery(sql, parameters)) {
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();
            
            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                for (int i = 1; i <= columnCount; i++) {
                    String columnName = metaData.getColumnName(i);
                    Object value = rs.getObject(i);
                    row.put(columnName, value);
                }
                results.add(row);
            }
        } catch (SQLException e) {
            logger.error("Error getting result as list from prepared query", e);
            throw new RuntimeException("Failed to get result as list", e);
        }
        return results;
    }

    /**
     * Get first row as map
     */
    protected Map<String, Object> getFirstRow(String sql) {
        List<Map<String, Object>> results = getResultAsList(sql);
        return results.isEmpty() ? null : results.get(0);
    }

    /**
     * Get first row as map from prepared query
     */
    protected Map<String, Object> getFirstRow(String sql, Object... parameters) {
        List<Map<String, Object>> results = getResultAsList(sql, parameters);
        return results.isEmpty() ? null : results.get(0);
    }

    /**
     * Count rows in table
     */
    protected int getRowCount(String tableName) {
        String sql = "SELECT COUNT(*) FROM " + tableName;
        Object count = getSingleValue(sql, "COUNT(*)");
        return count != null ? ((Number) count).intValue() : 0;
    }

    /**
     * Count rows in table with condition
     */
    protected int getRowCount(String tableName, String whereClause, Object... parameters) {
        String sql = "SELECT COUNT(*) FROM " + tableName + " WHERE " + whereClause;
        Object count = getSingleValue(sql, "COUNT(*)", parameters);
        return count != null ? ((Number) count).intValue() : 0;
    }

    /**
     * Check if record exists
     */
    protected boolean recordExists(String tableName, String whereClause, Object... parameters) {
        return getRowCount(tableName, whereClause, parameters) > 0;
    }

    /**
     * Insert record and return generated key
     */
    protected int insertRecord(String sql, Object... parameters) {
        try {
            logger.info("Inserting record with SQL: {} and parameters: {}", sql, parameters);
            PreparedStatement statement = getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            
            for (int i = 0; i < parameters.length; i++) {
                statement.setObject(i + 1, parameters[i]);
            }
            
            int affectedRows = statement.executeUpdate();
            logger.info("Insert affected {} rows", affectedRows);
            
            if (affectedRows > 0) {
                ResultSet generatedKeys = statement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                }
            }
            
            return -1;
        } catch (SQLException e) {
            logger.error("Error inserting record", e);
            throw new RuntimeException("Insert operation failed", e);
        }
    }

    /**
     * Assert row count equals
     */
    protected void assertRowCount(String tableName, int expectedCount) {
        int actualCount = getRowCount(tableName);
        Assert.assertEquals(actualCount, expectedCount, 
                          "Row count in table " + tableName + " should be " + expectedCount);
    }

    /**
     * Assert row count with condition
     */
    protected void assertRowCount(String tableName, String whereClause, int expectedCount, Object... parameters) {
        int actualCount = getRowCount(tableName, whereClause, parameters);
        Assert.assertEquals(actualCount, expectedCount, 
                          "Row count in table " + tableName + " with condition should be " + expectedCount);
    }

    /**
     * Assert record exists
     */
    protected void assertRecordExists(String tableName, String whereClause, Object... parameters) {
        boolean exists = recordExists(tableName, whereClause, parameters);
        Assert.assertTrue(exists, "Record should exist in table " + tableName);
    }

    /**
     * Assert record does not exist
     */
    protected void assertRecordNotExists(String tableName, String whereClause, Object... parameters) {
        boolean exists = recordExists(tableName, whereClause, parameters);
        Assert.assertFalse(exists, "Record should not exist in table " + tableName);
    }

    /**
     * Assert column value equals
     */
    protected void assertColumnValue(String sql, String columnName, Object expectedValue, Object... parameters) {
        Object actualValue = getSingleValue(sql, columnName, parameters);
        Assert.assertEquals(actualValue, expectedValue, 
                          "Column " + columnName + " should have value " + expectedValue);
    }

    /**
     * Clean up test data
     */
    protected void cleanupTestData(String tableName, String whereClause, Object... parameters) {
        String sql = "DELETE FROM " + tableName + " WHERE " + whereClause;
        int deletedRows = executePreparedUpdate(sql, parameters);
        logger.info("Cleaned up {} rows from table {}", deletedRows, tableName);
    }

    /**
     * Begin transaction
     */
    protected void beginTransaction() {
        try {
            getConnection().setAutoCommit(false);
            logger.info("Transaction begun");
        } catch (SQLException e) {
            logger.error("Error beginning transaction", e);
            throw new RuntimeException("Failed to begin transaction", e);
        }
    }

    /**
     * Commit transaction
     */
    protected void commitTransaction() {
        try {
            getConnection().commit();
            getConnection().setAutoCommit(true);
            logger.info("Transaction committed");
        } catch (SQLException e) {
            logger.error("Error committing transaction", e);
            throw new RuntimeException("Failed to commit transaction", e);
        }
    }

    /**
     * Rollback transaction
     */
    protected void rollbackTransaction() {
        try {
            getConnection().rollback();
            getConnection().setAutoCommit(true);
            logger.info("Transaction rolled back");
        } catch (SQLException e) {
            logger.error("Error rolling back transaction", e);
            throw new RuntimeException("Failed to rollback transaction", e);
        }
    }
}