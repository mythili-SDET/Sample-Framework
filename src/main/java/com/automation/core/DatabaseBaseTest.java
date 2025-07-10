package com.automation.core;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Base class for Database testing
 * Provides common functionality for database operations and validation
 */
public class DatabaseBaseTest {
    private static final Logger logger = LogManager.getLogger(DatabaseBaseTest.class);
    protected static final ConfigManager config = ConfigManager.getInstance();
    
    // Thread-safe connection management
    private static final ConcurrentHashMap<String, Connection> connections = new ConcurrentHashMap<>();
    
    protected Connection connection;
    protected Statement statement;
    protected PreparedStatement preparedStatement;
    protected ResultSet resultSet;

    @BeforeMethod
    public void dbSetUp() {
        logger.info("Starting Database test setup");
        
        try {
            // Get connection for current thread
            connection = getConnection();
            
            if (connection != null) {
                statement = connection.createStatement();
                logger.info("Database test setup completed");
            } else {
                logger.error("Failed to establish database connection");
                throw new RuntimeException("Database connection failed");
            }
            
        } catch (SQLException e) {
            logger.error("Error during database setup", e);
            throw new RuntimeException("Database setup failed", e);
        }
    }

    @AfterMethod
    public void dbTearDown() {
        logger.info("Starting Database test teardown");
        
        try {
            // Close resources
            closeResources();
            
            // Don't close connection here - keep it for the thread
            // Connection will be closed when thread ends
            
            logger.info("Database test teardown completed");
            
        } catch (Exception e) {
            logger.error("Error during database teardown", e);
        }
    }

    /**
     * Get database connection for current thread
     * @return Database connection
     */
    protected Connection getConnection() {
        String threadId = Thread.currentThread().getName();
        Connection conn = connections.get(threadId);
        
        if (conn == null || isClosed(conn)) {
            conn = createConnection();
            if (conn != null) {
                connections.put(threadId, conn);
                logger.info("New database connection created for thread: {}", threadId);
            }
        }
        
        return conn;
    }

    /**
     * Create new database connection
     * @return Database connection
     */
    private Connection createConnection() {
        try {
            String dbUrl = config.getDatabaseUrl();
            String dbUsername = config.getDatabaseUsername();
            String dbPassword = config.getDatabasePassword();
            String dbDriver = config.getDatabaseDriver();
            
            if (dbUrl == null || dbUsername == null || dbPassword == null) {
                logger.warn("Database configuration not found");
                return null;
            }
            
            // Load database driver
            if (dbDriver != null) {
                Class.forName(dbDriver);
            }
            
            logger.info("Connecting to database: {}", dbUrl);
            
            Connection conn = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
            conn.setAutoCommit(true); // Enable auto-commit by default
            
            logger.info("Database connection established successfully");
            return conn;
            
        } catch (Exception e) {
            logger.error("Error creating database connection", e);
            return null;
        }
    }

    /**
     * Execute SELECT query
     * @param query SQL SELECT query
     * @return List of result maps
     */
    protected List<Map<String, Object>> executeSelectQuery(String query) {
        logger.info("Executing SELECT query: {}", query);
        
        List<Map<String, Object>> results = new ArrayList<>();
        
        try {
            resultSet = statement.executeQuery(query);
            results = convertResultSetToList(resultSet);
            
            logger.info("Query executed successfully. Retrieved {} rows", results.size());
            
        } catch (SQLException e) {
            logger.error("Error executing SELECT query: {}", query, e);
            throw new RuntimeException("Query execution failed", e);
        }
        
        return results;
    }

    /**
     * Execute SELECT query with parameters
     * @param query SQL SELECT query with placeholders
     * @param parameters Query parameters
     * @return List of result maps
     */
    protected List<Map<String, Object>> executeSelectQuery(String query, Object... parameters) {
        logger.info("Executing parameterized SELECT query: {}", query);
        logger.debug("Parameters: {}", (Object) parameters);
        
        List<Map<String, Object>> results = new ArrayList<>();
        
        try {
            preparedStatement = connection.prepareStatement(query);
            setParameters(preparedStatement, parameters);
            
            resultSet = preparedStatement.executeQuery();
            results = convertResultSetToList(resultSet);
            
            logger.info("Parameterized query executed successfully. Retrieved {} rows", results.size());
            
        } catch (SQLException e) {
            logger.error("Error executing parameterized SELECT query: {}", query, e);
            throw new RuntimeException("Parameterized query execution failed", e);
        }
        
        return results;
    }

    /**
     * Execute INSERT, UPDATE, or DELETE query
     * @param query SQL query
     * @return Number of affected rows
     */
    protected int executeUpdateQuery(String query) {
        logger.info("Executing UPDATE query: {}", query);
        
        try {
            int rowsAffected = statement.executeUpdate(query);
            logger.info("Query executed successfully. {} rows affected", rowsAffected);
            return rowsAffected;
            
        } catch (SQLException e) {
            logger.error("Error executing UPDATE query: {}", query, e);
            throw new RuntimeException("Update query execution failed", e);
        }
    }

    /**
     * Execute INSERT, UPDATE, or DELETE query with parameters
     * @param query SQL query with placeholders
     * @param parameters Query parameters
     * @return Number of affected rows
     */
    protected int executeUpdateQuery(String query, Object... parameters) {
        logger.info("Executing parameterized UPDATE query: {}", query);
        logger.debug("Parameters: {}", (Object) parameters);
        
        try {
            preparedStatement = connection.prepareStatement(query);
            setParameters(preparedStatement, parameters);
            
            int rowsAffected = preparedStatement.executeUpdate();
            logger.info("Parameterized update query executed successfully. {} rows affected", rowsAffected);
            return rowsAffected;
            
        } catch (SQLException e) {
            logger.error("Error executing parameterized UPDATE query: {}", query, e);
            throw new RuntimeException("Parameterized update query execution failed", e);
        }
    }

    /**
     * Execute stored procedure
     * @param procedureName Stored procedure name
     * @param parameters Procedure parameters
     * @return List of result maps
     */
    protected List<Map<String, Object>> executeStoredProcedure(String procedureName, Object... parameters) {
        logger.info("Executing stored procedure: {}", procedureName);
        logger.debug("Parameters: {}", (Object) parameters);
        
        List<Map<String, Object>> results = new ArrayList<>();
        
        try {
            // Create callable statement
            String call = "{call " + procedureName + "(" + getParameterPlaceholders(parameters.length) + ")}";
            CallableStatement callableStatement = connection.prepareCall(call);
            
            // Set parameters
            for (int i = 0; i < parameters.length; i++) {
                callableStatement.setObject(i + 1, parameters[i]);
            }
            
            // Execute procedure
            boolean hasResultSet = callableStatement.execute();
            
            if (hasResultSet) {
                resultSet = callableStatement.getResultSet();
                results = convertResultSetToList(resultSet);
            }
            
            logger.info("Stored procedure executed successfully");
            callableStatement.close();
            
        } catch (SQLException e) {
            logger.error("Error executing stored procedure: {}", procedureName, e);
            throw new RuntimeException("Stored procedure execution failed", e);
        }
        
        return results;
    }

    /**
     * Get single value from database
     * @param query SQL query
     * @param columnName Column name
     * @return Single value
     */
    protected Object getSingleValue(String query, String columnName) {
        logger.info("Getting single value from query: {}", query);
        
        List<Map<String, Object>> results = executeSelectQuery(query);
        
        if (results.isEmpty()) {
            logger.warn("No results found for query");
            return null;
        }
        
        Object value = results.get(0).get(columnName);
        logger.debug("Retrieved single value: {}", value);
        return value;
    }

    /**
     * Get row count from table
     * @param tableName Table name
     * @return Row count
     */
    protected int getRowCount(String tableName) {
        String query = "SELECT COUNT(*) as count FROM " + tableName;
        Object count = getSingleValue(query, "count");
        return count != null ? ((Number) count).intValue() : 0;
    }

    /**
     * Get row count with condition
     * @param tableName Table name
     * @param condition WHERE condition
     * @return Row count
     */
    protected int getRowCount(String tableName, String condition) {
        String query = "SELECT COUNT(*) as count FROM " + tableName + " WHERE " + condition;
        Object count = getSingleValue(query, "count");
        return count != null ? ((Number) count).intValue() : 0;
    }

    /**
     * Check if record exists
     * @param tableName Table name
     * @param condition WHERE condition
     * @return true if record exists, false otherwise
     */
    protected boolean recordExists(String tableName, String condition) {
        int count = getRowCount(tableName, condition);
        boolean exists = count > 0;
        logger.info("Record exists in {}: {}", tableName, exists);
        return exists;
    }

    /**
     * Validate data in database
     * @param tableName Table name
     * @param expectedData Expected data map
     * @param condition WHERE condition
     * @return true if validation passes, false otherwise
     */
    protected boolean validateData(String tableName, Map<String, Object> expectedData, String condition) {
        logger.info("Validating data in table: {}", tableName);
        
        String query = "SELECT * FROM " + tableName + " WHERE " + condition;
        List<Map<String, Object>> results = executeSelectQuery(query);
        
        if (results.isEmpty()) {
            logger.error("No records found for validation");
            return false;
        }
        
        Map<String, Object> actualData = results.get(0);
        
        for (Map.Entry<String, Object> entry : expectedData.entrySet()) {
            String column = entry.getKey();
            Object expectedValue = entry.getValue();
            Object actualValue = actualData.get(column);
            
            if (!compareValues(expectedValue, actualValue)) {
                logger.error("Data validation failed for column '{}'. Expected: {}, Actual: {}", 
                    column, expectedValue, actualValue);
                return false;
            }
        }
        
        logger.info("Data validation passed");
        return true;
    }

    /**
     * Begin database transaction
     */
    protected void beginTransaction() {
        try {
            connection.setAutoCommit(false);
            logger.info("Database transaction started");
        } catch (SQLException e) {
            logger.error("Error starting transaction", e);
            throw new RuntimeException("Transaction start failed", e);
        }
    }

    /**
     * Commit database transaction
     */
    protected void commitTransaction() {
        try {
            connection.commit();
            connection.setAutoCommit(true);
            logger.info("Database transaction committed");
        } catch (SQLException e) {
            logger.error("Error committing transaction", e);
            throw new RuntimeException("Transaction commit failed", e);
        }
    }

    /**
     * Rollback database transaction
     */
    protected void rollbackTransaction() {
        try {
            connection.rollback();
            connection.setAutoCommit(true);
            logger.info("Database transaction rolled back");
        } catch (SQLException e) {
            logger.error("Error rolling back transaction", e);
            throw new RuntimeException("Transaction rollback failed", e);
        }
    }

    /**
     * Convert ResultSet to List of Maps
     * @param rs ResultSet
     * @return List of result maps
     */
    private List<Map<String, Object>> convertResultSetToList(ResultSet rs) throws SQLException {
        List<Map<String, Object>> results = new ArrayList<>();
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
        
        return results;
    }

    /**
     * Set parameters for prepared statement
     * @param ps PreparedStatement
     * @param parameters Parameters array
     */
    private void setParameters(PreparedStatement ps, Object... parameters) throws SQLException {
        for (int i = 0; i < parameters.length; i++) {
            ps.setObject(i + 1, parameters[i]);
        }
    }

    /**
     * Generate parameter placeholders for prepared statement
     * @param count Parameter count
     * @return Placeholder string
     */
    private String getParameterPlaceholders(int count) {
        StringBuilder placeholders = new StringBuilder();
        for (int i = 0; i < count; i++) {
            if (i > 0) placeholders.append(", ");
            placeholders.append("?");
        }
        return placeholders.toString();
    }

    /**
     * Compare two values considering null values
     * @param expected Expected value
     * @param actual Actual value
     * @return true if values match, false otherwise
     */
    private boolean compareValues(Object expected, Object actual) {
        if (expected == null && actual == null) return true;
        if (expected == null || actual == null) return false;
        return expected.toString().equals(actual.toString());
    }

    /**
     * Check if connection is closed
     * @param conn Connection to check
     * @return true if closed, false otherwise
     */
    private boolean isClosed(Connection conn) {
        try {
            return conn == null || conn.isClosed();
        } catch (SQLException e) {
            return true;
        }
    }

    /**
     * Close database resources
     */
    private void closeResources() {
        try {
            if (resultSet != null && !resultSet.isClosed()) {
                resultSet.close();
            }
            if (preparedStatement != null && !preparedStatement.isClosed()) {
                preparedStatement.close();
            }
            if (statement != null && !statement.isClosed()) {
                statement.close();
            }
        } catch (SQLException e) {
            logger.error("Error closing database resources", e);
        }
    }

    /**
     * Close all connections (call this in test suite teardown)
     */
    public static void closeAllConnections() {
        for (Connection conn : connections.values()) {
            try {
                if (conn != null && !conn.isClosed()) {
                    conn.close();
                }
            } catch (SQLException e) {
                // Log but don't throw
            }
        }
        connections.clear();
    }
}