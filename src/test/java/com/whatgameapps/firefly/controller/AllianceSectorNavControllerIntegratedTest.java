package com.whatgameapps.firefly.controller;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.builder.ResponseSpecBuilder;
import com.jayway.restassured.response.Response;
import com.jayway.restassured.specification.ResponseSpecification;
import com.whatgameapps.firefly.com.whatgameapps.firefly.helper.TestUtils;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;

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
    public void redirectStdout() throws Exception {
        originalOut = System.out;
        output = new ByteArrayOutputStream();
        PrintStream stream = new PrintStream(output, true, StandardCharsets.UTF_8.name());
        System.setOut(stream);
    }

    @After
    public void restoreStdout() {
        System.setOut(originalOut);
    }

    @Test
    public void shouldReturn200WhenSuccessful() throws Exception {
        fly(getSpecBuilder().build());
    }

    private ResponseSpecBuilder getSpecBuilder() {
        return testUtils.getSpecBuilder(200);
    }

    @Test
    public void shouldLogRequestToStdout() throws Exception {
        fly(getSpecBuilder().build());

        String result = new String(output.toByteArray(), StandardCharsets.UTF_8);
        assertThat(result, containsString("Received"));
    }

    @Test
    public void shouldReplyWithCard() throws Exception {
        ResponseSpecBuilder builder = getSpecBuilder();
        builder.expectBody("action", containsString("Alliance"));

        fly(builder.build());
    }

    private void fly(ResponseSpecification spec) {
        Response response = RestAssured.get(AllianceSectorNavController.PATH).andReturn();
        response.then().spec(spec);
    }

   }
