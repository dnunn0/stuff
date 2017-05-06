package com.whatgameapps.firefly;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.whatgameapps.firefly.rest.AllianceNavCard;
import org.apache.http.annotation.Immutable;

import java.util.Map;

@Immutable
public class AllianceNavDeckSpecification {
    public static final AllianceNavDeckSpecification BASIC = new AllianceNavDeckSpecification(40, ImmutableMap.<AllianceNavCard, Integer>builder()
            .put(AllianceNavCard.KEEP_FLYING, 25)
            .put(AllianceNavCard.CRUISER_CONTACT, 4)
            .put(AllianceNavCard.RESHUFFLE, 1)
            .build()
    );

    public static final AllianceNavDeckSpecification KALIDASA = new AllianceNavDeckSpecification(60, ImmutableMap.<AllianceNavCard, Integer>builder()
            .put(AllianceNavCard.KEEP_FLYING, 25)
            .put(AllianceNavCard.CRUISER_CONTACT, 4)
            .put(AllianceNavCard.CORVETTE_CONTACT, 4)
            .put(AllianceNavCard.RESHUFFLE, 1)
            .build()
    );

    public final int count;
    public final ImmutableMap<AllianceNavCard, Integer> deckSpec;

    AllianceNavDeckSpecification(int count, ImmutableMap<AllianceNavCard, Integer> deckSpec) {
        this.count = count;
        this.deckSpec = deckSpec;
    }

    public ImmutableSet<Map.Entry<AllianceNavCard, Integer>> entrySet() {
        return this.deckSpec.entrySet();
    }
}
