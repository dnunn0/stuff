package com.whatgameapps.firefly.controller;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.builder.ResponseSpecBuilder;
import com.jayway.restassured.response.Response;
import com.jayway.restassured.specification.ResponseSpecification;
import com.whatgameapps.firefly.com.whatgameapps.firefly.helper.TestUtils;
import org.eclipse.jetty.http.HttpStatus;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.hamcrest.CoreMatchers.containsString;

public class AllianceSectorNavControllerIntegratedTest {
    private static TestUtils testUtils;
    private PrintStream originalOut;
    private ByteArrayOutputStream output;

    @BeforeClass
    public static void setUp() {
        testUtils = new TestUtils(9876);
    }

    @AfterClass
    public static void tearDown() {
        testUtils.cleanup();
    }


    @Before
    public void resetDeck() {
        RestAssured.delete(AllianceSectorNavController.PATH).then().statusCode(HttpStatus.OK_200);
    }

    @Test
    public void shouldReturn200WhenSuccessful() throws Exception {
        fly(getSpecBuilder().build());
    }

    private Response fly(ResponseSpecification spec) {
        Response response = RestAssured.get(AllianceSectorNavController.PATH).andReturn();
        response.then().spec(spec);
        return response;
    }

    private ResponseSpecBuilder getSpecBuilder() {
        return testUtils.getSpecBuilder(HttpStatus.OK_200);
    }

    @Test
    public void shouldReplyWithJsonCard() throws Exception {
        ResponseSpecBuilder builder = getSpecBuilder();
        builder.expectBody("action", containsString("Alliance"));

        fly(builder.build());
    }

}
