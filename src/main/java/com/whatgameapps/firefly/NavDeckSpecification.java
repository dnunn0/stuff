package com.whatgameapps.firefly;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;

import java.util.Map;

public class NavDeckSpecification {
    public final int count;
    public final ImmutableMultimap<String, Integer> deckSpec;

    public NavDeckSpecification(int count, ImmutableMultimap<String, Integer> deckSpec) {
        this.count = count;
        this.deckSpec = deckSpec;
    }

    public NavDeckSpecification(final NavDeckSpecification basis, final int cumulativeCardCount, final ImmutableMultimap<String, Integer> addedCards) {
        Multimap<String, Integer> cards = ArrayListMultimap.create(basis.deckSpec);
        cards.putAll(addedCards);
        this.deckSpec = ImmutableMultimap.copyOf(cards);
        this.count = cumulativeCardCount;
    }

    public ImmutableCollection<Map.Entry<String, Integer>> entrySet() {
        return this.deckSpec.entries();
    }

    public String toString() {
        return "Deck with " + this.count + " cards";
    }
}
