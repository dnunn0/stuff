package com.whatgameapps.firefly;

import com.google.common.collect.ImmutableMultimap;
import org.apache.http.annotation.Immutable;

@Immutable
public class BountyDeckSpecification extends CardDeckSpecification {

    public static final BountyDeckSpecification BOUNTIES = new BountyDeckSpecification(20, ImmutableMultimap.<String, Integer>builder()
            .put("Bandits", 1)
            .put("Billy", 1)
            .put("Bree", 1)
            .put("Crow", 1)
            .put("Da;om", 1)
            .put("Enforcers", 1)
            .put("Grange Brothers", 1)
            .put("Helen", 1)
            .put("Interrogator", 1)
            .put("Jayne", 1)
            .put("Jesse", 1)
            .put("River Tam", 1)
            .put("Scrappers", 1)
            .put("Simon Tam", 1)
            .put("Stitch", 1)
            .put("The Fixer", 1)
            .put("The Specialist", 1)
            .put("Tracey", 1)
            .put("Two-Fry", 1)
            .put("Zoe", 1)
            .build()
    );

    public BountyDeckSpecification(int count, ImmutableMultimap<String, Integer> deckSpec) {
        super(count, deckSpec);
    }

    public BountyDeckSpecification(final BountyDeckSpecification basis, final int cumulativeCardCount, final ImmutableMultimap<String, Integer> addedCards) {
        super(basis, cumulativeCardCount, addedCards);
    }

}
