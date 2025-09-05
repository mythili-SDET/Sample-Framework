package com.automation.db;

import org.apache.commons.dbcp2.BasicDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class DBConnectionManager {

    private static BasicDataSource dataSource;

    static {
        dataSource = new BasicDataSource();
        dataSource.setDriverClassName("oracle.jdbc.driver.OracleDriver");
        dataSource.setUrl("jdbc:oracle:thin:@//localhost:1521/orclpdb1"); // Change to your host, port, service name
        dataSource.setUsername("your_oracle_username");
        dataSource.setPassword("your_oracle_password");

        // Connection pool settings
        dataSource.setInitialSize(5);
        dataSource.setMaxTotal(10);
        dataSource.setMinIdle(2);
        dataSource.setMaxIdle(5);

        dataSource.setValidationQuery("SELECT 1 FROM DUAL"); // Oracle specific test query
        dataSource.setTestOnBorrow(true);
    }

    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    public static void close() throws SQLException {
        if (dataSource != null) {
            dataSource.close();
        }
    }
}
