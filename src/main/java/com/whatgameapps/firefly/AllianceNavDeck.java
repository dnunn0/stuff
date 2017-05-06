package com.whatgameapps.firefly;

import com.whatgameapps.firefly.rest.AllianceNavCard;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;
import java.util.function.Consumer;

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
        List<AllianceNavCard> tempCards = new ArrayList<>();

        deckSpec.entrySet().stream().forEach(
                cardsSpec -> {
                    List<AllianceNavCard> copies = createCards(cardsSpec.getKey(), cardsSpec.getValue());
                    tempCards.addAll(copies);
                }
        );
        tempCards.addAll(createCards(AllianceNavCard.UNKNOWN, deckSpec.count - tempCards.size()));

        tempCards.forEach(cards::push);
        shuffle();

    }

    private List<AllianceNavCard> createCards(final AllianceNavCard prototype, final Integer count) {
        return Collections.nCopies(count, prototype);
    }

    public void shuffle() {
        List<AllianceNavCard> tempCards = new ArrayList<>();
        tempCards.addAll(cards);
        tempCards.addAll(discards);
        Collections.shuffle(tempCards);
        cards.clear();
        discards.clear();
        tempCards.forEach(cards::push);
    }

    public int size() {
        return cards.size();
    }

    public int countCards(final AllianceNavCard target) {
        return (int) cards.stream().filter(c -> c.equals(target)).count();
    }

    public AllianceNavCard take() {
        AllianceNavCard card = cards.pop();
        discards.push(card);
        return card;
    }

    public void forEach(final Consumer<? super AllianceNavCard> consumer) {
        this.cards.forEach(consumer);
    }

    public Stack<AllianceNavCard> discards() {
        return this.discards;
    }
}
