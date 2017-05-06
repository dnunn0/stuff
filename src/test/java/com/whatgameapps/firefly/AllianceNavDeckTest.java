package com.whatgameapps.firefly;

import com.whatgameapps.firefly.rest.AllianceNavCard;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.Collections;
import java.util.Optional;
import java.util.stream.IntStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class AllianceNavDeckTest {

    @Rule
    public ExpectedException expectedExcetion = ExpectedException.none();
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
//        sut.forEach(System.out::println);
    }

    @Test
    public void cantTakeCardsAfterReshufflePulled() {
        Optional<AllianceNavCard> card;
        do {
            card = sut.take();
        } while (!AllianceNavCard.RESHUFFLE.equals(card.get()));
        card = sut.take();
        assertTrue(!card.isPresent());
    }

    @Test
    public void cantTakeCardsAftePulledAll() {
        IntStream.range(1, sut.spec.count)
                .forEach(i -> sut.take());
        final Optional<AllianceNavCard> card = sut.take();
        assertTrue(!card.isPresent());
    }

}
