package com.testframework.database;

import com.testframework.config.ConfigManager;

import java.sql.*;
import java.util.*;

/**
 * Database Manager to handle database operations
 * Supports MySQL database operations for testing
 */
public class DatabaseManager {
    
    private ConfigManager configManager;
    private Connection connection;
    
    public DatabaseManager() {
        configManager = ConfigManager.getInstance();
    }
    
    /**
     * Get database connection
     */
    public Connection getConnection() {
        if (connection == null || isConnectionClosed()) {
            connection = createConnection();
        }
        return connection;
    }
    
    /**
     * Create database connection
     */
    private Connection createConnection() {
        try {
            Class.forName(configManager.getDbDriver());
            return DriverManager.getConnection(
                configManager.getDbUrl(),
                configManager.getDbUsername(),
                configManager.getDbPassword()
            );
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException("Failed to create database connection", e);
        }
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
    public void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                // Log error but don't throw exception
                System.err.println("Error closing database connection: " + e.getMessage());
            }
        }
    }
    
    /**
     * Execute SELECT query and return results
     */
    public List<Map<String, Object>> executeQuery(String query) {
        List<Map<String, Object>> results = new ArrayList<>();
        
        try (Statement statement = getConnection().createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();
            
            while (resultSet.next()) {
                Map<String, Object> row = new HashMap<>();
                for (int i = 1; i <= columnCount; i++) {
                    String columnName = metaData.getColumnName(i);
                    Object value = resultSet.getObject(i);
                    row.put(columnName, value);
                }
                results.add(row);
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error executing query: " + query, e);
        }
        
        return results;
    }
    
    /**
     * Execute UPDATE, INSERT, DELETE query
     */
    public int executeUpdate(String query) {
        try (Statement statement = getConnection().createStatement()) {
            return statement.executeUpdate(query);
        } catch (SQLException e) {
            throw new RuntimeException("Error executing update query: " + query, e);
        }
    }
    
    /**
     * Execute prepared statement with parameters
     */
    public List<Map<String, Object>> executePreparedQuery(String query, Object... parameters) {
        List<Map<String, Object>> results = new ArrayList<>();
        
        try (PreparedStatement statement = getConnection().prepareStatement(query)) {
            
            // Set parameters
            for (int i = 0; i < parameters.length; i++) {
                statement.setObject(i + 1, parameters[i]);
            }
            
            try (ResultSet resultSet = statement.executeQuery()) {
                ResultSetMetaData metaData = resultSet.getMetaData();
                int columnCount = metaData.getColumnCount();
                
                while (resultSet.next()) {
                    Map<String, Object> row = new HashMap<>();
                    for (int i = 1; i <= columnCount; i++) {
                        String columnName = metaData.getColumnName(i);
                        Object value = resultSet.getObject(i);
                        row.put(columnName, value);
                    }
                    results.add(row);
                }
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error executing prepared query: " + query, e);
        }
        
        return results;
    }
    
    /**
     * Execute prepared statement for UPDATE, INSERT, DELETE
     */
    public int executePreparedUpdate(String query, Object... parameters) {
        try (PreparedStatement statement = getConnection().prepareStatement(query)) {
            
            // Set parameters
            for (int i = 0; i < parameters.length; i++) {
                statement.setObject(i + 1, parameters[i]);
            }
            
            return statement.executeUpdate();
            
        } catch (SQLException e) {
            throw new RuntimeException("Error executing prepared update query: " + query, e);
        }
    }
    
    /**
     * Get single value from query result
     */
    public Object getSingleValue(String query) {
        List<Map<String, Object>> results = executeQuery(query);
        if (results.isEmpty() || results.get(0).isEmpty()) {
            return null;
        }
        return results.get(0).values().iterator().next();
    }
    
    /**
     * Get single value from prepared query
     */
    public Object getSingleValue(String query, Object... parameters) {
        List<Map<String, Object>> results = executePreparedQuery(query, parameters);
        if (results.isEmpty() || results.get(0).isEmpty()) {
            return null;
        }
        return results.get(0).values().iterator().next();
    }
    
    /**
     * Check if record exists
     */
    public boolean recordExists(String tableName, String columnName, Object value) {
        String query = "SELECT COUNT(*) FROM " + tableName + " WHERE " + columnName + " = ?";
        Object count = getSingleValue(query, value);
        return count != null && ((Number) count).intValue() > 0;
    }
    
    /**
     * Get record count
     */
    public int getRecordCount(String tableName) {
        String query = "SELECT COUNT(*) FROM " + tableName;
        Object count = getSingleValue(query);
        return count != null ? ((Number) count).intValue() : 0;
    }
    
    /**
     * Get record count with condition
     */
    public int getRecordCount(String tableName, String condition) {
        String query = "SELECT COUNT(*) FROM " + tableName + " WHERE " + condition;
        Object count = getSingleValue(query);
        return count != null ? ((Number) count).intValue() : 0;
    }
    
    /**
     * Insert record
     */
    public int insertRecord(String tableName, Map<String, Object> data) {
        if (data.isEmpty()) {
            throw new IllegalArgumentException("Data cannot be empty");
        }
        
        StringBuilder query = new StringBuilder("INSERT INTO " + tableName + " (");
        StringBuilder values = new StringBuilder(") VALUES (");
        
        List<Object> parameters = new ArrayList<>();
        boolean first = true;
        
        for (Map.Entry<String, Object> entry : data.entrySet()) {
            if (!first) {
                query.append(", ");
                values.append(", ");
            }
            query.append(entry.getKey());
            values.append("?");
            parameters.add(entry.getValue());
            first = false;
        }
        
        query.append(values).append(")");
        
        return executePreparedUpdate(query.toString(), parameters.toArray());
    }
    
    /**
     * Update record
     */
    public int updateRecord(String tableName, Map<String, Object> data, String whereCondition, Object... whereParameters) {
        if (data.isEmpty()) {
            throw new IllegalArgumentException("Data cannot be empty");
        }
        
        StringBuilder query = new StringBuilder("UPDATE " + tableName + " SET ");
        List<Object> parameters = new ArrayList<>();
        boolean first = true;
        
        for (Map.Entry<String, Object> entry : data.entrySet()) {
            if (!first) {
                query.append(", ");
            }
            query.append(entry.getKey()).append(" = ?");
            parameters.add(entry.getValue());
            first = false;
        }
        
        query.append(" WHERE ").append(whereCondition);
        parameters.addAll(Arrays.asList(whereParameters));
        
        return executePreparedUpdate(query.toString(), parameters.toArray());
    }
    
    /**
     * Delete record
     */
    public int deleteRecord(String tableName, String whereCondition, Object... parameters) {
        String query = "DELETE FROM " + tableName + " WHERE " + whereCondition;
        return executePreparedUpdate(query, parameters);
    }
    
    /**
     * Get table structure
     */
    public List<Map<String, Object>> getTableStructure(String tableName) {
        String query = "DESCRIBE " + tableName;
        return executeQuery(query);
    }
    
    /**
     * Get all table names
     */
    public List<String> getTableNames() {
        String query = "SHOW TABLES";
        List<Map<String, Object>> results = executeQuery(query);
        List<String> tableNames = new ArrayList<>();
        
        for (Map<String, Object> row : results) {
            tableNames.add(row.values().iterator().next().toString());
        }
        
        return tableNames;
    }
    
    /**
     * Execute batch operations
     */
    public int[] executeBatch(List<String> queries) {
        try (Statement statement = getConnection().createStatement()) {
            for (String query : queries) {
                statement.addBatch(query);
            }
            return statement.executeBatch();
        } catch (SQLException e) {
            throw new RuntimeException("Error executing batch operations", e);
        }
    }
}