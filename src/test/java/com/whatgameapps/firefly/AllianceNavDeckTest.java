package com.whatgameapps.firefly;

import com.whatgameapps.firefly.com.whatgameapps.firefly.helper.TestUtils;
import com.whatgameapps.firefly.rest.AllianceNavCard;
import org.junit.After;
import org.junit.Test;

import java.util.Collections;
import java.util.Optional;
import java.util.stream.IntStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class AllianceNavDeckTest {
    private final TestUtils testUtils = new TestUtils();
    private final AllianceNavDeck sut = new AllianceNavDeck(AllianceNavDeckSpecification.BASIC);

    @After
    public void restoreStdout() {
        testUtils.restoreStdout();
    }

    @Test
    public void deckContainsRightNumberOfCardsBasicDeck() {
        createAndCheckDeck(AllianceNavDeckSpecification.BASIC);
    }

    private void createAndCheckDeck(final AllianceNavDeckSpecification expectedDeck) {
        final AllianceNavDeck sut = new AllianceNavDeck(expectedDeck);
        expectedDeck.entrySet().forEach((spec) ->
                assertEquals(spec.getKey().action, (int) spec.getValue(), sut.countCards(spec.getKey())));
    }

    @Test
    public void deckContainsRightNumberOfCardsKalidasaDeck() {
        AllianceNavDeckSpecification spec = AllianceNavDeckSpecification.KALIDASA;
        final AllianceNavDeck sut = new AllianceNavDeck(spec);
        assertEquals(spec.count, sut.size());
    }

    @Test
    public void deckShuffledAfterCreation() {
        testUtils.redirectStdout();
        int deckCount = 20000;
        long count = Collections.nCopies(deckCount, 1)
                .stream()
                .map(i -> new AllianceNavDeck(AllianceNavDeckSpecification.BASIC))
                .map(deck -> deck.take().get())
                .filter(AllianceNavCard.KEEP_FLYING::equals)
                .count();

        AllianceNavDeck stdDeck = new AllianceNavDeck(AllianceNavDeckSpecification.BASIC);
        assertEquals(((double) stdDeck.countCards(AllianceNavCard.KEEP_FLYING)) / stdDeck.size(), ((double) count) / deckCount, 0.02);
    }

    @Test
    public void takingACardDiscardsIt() {
        final int originalDiscardCount = sut.discards().size();
        final Optional<AllianceNavCard> card = sut.take();
        assertEquals(originalDiscardCount + 1, sut.discards().size());
        assertEquals(card.get(), sut.discards().peek());
    }

    @Test
    public void shuffleRestoresCards() {
        sut.take();
        sut.shuffle();
        this.deckContainsRightNumberOfCards();
    }

    @Test
    public void deckContainsRightNumberOfCards() {
        assertEquals(sut.spec.count, sut.size());
        assertEquals(0, sut.discards().size());
    }

    @Test
    public void cantTakeCardsAfterReshufflePulled() {
        drainCards();
        assertTrue(!sut.take().isPresent());
    }

    private void drainCards() {
        Optional<AllianceNavCard> card;
        do {
            card = sut.take();
        } while (!AllianceNavCard.RESHUFFLE.equals(card.get()));
    }

    @Test
    public void cantTakeCardsAftePulledAll() {
        IntStream.rangeClosed(1, sut.spec.count)
                .forEach(i -> sut.take());
        final Optional<AllianceNavCard> card = sut.take();
        assertTrue(!card.isPresent());
    }

}
