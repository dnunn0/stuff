package com.whatgameapps.firefly;

import com.google.gson.Gson;
import com.whatgameapps.firefly.rest.NavCard;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Stack;
import java.util.stream.Collectors;

public class NavDeck {
    private static final Gson gson = new Gson();
    public transient final NavDeckSpecification spec;
    //   private final CardStacks stacks = new CardStacks();
    private transient String gsonStore;

    public NavDeck(NavDeckSpecification deckSpec) {
        this.spec = deckSpec;
        createDeck(deckSpec);
    }

    private void createDeck(final NavDeckSpecification deckSpec) {
        final CardStacks stacks = new CardStacks();
        List<NavCard> cardList = deckSpec.entrySet().stream()
                .map(cardsSpec -> createCards(cardsSpec.getElement(), cardsSpec.getCount()))
                .flatMap(Collection::stream)
                .collect(Collectors.toList());

        stacks.cards.addAll(cardList);
        stacks.cards.addAll(createCards(NavCard.UNKNOWN, deckSpec.count - stacks.cards.size()));

        stacks.shuffle();
        storeStacks(stacks);
    }

    private List<NavCard> createCards(String cardTitle, final Integer count) {
        if (count <= 0) return Collections.emptyList();

        final NavCard prototype = new NavCard(cardTitle);
        return Collections.nCopies(count, prototype);
    }

    private void storeStacks(CardStacks stacks) {
        this.gsonStore = gson.toJson(stacks);
    }

    public void shuffle() {
        final CardStacks stacks = CardStacks.From(this.gsonStore);
        stacks.shuffle();
        storeStacks(stacks);
    }

    /**
     * count of cards remaining in deck
     *
     * @return
     */
    public int size() {
        final CardStacks stacks = CardStacks.From(this.gsonStore);
        return stacks.cards.size();
    }

    public int countCards(String target) {
        final CardStacks stacks = CardStacks.From(this.gsonStore);
        return stacks.countCards(target);
    }

    public Optional<NavCard> take() {
        final CardStacks stacks = CardStacks.From(this.gsonStore);
        final Optional<NavCard> result = stacks.take();
        storeStacks(stacks);
        return result;
    }

    public Stack<NavCard> discards() {
        final CardStacks stacks = CardStacks.From(this.gsonStore);
        return stacks.discards;
    }

    static class CardStacks {
        private final Stack<NavCard> cards = new Stack<>();
        private final Stack<NavCard> discards = new Stack<>();

        static CardStacks From(String gsonStore) {
            return gson.fromJson(gsonStore, NavDeck.CardStacks.class);
        }

        Optional<NavCard> take() {
            if (this.cards.empty()) return Optional.empty();

            NavCard card = this.cards.pop();
            this.discards.push(card);
            if (card.isReshuffle())
                this.discardAll();

            return Optional.of(card);
        }

        private void discardAll() {
            this.moveCardsToOtherPile(this.cards, this.discards);
        }

        private void moveCardsToOtherPile(final Stack<NavCard> source, final Stack<NavCard> destination) {
            destination.addAll(source);
            source.clear();
        }

        void shuffle() {
            this.mergeIntoCards();
            Collections.shuffle(this.cards);
        }

        private void mergeIntoCards() {
            this.moveCardsToOtherPile(this.discards, this.cards);
        }

        int countCards(String target) {
            return (int) this.cards.stream().map(c -> c.action).filter(s -> s.equals(target)).count();
        }
    }

}
