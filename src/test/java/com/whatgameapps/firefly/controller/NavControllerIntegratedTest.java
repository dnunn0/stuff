package com.whatgameapps.firefly.controller;

import com.google.gson.Gson;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.builder.ResponseSpecBuilder;
import com.jayway.restassured.response.Response;
import com.jayway.restassured.specification.ResponseSpecification;
import com.whatgameapps.firefly.AllianceNavDeckSpecification;
import com.whatgameapps.firefly.ArchiveInMemory;
import com.whatgameapps.firefly.CardDeck;
import com.whatgameapps.firefly.com.whatgameapps.firefly.helper.TestUtils;
import com.whatgameapps.firefly.rest.NavCard;
import org.eclipse.jetty.http.HttpStatus;
import org.hamcrest.CoreMatchers;
import org.junit.After;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.StringContains.containsString;

public class NavControllerIntegratedTest {
    private static final String NAV_PATH = NavController.ALLIANCE_SPACE + NavController.NAV;
    private static Gson gson = new Gson();
    private TestUtils testUtils = new TestUtils(8888, "RESHUFFLE");

    @After
    public void tearDown() {
        testUtils.cleanup();
    }

    @After
    public void restoreStdout() {
        testUtils.restoreStdout();
    }

    @Test
    public void shouldLogRequestToStdout() throws Exception {
        testUtils.redirectStdout();
        fly(getSpecBuilder().build());
        String result = testUtils.outStream.toString();
        assertThat(result, CoreMatchers.containsString(NAV_PATH));
    }

    private Response fly(ResponseSpecification spec) {
        Response response = RestAssured.when().get(NAV_PATH).andReturn();
        response.then().spec(spec);
        return response;
    }

    private ResponseSpecBuilder getSpecBuilder() {
        return testUtils.getSpecBuilder(HttpStatus.OK_200);
    }

    @Test
    public void shouldReturn200WhenSuccessful() throws Exception {
        fly(getSpecBuilder().build());
    }

    @Test
    public void shouldReplyWithJsonCard() throws Exception {
        ResponseSpecBuilder builder = getSpecBuilder();
        String topCardAsJson = getTopCardAsJson();
        builder.expectContent(containsString(topCardAsJson));

        fly(builder.build());
    }

    private String getTopCardAsJson() {
        CardDeck deck = CardDeck.NewFrom(AllianceNavDeckSpecification.RESHUFFLE, new ArchiveInMemory());
        final NavCard card = deck.take().get();
        return gson.toJson(card);
    }

    @Test
    public void flyingPastReshuffleGivesError() {
        fly(getSpecBuilder().build());
        fly(testUtils.getSpecBuilder(NavController.NOT_FOUND_ERROR).build());
    }

    @Test
    public void postingReshufflesDeck() {
        fly(getSpecBuilder().build());
        RestAssured.when().post(NAV_PATH).then().statusCode(HttpStatus.OK_200);
        fly(getSpecBuilder().build());
    }

}
