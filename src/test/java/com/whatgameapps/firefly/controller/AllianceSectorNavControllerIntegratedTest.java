package com.whatgameapps.firefly.controller;

import com.google.gson.Gson;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.builder.ResponseSpecBuilder;
import com.jayway.restassured.response.Response;
import com.jayway.restassured.specification.ResponseSpecification;
import com.whatgameapps.firefly.AllianceNavDeck;
import com.whatgameapps.firefly.AllianceNavDeckSpecification;
import com.whatgameapps.firefly.com.whatgameapps.firefly.helper.TestUtils;
import com.whatgameapps.firefly.rest.AllianceNavCard;
import org.eclipse.jetty.http.HttpStatus;
import org.junit.After;
import org.junit.Test;

import static org.hamcrest.core.StringContains.containsString;

public class AllianceSectorNavControllerIntegratedTest {
    private static Gson gson = new Gson();
    private TestUtils testUtils;

    @After
    public void tearDown() {
        testUtils.cleanup();
    }

    @Test
    public void shouldReturn200WhenSuccessful() throws Exception {
        testUtils = new TestUtils(9876, "BASIC");
        fly(getSpecBuilder().build());
    }

    private Response fly(ResponseSpecification spec) {
        Response response = RestAssured.when().get(AllianceSectorNavController.PATH).andReturn();
        response.then().spec(spec);
        return response;
    }

    private ResponseSpecBuilder getSpecBuilder() {
        return testUtils.getSpecBuilder(HttpStatus.OK_200);
    }

    @Test
    public void shouldReplyWithJsonCard() throws Exception {
        testUtils = new TestUtils(8888, "RESHUFFLE");
        ResponseSpecBuilder builder = getSpecBuilder();
        builder.expectContent(containsString(getTopCardAsJson()));

        fly(builder.build());
    }

    private String getTopCardAsJson() {
        AllianceNavDeck deck = new AllianceNavDeck(AllianceNavDeckSpecification.RESHUFFLE);
        final AllianceNavCard card = deck.take().get();
        return gson.toJson(card);
    }

    @Test
    public void flyingPastReshuffleGivesError() {
        testUtils = new TestUtils(9876, "RESHUFFLE");
        fly(getSpecBuilder().build());
        fly(testUtils.getSpecBuilder(AllianceSectorNavController.NOT_FOUND_ERROR).build());
    }

    @Test
    public void postingReshufflesDeck() {
        testUtils = new TestUtils(9876, "RESHUFFLE");
        fly(getSpecBuilder().build());
        RestAssured.when().post(AllianceSectorNavController.PATH).then().statusCode(HttpStatus.OK_200);
        fly(getSpecBuilder().build());
    }

}
