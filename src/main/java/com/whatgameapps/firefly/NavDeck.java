package com.whatgameapps.firefly;

import com.whatgameapps.firefly.rest.NavCard;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Stack;
import java.util.stream.Collectors;

public class NavDeck {

    public final NavDeckSpecification spec;
    private final Stack<NavCard> cards = new Stack<>();
    private final Stack<NavCard> discards = new Stack<>();

    public NavDeck(NavDeckSpecification deckSpec) {
        this.spec = deckSpec;
        createDeck(deckSpec);
    }

    private void createDeck(final NavDeckSpecification deckSpec) {

        List<NavCard> cardList = deckSpec.entrySet().stream()
                .map(cardsSpec -> createCards(cardsSpec.getKey(), cardsSpec.getValue()))
                .flatMap(Collection::stream)
                .collect(Collectors.toList());

        this.cards.addAll(cardList);
        this.cards.addAll(createCards(NavCard.UNKNOWN, deckSpec.count - cards.size()));

        shuffle();
    }

    private List<NavCard> createCards(String cardTitle, final Integer count) {
        if (count <= 0) return Collections.emptyList();

        final NavCard prototype = new NavCard(cardTitle);
        return Collections.nCopies(count, prototype);
    }

    public void shuffle() {
        moveCardsToOtherPile(discards, cards);
        Collections.shuffle(cards);
    }

    private void moveCardsToOtherPile(final Stack<NavCard> source, final Stack<NavCard> destination) {
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

    public int countCards(String target) {
        return (int) cards.stream().map(c -> c.action).filter(s -> s.equals(target)).count();
    }

    public Optional<NavCard> take() {
        if (cards.empty()) return Optional.empty();

        NavCard card = cards.pop();
        discards.push(card);
        if (card.isReshuffle())
            moveCardsToOtherPile(cards, discards);

        return Optional.of(card);
    }

    public Stack<NavCard> discards() {
        return this.discards;
    }
}
