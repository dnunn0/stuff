package com.whatgameapps.firefly;

import com.google.gson.Gson;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.builder.ResponseSpecBuilder;
import com.jayway.restassured.response.Response;
import com.jayway.restassured.specification.ResponseSpecification;
import com.whatgameapps.firefly.com.whatgameapps.firefly.helper.TestUtils;
import com.whatgameapps.firefly.controller.NavController;
import com.whatgameapps.firefly.rest.NavDeckStatus;
import org.eclipse.jetty.http.HttpStatus;
import org.junit.After;
import org.junit.Test;

import static org.hamcrest.core.StringContains.containsString;

public class MainJoinPreviousGameIntegratedTest {
    private static final String NAV_PATH = NavController.ALLIANCE_SPACE + NavController.NAV;
    private static final String STATUS_PATH = NAV_PATH + NavController.STATUS;
    private TestUtils testUtils = new TestUtils(8888, "RESHUFFLE");

    @After
    public void tearDown() {
        testUtils.cleanup();
    }

    @Test
    public void shouldJoinPreviousGameWhenAskedToDoSo() {
        fly(getSpecBuilder().build());
        final TestUtils test2 = new TestUtils(testUtils.main.spark().port() + 1, "join");
        try {
            final String expectedStatus = new Gson().toJson(new NavDeckStatus(NAV_PATH, 0, 1, false));
            ResponseSpecification statusSpec = getSpecBuilder().expectContent(containsString(expectedStatus)).build();
            Response response = RestAssured.when().get(STATUS_PATH).andReturn();
            response.then().spec(statusSpec);
        } finally {
            test2.cleanup();
        }
    }

    private Response fly(ResponseSpecification spec) {
        Response response = RestAssured.when().get(NAV_PATH).andReturn();
        response.then().spec(spec);
        return response;
    }

    private ResponseSpecBuilder getSpecBuilder() {
        return testUtils.getSpecBuilder(HttpStatus.OK_200);
    }
}
