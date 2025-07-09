package com.example.tests.db;

import com.example.framework.utils.DBUtils;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.sql.ResultSet;
import java.sql.SQLException;

@Test(groups = {"db"})
public class DBConnectionTest {

    public void userTableHasRecords() throws SQLException {
        ResultSet rs = DBUtils.executeQuery("SELECT COUNT(*) AS cnt FROM users");
        rs.next();
        int count = rs.getInt("cnt");
        Assert.assertTrue(count > 0, "No users found in DB");
    }
}