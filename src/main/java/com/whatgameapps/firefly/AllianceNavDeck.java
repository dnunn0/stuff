package com.whatgameapps.firefly;

import com.whatgameapps.firefly.rest.AllianceNavCard;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Stack;

public class AllianceNavDeck {

    public final AllianceNavDeckSpecification spec;
    private final Stack<AllianceNavCard> cards = new Stack<>();
    private final Stack<AllianceNavCard> discards = new Stack<>();

    public AllianceNavDeck(AllianceNavDeckSpecification deckSpec) {
//        for(Map.Entry<AllianceNavCard, Integer> cardsSpec: deckSpec.entrySet())
//            for(int i = 0; i < cardsSpec.getValue(); ++i)
//                cards.add(cardsSpec.getKey().clone());

        this.spec = deckSpec;
        createDeck(deckSpec);
    }

    private void createDeck(final AllianceNavDeckSpecification deckSpec) {

        deckSpec.entrySet().stream().forEach(
                cardsSpec -> {
                    List<AllianceNavCard> copies = createCards(cardsSpec.getKey(), cardsSpec.getValue());
                    cards.addAll(copies);
                }
        );
        cards.addAll(createCards(AllianceNavCard.UNKNOWN, deckSpec.count - cards.size()));

        shuffle();
    }

    private List<AllianceNavCard> createCards(final AllianceNavCard prototype, final Integer count) {
        if (count <= 0) return Collections.emptyList();
        return Collections.nCopies(count, prototype);
    }

    public void shuffle() {
        cards.addAll(discards);
        discards.clear();
        Collections.shuffle(cards);
    }

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
        if (AllianceNavCard.RESHUFFLE.equals(card)) {
            discards.addAll(cards);
            cards.clear();
        }
        return Optional.of(card);
    }

    public Stack<AllianceNavCard> discards() {
        return this.discards;
    }
}
