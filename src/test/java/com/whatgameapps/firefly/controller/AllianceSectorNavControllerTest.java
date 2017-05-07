package com.whatgameapps.firefly.controller;

import com.whatgameapps.firefly.AllianceNavDeckSpecification;
import com.whatgameapps.firefly.com.whatgameapps.firefly.helper.TestUtils;
import com.whatgameapps.firefly.rest.AllianceNavCard;
import org.eclipse.jetty.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import spark.Request;
import spark.SparkRequestStub;
import spark.SparkResponseWrapper;

import java.util.stream.IntStream;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class AllianceSectorNavControllerTest {
    private final TestUtils testUtils = new TestUtils();
    private final AllianceSectorNavController sut = new AllianceSectorNavController(AllianceNavDeckSpecification.BASIC);
    private final Request req = new SparkRequestStub();
    private final spark.Response res = new SparkResponseWrapper();


    @After
    public void restoreStdout() {
        testUtils.restoreStdout();
    }

    @Before
    public void resetDeck() {
        sut.reset(req, res);
    }

    @Test
    public void shouldReplyWithCard() throws Exception {
        final AllianceNavCard card = sut.drawCard(req, res);
        assertNotNull(card);

    }

    @Test
    public void shouldLogRequestToStdout() throws Exception {
        testUtils.redirectStdout();
        sut.drawCard(req, res);
        String result = testUtils.outStream.toString();
        assertThat(result, containsString(AllianceSectorNavController.PREFIX));
    }

    @Test
    public void shouldEventuallyRunOutOfCardsWithError() {
        int expectedCardCount = sut.deck.spec.count;
        boolean foundMatch = IntStream.rangeClosed(1, expectedCardCount + 1)
                .map(i -> {
                    sut.drawCard(req, res);
                    return res.status();
                })
                .anyMatch(c -> AllianceSectorNavController.NOT_FOUND_ERROR == c);

        assertTrue(foundMatch);
    }

    @Test
    public void shouldNotBeAbleTShuffledWhenLocked() {
        sut.lock(req, res);
        sut.reset(req, res);
        assertEquals(AllianceSectorNavController.LOCK_ERROR, res.status());
    }

    @Test
    public void shouldNotBeAbleToDrawCardAfterDoubleLocking() {
        sut.lock(req, res);
        shouldNotBeAbleToGetCardWhenLocked();
    }

    @Test
    public void shouldNotBeAbleToGetCardWhenLocked() {
        sut.lock(req, res);
        AllianceNavCard card = sut.drawCard(req, res);
        assertNull(card);
        assertEquals(AllianceSectorNavController.LOCK_ERROR, res.status());
    }

    @Test
    public void shouldBeAbleToGetCardAfterUnlocked() {
        sut.lock(req, res);
        sut.unlock(req, res);
        AllianceNavCard card = sut.drawCard(req, res);
        assertNotNull(card);
        assertEquals(HttpStatus.OK_200, res.status());
    }

    @Test
    public void shouldBeAbleTShuffledAfterUnlocked() {
        sut.lock(req, res);
        sut.reset(req, res);
        assertEquals(AllianceSectorNavController.LOCK_ERROR, res.status());
    }

}
