package com.whatgameapps.firefly;

import com.whatgameapps.firefly.rest.NavCard;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Stack;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class NavDeck {
    public transient final NavDeckSpecification spec;
    private transient String externalized;

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
        this.externalized = stacks.externalize();
    }

    public void shuffle() {
        final CardStacks stacks = CardStacks.From(this.externalized);
        stacks.shuffle();
        storeStacks(stacks);
    }

    /**
     * count of cards remaining in deck
     *
     * @return
     */
    public int size() {
        final CardStacks stacks = CardStacks.From(this.externalized);
        return stacks.cards.size();
    }

    public int countCards(String target) {
        final CardStacks stacks = CardStacks.From(this.externalized);
        return stacks.countCards(target);
    }

    public Optional<NavCard> take() {
        final CardStacks stacks = CardStacks.From(this.externalized);
        final Optional<NavCard> result = stacks.take();
        storeStacks(stacks);
        return result;
    }

    public Stack<NavCard> discards() {
        final CardStacks stacks = CardStacks.From(this.externalized);
        return stacks.discards;
    }

    static class CardStacks {
        private static final char DELIMITER_CHAR = '\n';
        private final Stack<NavCard> cards = new Stack<>();
        private final Stack<NavCard> discards = new Stack<>();

        static CardStacks From(String storage) {
            CardStacks result = new CardStacks();
            return result.readFrom(storage);

            // return gson.fromJson(externalized, NavDeck.CardStacks.class);
        }

        private CardStacks readFrom(String storage) {
            BufferedReader buff = new BufferedReader(new StringReader(storage));
            this.readFrom(this.cards, buff);
            this.readFrom(this.discards, buff);
            return this;
        }

        private CardStacks readFrom(Stack<NavCard> newCards, BufferedReader buff) {
            int count = Integer.parseInt(readLineConvertingException(buff));
            IntStream.range(0, count).forEach(i -> newCards.push(NavCard.From(readLineConvertingException(buff))));
            return this;
        }

        private String readLineConvertingException(BufferedReader buff) {
            try {
                return buff.readLine();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        public String externalize() {
            final StringBuilder buff = new StringBuilder(cards.size() * 30 + (this.discards.size() * 30));
            this.storeStack(buff, this.cards);
            this.storeStack(buff, this.discards);
            return buff.toString();
        }

        private void storeStack(StringBuilder buff, Stack<NavCard> cards) {
            buff.append(cards.size());
            buff.append(DELIMITER_CHAR);
            cards.stream().forEach(c -> {
                buff.append(c.externalized());
                buff.append(DELIMITER_CHAR);
            });
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
