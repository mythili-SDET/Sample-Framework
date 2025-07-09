package com.example.tests.api;

import com.example.framework.utils.APIUtils;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

@Test(groups = {"api"})
public class UserAPITest {

    public void getUserReturns200() {
        Response response = APIUtils.get("/users/1");
        Assert.assertEquals(response.getStatusCode(), 200);
    }
}