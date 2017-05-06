package com.whatgameapps.firefly;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.whatgameapps.firefly.rest.AllianceNavCard;
import org.apache.http.annotation.Immutable;

import java.util.HashMap;
import java.util.Map;

@Immutable
public class AllianceNavDeckSpecification {
    public static final AllianceNavDeckSpecification BASIC = new AllianceNavDeckSpecification(40, ImmutableMap.<AllianceNavCard, Integer>builder()
            .put(AllianceNavCard.RESHUFFLE, 1)
            .put(AllianceNavCard.KEEP_FLYING, 25)
            .put(AllianceNavCard.ALLIANCE_CRUISER, 3)
            .put(AllianceNavCard.CUSTOMS_INSPECTION, 2)
            .put(AllianceNavCard.BREAKDOWN, 3)
            .put(AllianceNavCard.SALVAGE_OPS, 2)
            .put(AllianceNavCard.SALVAGE_OPS_ABANDONED, 1)
            .put(AllianceNavCard.FAMILY_DINNER, 1)
            .put(AllianceNavCard.DISTRESS_SIGNAL, 1)
            .put(AllianceNavCard.FREIGHTER_CONVOY, 1)
            .build()
    );

    public static final AllianceNavDeckSpecification KALIDASA = new AllianceNavDeckSpecification(BASIC, 60, ImmutableMap.<AllianceNavCard, Integer>builder()
            .put(AllianceNavCard.CORVETTE_CONTACT, 2)
            .build()
    );

    public final int count;
    public final ImmutableMap<AllianceNavCard, Integer> deckSpec;

    AllianceNavDeckSpecification(int count, ImmutableMap<AllianceNavCard, Integer> deckSpec) {
        this.count = count;
        this.deckSpec = deckSpec;
    }

    public AllianceNavDeckSpecification(final AllianceNavDeckSpecification basis, final int cumulativeCardCount, final ImmutableMap<AllianceNavCard, Integer> addedCards) {
        Map<AllianceNavCard, Integer> cards = new HashMap<>(basis.deckSpec);
        cards.putAll(addedCards);
        this.deckSpec = ImmutableMap.copyOf(cards);
        this.count = cumulativeCardCount;
    }

    public ImmutableSet<Map.Entry<AllianceNavCard, Integer>> entrySet() {
        return this.deckSpec.entrySet();
    }

    public String toString() {
        return "Deck with " + this.count + " cards";
    }
}
