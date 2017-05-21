package com.whatgameapps.firefly;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multiset;
import com.google.common.collect.SortedMultiset;
import com.google.common.collect.TreeMultiset;
import org.apache.http.annotation.Immutable;

import java.util.Set;

@Immutable
public class CardDeckSpecification {
    public final int count;
    private final SortedMultiset<String> foo = TreeMultiset.create();

    public CardDeckSpecification(final CardDeckSpecification basis, final int cumulativeCardCount, final ImmutableMultimap<String, Integer> addedCards) {
        this(cumulativeCardCount, addedCards);
        this.addCards(basis.entrySet());
    }

    public CardDeckSpecification(int count, ImmutableMultimap<String, Integer> deckSpec) {
        this.count = count;
        addCards(deckSpec);
    }

    private void addCards(Set<Multiset.Entry<String>> deckSpec) {
        deckSpec.forEach(e -> this.foo.add(e.getElement(), e.getCount()));
    }

    public Set<Multiset.Entry<String>> entrySet() {
        return this.foo.entrySet();
    }

    private void addCards(ImmutableMultimap<String, Integer> deckSpec) {
        deckSpec.entries().stream().forEach(e -> this.foo.add(e.getKey(), e.getValue()));
    }

    public String toString() {
        return "Deck with " + this.count + " cards";
    }
}
