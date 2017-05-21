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

public class CardDeck {
    public final transient CardDeckSpecification spec;
    private Archive storedDeck;

    private CardDeck(CardDeckSpecification deckSpec, Archive storage) {
        this.storedDeck = storage;
        this.spec = deckSpec;
    }

    public static CardDeck NewFrom(CardDeckSpecification deckSpec, Archive storage) {
        createAndStore(deckSpec, storage);
        return OldFrom(deckSpec, storage);
    }

    private static void createAndStore(CardDeckSpecification deckSpec, Archive storage) {
        CardStacks stacks = CardStacks.Create(deckSpec);
        storage.write(stacks.externalize());
    }

    public static CardDeck OldFrom(CardDeckSpecification deckSpec, Archive storage) {
        return new CardDeck(deckSpec, storage);
    }

    /**
     * count of cards remaining in deck
     *
     * @return
     */
    public int size() {
        final CardStacks stacks = retrieveStacks();
        return stacks.cards.size();
    }

    private CardStacks retrieveStacks() {
        return CardStacks.From(this.storedDeck.read());
    }

    public int countCards(String target) {
        final CardStacks stacks = retrieveStacks();
        return stacks.countCards(target);
    }

    public Optional<NavCard> take() {
        final CardStacks stacks = retrieveStacks();
        final Optional<NavCard> result = stacks.take();
        storeStacks(stacks);
        return result;
    }

    private void storeStacks(CardStacks stacks) {
        this.storedDeck.write(stacks.externalize());
    }

    public void shuffle() {
        final CardStacks stacks = retrieveStacks();
        stacks.shuffle();
        storeStacks(stacks);
    }


    public Stack<NavCard> discards() {
        final CardStacks stacks = retrieveStacks();
        return stacks.discards;
    }

    static class CardStacks {
        private static final char DELIMITER_CHAR = '\n';
        private final Stack<NavCard> cards = new Stack<>();
        private final Stack<NavCard> discards = new Stack<>();

        static CardStacks Create(CardDeckSpecification spec) {
            final CardStacks stacks = new CardStacks();
            stacks.createDeck(spec);
            return stacks;
        }

        private void createDeck(CardDeckSpecification spec) {
            List<NavCard> cardList = spec.entrySet().stream()
                    .map(cardsSpec -> createCards(cardsSpec.getElement(), cardsSpec.getCount()))
                    .flatMap(Collection::stream)
                    .collect(Collectors.toList());

            this.cards.addAll(cardList);
            this.cards.addAll(createCards(NavCard.UNKNOWN, spec.count - this.cards.size()));

            this.shuffle();
        }

        private List<NavCard> createCards(String cardTitle, final Integer count) {
            if (count <= 0) return Collections.emptyList();

            final NavCard prototype = new NavCard(cardTitle);
            return Collections.nCopies(count, prototype);
        }

        void shuffle() {
            this.mergeIntoCards();
            Collections.shuffle(this.cards);
        }

        private void mergeIntoCards() {
            this.moveCardsToOtherPile(this.discards, this.cards);
        }

        private void moveCardsToOtherPile(final Stack<NavCard> source, final Stack<NavCard> destination) {
            destination.addAll(source);
            source.clear();
        }

        static CardStacks From(String storage) {
            CardStacks result = new CardStacks();
            return result.readFrom(storage);
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

        int countCards(String target) {
            return (int) this.cards.stream().map(c -> c.action).filter(s -> s.equals(target)).count();
        }

    }

}
