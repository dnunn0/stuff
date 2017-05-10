package com.whatgameapps.firefly;

import com.google.common.collect.ImmutableMultimap;
import org.apache.http.annotation.Immutable;

@Immutable
public class AllianceNavDeckSpecification extends NavDeckSpecification {
    public static final AllianceNavDeckSpecification RESHUFFLE = new AllianceNavDeckSpecification(1, ImmutableMultimap.<String, Integer>builder()
            .put("Alliance Cruiser - Reshuffle", 1)
            .build());

    public static final AllianceNavDeckSpecification BASIC = new AllianceNavDeckSpecification(RESHUFFLE, 40, ImmutableMultimap.<String, Integer>builder()
            .put("The Big Black", 25)
            .put("Customs Inspection", 2)
            .put("Cruiser Patrol", 4)
            .put("Alliance Entanglements", 1)
            .put("Abandoned Ship", 1)
            .put("Distress Signal", 1)
            .put("\"Family\" Dinner", 1)
            .put("Freighter Convoy", 1)
            .put("If'n The Coil Busts We're Driftin'", 1)
            .put("Minor Technical Difficulty", 1)
            .put("What's Going On In The Engine Room?", 1)
            .build()
    );

    public static final AllianceNavDeckSpecification KALIDASA = new AllianceNavDeckSpecification(BASIC, 60, ImmutableMultimap.<String, Integer>builder()
            .put("The Big Black", 2)
            .put("Alliance Checkpoint", 1)
            .put("Alliance Harassment", 2)
            .put("Alliance Interrogation", 1)
            .put("Badger's Boys", 1)
            .put("Broken Down Shuttle", 1)
            .put("Cruiser Patrol", 1)//guessing this is here?
            .put("Enhanced Enforcement", 1)
            .put("Fire In The Engine Room", 1)
            .put("He'll Come at You Sideways", 1)
            .put("Leave No Ground To Go To", 1)
            .put("Outbound Colonists", 1)
            .put("Persistent Pursuit", 4)
            .put("Passenger Unrest", 1)
            .put("Regulated Salvage", 1)
            .build()
    );

    public AllianceNavDeckSpecification(int count, ImmutableMultimap<String, Integer> deckSpec) {
        super(count, deckSpec);
    }

    public AllianceNavDeckSpecification(final AllianceNavDeckSpecification basis, final int cumulativeCardCount, final ImmutableMultimap<String, Integer> addedCards) {
        super(basis, cumulativeCardCount, addedCards);
    }

}
