package com.automation.db;

import com.automation.config.ConfigManager;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnectionManager {

    private static String jdbcUrl;
    private static String username;
    private static String password;

    static {
        ConfigManager config = ConfigManager.getInstance();
        String host = config.getDBHost();
        int port = config.getDBPort();
        String dbName = config.getDBName();
        username = config.getDBUsername();
        password = config.getDBPassword();
        jdbcUrl = String.format("jdbc:mysql://%s:%d/%s", host, port, dbName);
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("MySQL JDBC Driver not found", e);
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(jdbcUrl, username, password);
    }

    public static void close() throws SQLException {
        // no-op for DriverManager
    }
}
