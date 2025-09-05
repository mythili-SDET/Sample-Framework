package com.automation.utils;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBUtils {

    public static int executeUpdate(String sql, Object... params) throws SQLException {
        try (Connection conn = com.framework.core.db.DBConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            setParameters(ps, params);
            return ps.executeUpdate();
        }
    }

    public static ResultSet executeQuery(String sql, Object... params) throws SQLException {
        Connection conn = com.framework.core.db.DBConnectionManager.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql);
        setParameters(ps, params);
        return ps.executeQuery();
    }

    private static void setParameters(PreparedStatement ps, Object... params) throws SQLException {
        for (int i = 0; i < params.length; i++) {
            ps.setObject(i + 1, params[i]);
        }
    }
}
