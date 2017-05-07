package com.whatgameapps.firefly;

import com.whatgameapps.firefly.rest.AllianceNavCard;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Stack;
import java.util.stream.Collectors;

public class AllianceNavDeck {

    public final AllianceNavDeckSpecification spec;
    private final Stack<AllianceNavCard> cards = new Stack<>();
    private final Stack<AllianceNavCard> discards = new Stack<>();

    public AllianceNavDeck(AllianceNavDeckSpecification deckSpec) {
        this.spec = deckSpec;
        createDeck(deckSpec);
    }

    private void createDeck(final AllianceNavDeckSpecification deckSpec) {

        List<AllianceNavCard> cardList = deckSpec.entrySet().stream()
                .map(cardsSpec -> createCards(cardsSpec.getKey(), cardsSpec.getValue()))
                .flatMap(Collection::stream)
                .collect(Collectors.toList());

        this.cards.addAll(cardList);
        this.cards.addAll(createCards(AllianceNavCard.UNKNOWN, deckSpec.count - cards.size()));

        shuffle();
    }

    private List<AllianceNavCard> createCards(final AllianceNavCard prototype, final Integer count) {
        if (count <= 0) return Collections.emptyList();
        return Collections.nCopies(count, prototype);
    }

    public void shuffle() {
        System.out.println("============ Shuffle =============");
        moveCardsToOtherPile(discards, cards);
        Collections.shuffle(cards);
    }

    private void moveCardsToOtherPile(final Stack<AllianceNavCard> source, final Stack<AllianceNavCard> destination) {
        destination.addAll(source);
        source.clear();
    }

    /**
     * count of cards remaining in deck
     *
     * @return
     */
    public int size() {
        return cards.size();
    }

    public int countCards(final AllianceNavCard target) {
        return (int) cards.stream().filter(c -> c.equals(target)).count();
    }

    public Optional<AllianceNavCard> take() {
        if (cards.empty()) return Optional.empty();

        AllianceNavCard card = cards.pop();
        discards.push(card);
        if (AllianceNavCard.RESHUFFLE.equals(card))
            moveCardsToOtherPile(cards, discards);

        return Optional.of(card);
    }

    public Stack<AllianceNavCard> discards() {
        return this.discards;
    }
}
