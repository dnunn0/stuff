package com.whatgameapps.firefly.controller;

import com.google.common.collect.ImmutableMultimap;
import com.whatgameapps.firefly.AllianceNavDeckSpecification;
import com.whatgameapps.firefly.com.whatgameapps.firefly.helper.TestUtils;
import com.whatgameapps.firefly.rest.NavCard;
import com.whatgameapps.firefly.rest.NavDeckStatus;
import org.eclipse.jetty.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import spark.Request;
import spark.SparkRequestStub;
import spark.SparkResponseWrapper;

import java.util.stream.IntStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class AllianceSpaceNavControllerTest {
    private final static AllianceNavDeckSpecification noReshuffleCardSpec = new AllianceNavDeckSpecification(3, ImmutableMultimap.<String, Integer>builder()
            .build());
    private final TestUtils testUtils = new TestUtils();
    private final Request req = new SparkRequestStub();
    private final spark.Response res = new SparkResponseWrapper();
    private final StatusBroadcaster middleman = new StatusBroadcaster();
    private AllianceSpaceNavController sut = new AllianceSpaceNavController(AllianceNavDeckSpecification.BASIC, middleman);

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
        final NavCard card = sut.drawCard(req, res);
        assertNotNull(card);

    }

    @Test
    public void shouldEventuallyRunOutOfCardsWithError() {
        int expectedCardCount = sut.deck.spec.count;
        boolean foundMatch = IntStream.rangeClosed(1, expectedCardCount + 1)
                .map(i -> {
                    sut.drawCard(req, res);
                    return res.status();
                })
                .anyMatch(c -> NavController.NOT_FOUND_ERROR == c);

        assertTrue(foundMatch);
    }

    @Test
    public void shouldNotBeAbleTShuffledWhenLocked() {
        sut.lock(req, res);
        sut.reset(req, res);
        assertEquals(NavController.LOCK_ERROR, res.status());
    }

    @Test
    public void shouldNotBeAbleToDrawCardAfterDoubleLocking() {
        sut.lock(req, res);
        shouldNotBeAbleToGetCardWhenLocked();
    }

    @Test
    public void shouldNotBeAbleToGetCardWhenLocked() {
        sut.lock(req, res);
        NavCard card = sut.drawCard(req, res);
        assertNull(card);
        assertEquals(NavController.LOCK_ERROR, res.status());
    }

    @Test
    public void shouldBeAbleToGetCardAfterUnlocked() {
        sut.lock(req, res);
        sut.unlock(req, res);
        NavCard card = sut.drawCard(req, res);
        assertNotNull(card);
        assertEquals(HttpStatus.OK_200, res.status());
    }

    @Test
    public void shouldBeAbleTShuffledAfterUnlocked() {
        sut.lock(req, res);
        sut.reset(req, res);
        assertEquals(NavController.LOCK_ERROR, res.status());
    }

    @Test
    public void statusAfterCreationShouldBeRight() {
        checkStatus(sut.deck.spec.count, 0, false, sut.status(req, res));
    }

    private void checkStatus(int expectedCardCount, int expectedDiscardCount, boolean expectedIsLocked, NavDeckStatus status) {
        assertEquals("card count", expectedCardCount, status.remainingCardsCount);
        assertEquals("discard count", expectedDiscardCount, status.discardsCount);
        assertEquals("lock state", expectedIsLocked, status.isLocked);
    }

    @Test
    public void statusAfterDrawingOneCardShouldBeRight() {
        sut = new AllianceSpaceNavController(noReshuffleCardSpec, middleman);
        sut.drawCard(req, res);
        checkStatus(sut.deck.spec.count - 1, 1, false, sut.status(req, res));
    }

    @Test
    public void statusAfterDrawingAllCardsShouldBeRight() {
        sut = new AllianceSpaceNavController(noReshuffleCardSpec, middleman);
        IntStream.range(0, noReshuffleCardSpec.count).forEachOrdered(i -> sut.drawCard(req, res));
        checkStatus(0, sut.deck.spec.count, false, sut.status(req, res));
    }

    @Test
    public void statusAfterLockingShouldBeRight() {
        sut = new AllianceSpaceNavController(noReshuffleCardSpec, middleman);
        IntStream.range(0, noReshuffleCardSpec.count).forEachOrdered(i -> sut.drawCard(req, res));
        sut.lock(req, res);
        checkStatus(0, sut.deck.spec.count, true, sut.status(req, res));
    }

    @Test
    public void statusAfterUnLockingShouldBeRight() {
        sut = new AllianceSpaceNavController(noReshuffleCardSpec, middleman);
        sut.lock(req, res);
        sut.unlock(req, res);
        checkStatus(sut.deck.spec.count, 0, false, sut.status(req, res));
    }

}
