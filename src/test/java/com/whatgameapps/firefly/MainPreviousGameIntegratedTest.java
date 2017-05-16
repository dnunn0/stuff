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
import static org.junit.Assert.assertEquals;

public class MainPreviousGameIntegratedTest {
    private static final String BORDER_NAV = NavController.BORDER_SPACE + NavController.NAV;
    private static final String RIM_NAV = NavController.RIM_SPACE + NavController.NAV;
    private static final String STATUS_PATH = RIM_NAV + NavController.STATUS;
    private TestUtils testUtils = new TestUtils(8888, "RESHUFFLE");

    @After
    public void tearDown() {
        testUtils.cleanup();
    }

    @Test
    public void shouldTakeTakeFromPreviousGame() {
        fly(getSpecBuilder().build(), BORDER_NAV);
        fly(getSpecBuilder().build(), RIM_NAV);

        final TestUtils test2 = new TestUtils(testUtils.main.spark().port() + 1, "join");
        try {
            final String expectedStatus = new Gson().toJson(new NavDeckStatus(RIM_NAV, 0, 1, false));
            ResponseSpecification statusSpec = getSpecBuilder().expectContent(containsString(expectedStatus)).build();
            Response response = RestAssured.when().get(STATUS_PATH).andReturn();
            response.then().spec(statusSpec);
        } finally {
            test2.cleanup();
        }
    }

    private Response fly(ResponseSpecification spec, String path) {
        Response response = RestAssured.when().get(path).andReturn();
        response.then().spec(spec);
        return response;
    }

    private ResponseSpecBuilder getSpecBuilder() {
        return testUtils.getSpecBuilder(HttpStatus.OK_200);
    }

    @Test
    public void shouldNotAddMoreDecksToGame() {
        final TestUtils test2 = new TestUtils(testUtils.main.spark().port() + 1, "join");
        assertEquals(3, test2.main.getMetadata().split("" + Main.METADATA_DELIMITER).length);
    }
}
