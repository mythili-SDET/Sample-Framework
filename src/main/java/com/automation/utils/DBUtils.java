package com.automation.utils;

import com.automation.db.DBConnectionManager;
import com.automation.logger.LoggerManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBUtils {
    
    private static final Logger logger = LoggerManager.getDBLogger();

    public static int executeUpdate(String sql, Object... params) throws SQLException {
        LoggerManager.logDBOperation("UPDATE", sql, params);
        try (Connection conn = DBConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            setParameters(ps, params);
            int result = ps.executeUpdate();
            logger.info("DB Update executed successfully, affected rows: {}", result);
            return result;
        } catch (SQLException e) {
            LoggerManager.logError(logger, "DB Update", "Failed to execute update", e);
            throw e;
        }
    }

    public static ResultSet executeQuery(String sql, Object... params) throws SQLException {
        LoggerManager.logDBOperation("SELECT", sql, params);
        try {
            Connection conn = DBConnectionManager.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            setParameters(ps, params);
            ResultSet result = ps.executeQuery();
            logger.info("DB Query executed successfully");
            return result;
        } catch (SQLException e) {
            LoggerManager.logError(logger, "DB Query", "Failed to execute query", e);
            throw e;
        }
    }

    private static void setParameters(PreparedStatement ps, Object... params) throws SQLException {
        for (int i = 0; i < params.length; i++) {
            ps.setObject(i + 1, params[i]);
        }
    }
}
