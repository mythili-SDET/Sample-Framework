package com.automation.database;

import com.automation.core.ConfigManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.*;

/**
 * Database Manager for handling database connections and operations
 * Supports MySQL and PostgreSQL databases
 */
public class DatabaseManager {
    private static final Logger logger = LogManager.getLogger(DatabaseManager.class);
    private static final ConfigManager config = ConfigManager.getInstance();
    private Connection connection;
    private String databaseType;

    public DatabaseManager(String databaseType) {
        this.databaseType = databaseType.toLowerCase();
        establishConnection();
    }

    /**
     * Establish database connection based on database type
     */
    private void establishConnection() {
        try {
            String url, username, password, driver;
            
            if ("mysql".equals(databaseType)) {
                url = config.getDbUrl();
                username = config.getDbUsername();
                password = config.getDbPassword();
                driver = config.getDbDriver();
            } else if ("postgres".equals(databaseType) || "postgresql".equals(databaseType)) {
                url = config.getPostgresUrl();
                username = config.getPostgresUsername();
                password = config.getPostgresPassword();
                driver = config.getPostgresDriver();
            } else {
                throw new IllegalArgumentException("Unsupported database type: " + databaseType);
            }

            Class.forName(driver);
            connection = DriverManager.getConnection(url, username, password);
            connection.setAutoCommit(false); // Enable transaction management
            
            logger.info("Successfully connected to {} database", databaseType);
            
        } catch (ClassNotFoundException e) {
            logger.error("Database driver not found for {}", databaseType, e);
            throw new RuntimeException("Database driver not found", e);
        } catch (SQLException e) {
            logger.error("Failed to connect to {} database", databaseType, e);
            throw new RuntimeException("Database connection failed", e);
        }
    }

    /**
     * Execute SELECT query and return results as List of Maps
     * @param query SQL SELECT query
     * @param parameters Query parameters
     * @return List of Maps containing row data
     */
    public List<Map<String, Object>> executeQuery(String query, Object... parameters) {
        List<Map<String, Object>> results = new ArrayList<>();
        
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            
            // Set parameters
            for (int i = 0; i < parameters.length; i++) {
                statement.setObject(i + 1, parameters[i]);
            }
            
            logger.debug("Executing query: {} with parameters: {}", query, Arrays.toString(parameters));
            
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
            
            logger.info("Query executed successfully. Returned {} rows", results.size());
            
        } catch (SQLException e) {
            logger.error("Error executing query: {}", query, e);
            throw new RuntimeException("Query execution failed", e);
        }
        
        return results;
    }

    /**
     * Execute INSERT, UPDATE, DELETE queries
     * @param query SQL query
     * @param parameters Query parameters
     * @return Number of affected rows
     */
    public int executeUpdate(String query, Object... parameters) {
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            
            // Set parameters
            for (int i = 0; i < parameters.length; i++) {
                statement.setObject(i + 1, parameters[i]);
            }
            
            logger.debug("Executing update: {} with parameters: {}", query, Arrays.toString(parameters));
            
            int affectedRows = statement.executeUpdate();
            logger.info("Update executed successfully. Affected {} rows", affectedRows);
            
            return affectedRows;
            
        } catch (SQLException e) {
            logger.error("Error executing update: {}", query, e);
            throw new RuntimeException("Update execution failed", e);
        }
    }

    /**
     * Execute batch updates
     * @param query SQL query
     * @param parametersList List of parameter arrays
     * @return Array of update counts
     */
    public int[] executeBatch(String query, List<Object[]> parametersList) {
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            
            for (Object[] parameters : parametersList) {
                for (int i = 0; i < parameters.length; i++) {
                    statement.setObject(i + 1, parameters[i]);
                }
                statement.addBatch();
            }
            
            logger.debug("Executing batch update: {} with {} parameter sets", query, parametersList.size());
            
            int[] updateCounts = statement.executeBatch();
            logger.info("Batch update executed successfully. Total operations: {}", updateCounts.length);
            
            return updateCounts;
            
        } catch (SQLException e) {
            logger.error("Error executing batch update: {}", query, e);
            throw new RuntimeException("Batch update execution failed", e);
        }
    }

    /**
     * Get single value from database
     * @param query SQL query that returns single value
     * @param parameters Query parameters
     * @return Single value as Object
     */
    public Object getSingleValue(String query, Object... parameters) {
        List<Map<String, Object>> results = executeQuery(query, parameters);
        
        if (results.isEmpty()) {
            return null;
        }
        
        Map<String, Object> firstRow = results.get(0);
        return firstRow.values().iterator().next();
    }

    /**
     * Check if record exists
     * @param query SQL query
     * @param parameters Query parameters
     * @return true if record exists, false otherwise
     */
    public boolean recordExists(String query, Object... parameters) {
        List<Map<String, Object>> results = executeQuery(query, parameters);
        return !results.isEmpty();
    }

    /**
     * Get row count from table
     * @param tableName Table name
     * @param whereClause WHERE clause (optional)
     * @param parameters Parameters for WHERE clause
     * @return Row count
     */
    public long getRowCount(String tableName, String whereClause, Object... parameters) {
        String query = "SELECT COUNT(*) FROM " + tableName;
        
        if (whereClause != null && !whereClause.trim().isEmpty()) {
            query += " WHERE " + whereClause;
        }
        
        Object result = getSingleValue(query, parameters);
        return result instanceof Number ? ((Number) result).longValue() : 0;
    }

    /**
     * Get table column names
     * @param tableName Table name
     * @return List of column names
     */
    public List<String> getTableColumns(String tableName) {
        List<String> columns = new ArrayList<>();
        
        try {
            DatabaseMetaData metaData = connection.getMetaData();
            try (ResultSet resultSet = metaData.getColumns(null, null, tableName, null)) {
                while (resultSet.next()) {
                    columns.add(resultSet.getString("COLUMN_NAME"));
                }
            }
            
            logger.info("Retrieved {} columns for table: {}", columns.size(), tableName);
            
        } catch (SQLException e) {
            logger.error("Error getting columns for table: {}", tableName, e);
            throw new RuntimeException("Failed to get table columns", e);
        }
        
        return columns;
    }

    /**
     * Commit transaction
     */
    public void commit() {
        try {
            connection.commit();
            logger.debug("Transaction committed successfully");
        } catch (SQLException e) {
            logger.error("Error committing transaction", e);
            throw new RuntimeException("Commit failed", e);
        }
    }

    /**
     * Rollback transaction
     */
    public void rollback() {
        try {
            connection.rollback();
            logger.debug("Transaction rolled back successfully");
        } catch (SQLException e) {
            logger.error("Error rolling back transaction", e);
            throw new RuntimeException("Rollback failed", e);
        }
    }

    /**
     * Close database connection
     */
    public void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                logger.info("Database connection closed successfully");
            } catch (SQLException e) {
                logger.error("Error closing database connection", e);
            }
        }
    }

    /**
     * Check if connection is valid
     * @return true if connection is valid, false otherwise
     */
    public boolean isConnectionValid() {
        try {
            return connection != null && !connection.isClosed() && connection.isValid(5);
        } catch (SQLException e) {
            logger.error("Error checking connection validity", e);
            return false;
        }
    }

    /**
     * Execute raw SQL script (for setup/teardown)
     * @param sqlScript SQL script content
     */
    public void executeSQLScript(String sqlScript) {
        try (Statement statement = connection.createStatement()) {
            // Split script by semicolons and execute each statement
            String[] statements = sqlScript.split(";");
            
            for (String sql : statements) {
                sql = sql.trim();
                if (!sql.isEmpty()) {
                    statement.execute(sql);
                }
            }
            
            logger.info("SQL script executed successfully");
            
        } catch (SQLException e) {
            logger.error("Error executing SQL script", e);
            throw new RuntimeException("SQL script execution failed", e);
        }
    }

    /**
     * Get connection for advanced operations
     * @return Database connection
     */
    public Connection getConnection() {
        return connection;
    }
}