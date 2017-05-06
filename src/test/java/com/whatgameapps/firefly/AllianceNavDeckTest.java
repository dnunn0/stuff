package com.whatgameapps.firefly;

import com.whatgameapps.firefly.rest.AllianceNavCard;
import org.junit.Test;

import java.util.Collections;

import static org.junit.Assert.assertEquals;

public class AllianceNavDeckTest {

    AllianceNavDeck sut = new AllianceNavDeck(AllianceNavDeckSpecification.BASIC);

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
        createAndCheckDeck(AllianceNavDeckSpecification.KALIDASA);
    }

    @Test
    public void deckShuffledAfterCreation() {
        int deckCount = 50000;
        long count = Collections.nCopies(deckCount, 1)
                .stream()
                .map(i -> new AllianceNavDeck(AllianceNavDeckSpecification.BASIC))
                .map(deck -> deck.take())
                .filter(AllianceNavCard.KEEP_FLYING::equals)
                .count();

        AllianceNavDeck stdDeck = new AllianceNavDeck(AllianceNavDeckSpecification.BASIC);
        assertEquals(((double) stdDeck.countCards(AllianceNavCard.KEEP_FLYING)) / stdDeck.size(), ((double) count) / deckCount, 0.02);
    }

    @Test
    public void takingACardDiscardsIt() {
        final int originalDiscardCount = sut.discards().size();
        final AllianceNavCard card = sut.take();
        assertEquals(originalDiscardCount + 1, sut.discards().size());
        assertEquals(card, sut.discards().peek());
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
//        sut.forEach(System.out::println);
    }

}
