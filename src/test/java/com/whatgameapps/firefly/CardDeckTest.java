package com.whatgameapps.firefly;

import com.google.common.collect.ImmutableMultimap;
import com.whatgameapps.firefly.com.whatgameapps.firefly.helper.TestUtils;
import com.whatgameapps.firefly.rest.NavCard;
import org.junit.After;
import org.junit.Test;

import java.util.Collections;
import java.util.Optional;
import java.util.stream.IntStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CardDeckTest {
    private final TestUtils testUtils = new TestUtils();
    private CardDeck sut = createDeck();

    @After
    public void restoreStdout() {
        testUtils.restoreStdout();
    }

    @Test
    public void deckContainsRightNumberOfCardsBasicDeck() {
        createAndCheckDeck(AllianceNavDeckSpecification.BASIC);
    }

    private void createAndCheckDeck(final AllianceNavDeckSpecification expectedDeck) {
        final CardDeck sut = CardDeck.NewFrom(expectedDeck, new ArchiveInMemory());
        expectedDeck.entrySet().stream().forEach((spec) ->
                assertEquals(spec.getElement(), spec.getCount(), sut.countCards(spec.getElement())));
    }

    @Test
    public void deckContainsRightNumberOfCardsKalidasaDeck() {
        AllianceNavDeckSpecification spec = AllianceNavDeckSpecification.KALIDASA;
        final CardDeck sut = CardDeck.NewFrom(spec, new ArchiveInMemory());
        assertEquals(spec.count, sut.size());
    }

    @Test
    public void combinesSpecsCorrectly() {
        AllianceNavDeckSpecification spec2 = new AllianceNavDeckSpecification(AllianceNavDeckSpecification.RESHUFFLE, 4, ImmutableMultimap.<String, Integer>builder()
                .put("Alliance Cruiser - Reshuffle", 3)
                .build());
        final CardDeck sut = CardDeck.NewFrom(spec2, new ArchiveInMemory());
        assertEquals(spec2.count, sut.size());
    }

    @Test
    public void deckShuffledAfterCreation() {
        sut = createDeck();
        testUtils.redirectStdout();
        int deckCount = 20000;
        String cardToFind = sut.take().get().action;
        long count = Collections.nCopies(deckCount, 1)
                .stream()
                .map(i -> createDeck())
                .map(deck -> deck.take().get())
                .map(c -> c.action)
                .filter(cardToFind::equals)
                .count();

        CardDeck stdDeck = createDeck();
        assertEquals(((double) stdDeck.countCards(cardToFind)) / stdDeck.size(), ((double) count) / deckCount, 0.02);
    }

    private CardDeck createDeck() {
        return CardDeck.NewFrom(AllianceNavDeckSpecification.BASIC, new ArchiveInMemory());
    }

    @Test
    public void takingACardDiscardsIt() {
        final int originalDiscardCount = sut.discards().size();
        final Optional<NavCard> card = sut.take();
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
        Optional<NavCard> card;
        do {
            card = sut.take();
        } while (!card.get().isReshuffle());
    }

    @Test
    public void cantTakeCardsAftePulledAll() {
        IntStream.rangeClosed(1, sut.spec.count)
                .forEach(i -> sut.take());
        final Optional<NavCard> card = sut.take();
        assertTrue(!card.isPresent());
    }

}

