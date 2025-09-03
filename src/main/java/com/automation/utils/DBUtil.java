package com.automation.utils;

import com.automation.config.ConfigManager;
import com.automation.config.DBConfig;
import com.automation.config.LoggerManager;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Database utility class for managing database connections and executing queries
 * Supports connection pooling with HikariCP
 */
public class DBUtil {

    private static final Logger logger = LoggerManager.getLogger(DBUtil.class);
    private static DBUtil instance;
    private HikariDataSource dataSource;
    private final DBConfig dbConfig;
    private final ConfigManager configManager;

    private DBUtil() {
        this.dbConfig = DBConfig.getInstance();
        this.configManager = ConfigManager.getInstance();
        initializeDataSource();
    }

    /**
     * Get singleton instance of DBUtil
     * @return DBUtil instance
     */
    public static synchronized DBUtil getInstance() {
        if (instance == null) {
            instance = new DBUtil();
        }
        return instance;
    }

    /**
     * Initialize HikariCP data source
     */
    private void initializeDataSource() {
        try {
            HikariConfig config = new HikariConfig();
            
            // Set basic connection properties
            config.setJdbcUrl(dbConfig.getDatabaseUrl());
            config.setUsername(dbConfig.getUsername());
            config.setPassword(dbConfig.getPassword());
            config.setDriverClassName(dbConfig.getDriverClassName());
            
            // Set connection pool properties
            config.setMaximumPoolSize(configManager.getIntProperty("db.max.pool.size", 10));
            config.setMinimumIdle(configManager.getIntProperty("db.min.idle", 5));
            config.setConnectionTimeout(configManager.getIntProperty("db.connection.timeout", 30000));
            config.setIdleTimeout(configManager.getIntProperty("db.idle.timeout", 600000));
            config.setMaxLifetime(configManager.getIntProperty("db.max.lifetime", 1800000));
            config.setLeakDetectionThreshold(configManager.getIntProperty("db.leak.detection.threshold", 60000));
            
            // Set connection test properties
            config.setConnectionTestQuery("SELECT 1");
            config.setValidationTimeout(configManager.getIntProperty("db.validation.timeout", 5000));
            
            // Create data source
            dataSource = new HikariDataSource(config);
            
            logger.info("Database connection pool initialized successfully");
            logger.info("Database URL: {}", dbConfig.getDatabaseUrl());
            logger.info("Max pool size: {}", config.getMaximumPoolSize());
            
        } catch (Exception e) {
            logger.error("Error initializing database connection pool", e);
            throw new RuntimeException("Failed to initialize database connection pool", e);
        }
    }

    /**
     * Get database connection from pool
     * @return Database connection
     * @throws SQLException if connection fails
     */
    public Connection getConnection() throws SQLException {
        try {
            Connection connection = dataSource.getConnection();
            connection.setAutoCommit(false); // Start transaction
            logger.debug("Database connection obtained from pool");
            return connection;
        } catch (SQLException e) {
            logger.error("Error getting database connection", e);
            throw e;
        }
    }

    /**
     * Execute SELECT query and return results as List of Maps
     * 
     * @param query SQL query to execute
     * @param params Query parameters (optional)
     * @return List of Maps containing query results
     */
    public List<Map<String, Object>> executeQuery(String query, Object... params) {
        List<Map<String, Object>> results = new ArrayList<>();
        
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            
            // Set parameters if provided
            if (params != null) {
                for (int i = 0; i < params.length; i++) {
                    statement.setObject(i + 1, params[i]);
                }
            }
            
            logger.debug("Executing query: {} with {} parameters", query, params != null ? params.length : 0);
            
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
     * Execute SELECT query and return single result as Map
     * 
     * @param query SQL query to execute
     * @param params Query parameters (optional)
     * @return Map containing single row result, or null if no results
     */
    public Map<String, Object> executeQuerySingle(String query, Object... params) {
        List<Map<String, Object>> results = executeQuery(query, params);
        return results.isEmpty() ? null : results.get(0);
    }

    /**
     * Execute INSERT, UPDATE, DELETE query and return affected row count
     * 
     * @param query SQL query to execute
     * @param params Query parameters (optional)
     * @return Number of affected rows
     */
    public int executeUpdate(String query, Object... params) {
        int affectedRows = 0;
        
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            
            // Set parameters if provided
            if (params != null) {
                for (int i = 0; i < params.length; i++) {
                    statement.setObject(i + 1, params[i]);
                }
            }
            
            logger.debug("Executing update query: {} with {} parameters", query, params != null ? params.length : 0);
            
            affectedRows = statement.executeUpdate();
            connection.commit(); // Commit transaction
            
            logger.info("Update query executed successfully. Affected {} rows", affectedRows);
            
        } catch (SQLException e) {
            logger.error("Error executing update query: {}", query, e);
            throw new RuntimeException("Update query execution failed", e);
        }
        
        return affectedRows;
    }

    /**
     * Execute batch INSERT, UPDATE, DELETE queries
     * 
     * @param query SQL query to execute
     * @param batchParams List of parameter arrays for batch execution
     * @return Array of affected row counts for each batch
     */
    public int[] executeBatch(String query, List<Object[]> batchParams) {
        int[] affectedRows = new int[0];
        
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            
            // Add batch parameters
            for (Object[] params : batchParams) {
                if (params != null) {
                    for (int i = 0; i < params.length; i++) {
                        statement.setObject(i + 1, params[i]);
                    }
                    statement.addBatch();
                }
            }
            
            logger.debug("Executing batch query: {} with {} batch sets", query, batchParams.size());
            
            affectedRows = statement.executeBatch();
            connection.commit(); // Commit transaction
            
            int totalAffected = 0;
            for (int rows : affectedRows) {
                totalAffected += rows;
            }
            
            logger.info("Batch query executed successfully. Total affected rows: {}", totalAffected);
            
        } catch (SQLException e) {
            logger.error("Error executing batch query: {}", query, e);
            throw new RuntimeException("Batch query execution failed", e);
        }
        
        return affectedRows;
    }

    /**
     * Execute stored procedure
     * 
     * @param procedureName Name of the stored procedure
     * @param params Procedure parameters (optional)
     * @return Map containing output parameters and result sets
     */
    public Map<String, Object> executeStoredProcedure(String procedureName, Object... params) {
        Map<String, Object> results = new HashMap<>();
        
        try (Connection connection = getConnection();
             CallableStatement statement = connection.prepareCall(procedureName)) {
            
            // Set parameters if provided
            if (params != null) {
                for (int i = 0; i < params.length; i++) {
                    statement.setObject(i + 1, params[i]);
                }
            }
            
            logger.debug("Executing stored procedure: {} with {} parameters", procedureName, params != null ? params.length : 0);
            
            boolean hasResults = statement.execute();
            
            // Handle result sets
            if (hasResults) {
                try (ResultSet resultSet = statement.getResultSet()) {
                    List<Map<String, Object>> resultList = new ArrayList<>();
                    ResultSetMetaData metaData = resultSet.getMetaData();
                    int columnCount = metaData.getColumnCount();
                    
                    while (resultSet.next()) {
                        Map<String, Object> row = new HashMap<>();
                        for (int i = 1; i <= columnCount; i++) {
                            String columnName = metaData.getColumnName(i);
                            Object value = resultSet.getObject(i);
                            row.put(columnName, value);
                        }
                        resultList.add(row);
                    }
                    results.put("resultSet", resultList);
                }
            }
            
            // Handle output parameters
            for (int i = 1; i <= statement.getParameterMetaData().getParameterCount(); i++) {
                try {
                    Object value = statement.getObject(i);
                    if (value != null) {
                        results.put("param" + i, value);
                    }
                } catch (SQLException e) {
                    // Parameter is not an output parameter
                }
            }
            
            connection.commit(); // Commit transaction
            logger.info("Stored procedure executed successfully: {}", procedureName);
            
        } catch (SQLException e) {
            logger.error("Error executing stored procedure: {}", procedureName, e);
            throw new RuntimeException("Stored procedure execution failed", e);
        }
        
        return results;
    }

    /**
     * Check if database connection is valid
     * 
     * @return true if connection is valid, false otherwise
     */
    public boolean isConnectionValid() {
        try (Connection connection = getConnection()) {
            return connection.isValid(5); // 5 second timeout
        } catch (SQLException e) {
            logger.error("Error checking database connection validity", e);
            return false;
        }
    }

    /**
     * Get database connection pool statistics
     * 
     * @return Map containing pool statistics
     */
    public Map<String, Object> getPoolStatistics() {
        Map<String, Object> stats = new HashMap<>();
        
        if (dataSource != null) {
            stats.put("totalConnections", dataSource.getHikariPoolMXBean().getTotalConnections());
            stats.put("activeConnections", dataSource.getHikariPoolMXBean().getActiveConnections());
            stats.put("idleConnections", dataSource.getHikariPoolMXBean().getIdleConnections());
            stats.put("threadsAwaitingConnection", dataSource.getHikariPoolMXBean().getThreadsAwaitingConnection());
        }
        
        return stats;
    }

    /**
     * Close database connection pool
     */
    public void closePool() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
            logger.info("Database connection pool closed");
        }
    }

    /**
     * Execute query and validate results against expected data
     * 
     * @param query SQL query to execute
     * @param expectedData Expected data for validation
     * @param params Query parameters (optional)
     * @return true if validation passes, false otherwise
     */
    public boolean validateQueryResults(String query, Map<String, Object> expectedData, Object... params) {
        try {
            Map<String, Object> actualResult = executeQuerySingle(query, params);
            
            if (actualResult == null) {
                logger.warn("Query returned no results for validation");
                return false;
            }
            
            // Validate each expected field
            for (Map.Entry<String, Object> entry : expectedData.entrySet()) {
                String key = entry.getKey();
                Object expectedValue = entry.getValue();
                Object actualValue = actualResult.get(key);
                
                if (!expectedValue.equals(actualValue)) {
                    logger.warn("Validation failed for field '{}': expected '{}', actual '{}'", 
                               key, expectedValue, actualValue);
                    return false;
                }
            }
            
            logger.info("Query results validation passed successfully");
            return true;
            
        } catch (Exception e) {
            logger.error("Error during query results validation", e);
            return false;
        }
    }

    /**
     * Execute query and return count of results
     * 
     * @param query SQL query to execute
     * @param params Query parameters (optional)
     * @return Count of results
     */
    public long getRowCount(String query, Object... params) {
        try {
            List<Map<String, Object>> results = executeQuery(query, params);
            return results.size();
        } catch (Exception e) {
            logger.error("Error getting row count", e);
            return 0;
        }
    }

    /**
     * Clean up resources
     */
    public void cleanup() {
        closePool();
    }
}
