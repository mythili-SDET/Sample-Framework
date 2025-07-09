package com.example.framework.utils;

import com.example.framework.config.ConfigManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;

/**
 * Utility for executing queries via JDBC.
 */
public final class DBUtils {

    private static final Logger LOG = LoggerFactory.getLogger(DBUtils.class);

    private static final String URL = ConfigManager.getInstance().getString("mysql.url");
    private static final String USER = ConfigManager.getInstance().getString("mysql.user");
    private static final String PASSWORD = ConfigManager.getInstance().getString("mysql.password");

    private DBUtils() {}

    private static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static ResultSet executeQuery(String query) {
        try {
            Connection conn = getConnection();
            Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            return stmt.executeQuery(query);
        } catch (SQLException e) {
            LOG.error("DB query failed", e);
            throw new RuntimeException(e);
        }
    }

    public static int executeUpdate(String query) {
        try (Connection conn = getConnection(); Statement stmt = conn.createStatement()) {
            return stmt.executeUpdate(query);
        } catch (SQLException e) {
            LOG.error("DB update failed", e);
            throw new RuntimeException(e);
        }
    }
}