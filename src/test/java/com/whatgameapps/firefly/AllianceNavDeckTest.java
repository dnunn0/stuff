package com.whatgameapps.firefly;

import com.google.common.collect.ImmutableMap;
import com.whatgameapps.firefly.rest.AllianceNavCard;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class AllianceNavDeckTest {

    @Test
    public void deckContainsRightNumberOfCards() {
        final AllianceNavDeck sut = new AllianceNavDeck(AllianceNavDeckSpecification.BASIC);
        assertEquals(40, sut.size());
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

    private void createAndCheckDeck(final ImmutableMap<AllianceNavCard, Integer> expectedDeck) {
        final AllianceNavDeck sut = new AllianceNavDeck(expectedDeck);
        expectedDeck.entrySet().forEach((spec) ->
                assertEquals(spec.getKey().action, (int) spec.getValue(), sut.countCards(spec.getKey())));
    }

}
