package com.whatgameapps.firefly.controller;

import com.whatgameapps.firefly.AllianceNavDeckSpecification;
import com.whatgameapps.firefly.rest.AllianceNavCard;
import org.eclipse.jetty.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import spark.Request;
import spark.SparkRequestStub;
import spark.SparkResponseWrapper;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.stream.IntStream;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class AllianceSectorNavControllerTest {
    private final AllianceSectorNavController sut = new AllianceSectorNavController(AllianceNavDeckSpecification.BASIC);
    private final Request req = new SparkRequestStub();
    private final spark.Response res = new SparkResponseWrapper();
    private PrintStream originalOut;
    private ByteArrayOutputStream output;

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
        //       System.out.println(output.toString());
    }

    @Before
    public void resetDeck() {
        sut.resetDeck(req, res);
    }

    @Test
    public void shouldReplyWithCard() throws Exception {
        final AllianceNavCard card = sut.drawCard(req, res);
        assertNotNull(card);

    }

    @Test
    public void shouldLogRequestToStdout() throws Exception {
        sut.drawCard(req, res);
        String result = new String(output.toByteArray(), StandardCharsets.UTF_8);
        assertThat(result, containsString(AllianceSectorNavController.PREFIX));
    }

    @Test
    public void shouldEventuallyRunOutOfCardsWithError() {
        final boolean gotExpectedError = IntStream.range(1, AllianceNavDeckSpecification.BASIC.count + 1)
                .map(i -> {
                    sut.drawCard(req, res);
                    return res.status();
                })
                .anyMatch(status ->
                        HttpStatus.REQUESTED_RANGE_NOT_SATISFIABLE_416 == status);
        assertTrue(gotExpectedError);
    }

}
