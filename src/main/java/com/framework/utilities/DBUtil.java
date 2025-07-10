package com.framework.utilities;

import com.framework.config.ConfigManager;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBUtil {

    private static HikariDataSource dataSource;

    static {
        ConfigManager cfg = ConfigManager.getInstance();
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(cfg.get("dbUrl"));
        config.setUsername(cfg.get("dbUser"));
        config.setPassword(cfg.get("dbPassword"));
        config.setMaximumPoolSize(5);
        dataSource = new HikariDataSource(config);
    }

    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    public static ResultSet executeQuery(String sql) throws SQLException {
        try (Connection con = getConnection(); Statement st = con.createStatement()) {
            return st.executeQuery(sql);
        }
    }

    public static int executeUpdate(String sql) throws SQLException {
        try (Connection con = getConnection(); Statement st = con.createStatement()) {
            return st.executeUpdate(sql);
        }
    }
}