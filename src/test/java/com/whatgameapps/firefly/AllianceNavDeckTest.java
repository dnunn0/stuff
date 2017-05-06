package com.whatgameapps.firefly;

import com.whatgameapps.firefly.rest.AllianceNavCard;
import org.junit.Test;

import java.util.Collections;

import static org.junit.Assert.assertEquals;

public class AllianceNavDeckTest {

    @Test
    public void deckContainsRightNumberOfCards() {
        final AllianceNavDeck sut = new AllianceNavDeck(AllianceNavDeckSpecification.BASIC);
        assertEquals(AllianceNavDeckSpecification.BASIC.count, sut.size());
        sut.cards.forEach(System.out::println);
    }

    @Test
    public void deckContainsRightNumberOfCardsBasicDeck() {
        createAndCheckDeck(AllianceNavDeckSpecification.BASIC);
    }

    @Test
    public void deckContainsRightNumberOfCardsKalidasaDeck() {
        createAndCheckDeck(AllianceNavDeckSpecification.KALIDASA);
    }

    @Test
    public void deckShuffled() {
        int deckCount = 50000;
        long count = Collections.nCopies(deckCount, 1)
                .stream()
                .map((i) -> new AllianceNavDeck(AllianceNavDeckSpecification.BASIC))
                .map(deck -> deck.cards.get(0))
                .filter(card -> AllianceNavCard.KEEP_FLYING.equals(card))
                .count();

        AllianceNavDeck stdDeck = new AllianceNavDeck(AllianceNavDeckSpecification.BASIC);
        assertEquals(((double) stdDeck.countCards(AllianceNavCard.KEEP_FLYING)) / stdDeck.size(), ((double) count) / deckCount, 0.02);

    }

    private void createAndCheckDeck(final AllianceNavDeckSpecification expectedDeck) {
        final AllianceNavDeck sut = new AllianceNavDeck(expectedDeck);
        expectedDeck.entrySet().forEach((spec) ->
                assertEquals(spec.getKey().action, (int) spec.getValue(), sut.countCards(spec.getKey())));
    }

}
