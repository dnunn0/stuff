package com.whatgameapps.firefly;

import com.google.common.collect.ImmutableMultimap;
import org.apache.http.annotation.Immutable;

@Immutable
public class RimNavDeckSpecification extends NavDeckSpecification {
    public static final RimNavDeckSpecification RESHUFFLE = new RimNavDeckSpecification(1, ImmutableMultimap.<String, Integer>builder()
            .put("Reaver Cutter - Reshuffle", 1)
            .build());

    public static final RimNavDeckSpecification KALIDASA = new RimNavDeckSpecification(RESHUFFLE, 60, ImmutableMultimap.<String, Integer>builder()
            .put("The Big Black", 20)
            .put("Abandoned Tanker", 1)
            .put("Alliance Spy Satellite", 1)
            .put("Blown Out Buffer Panel", 1)
            .put("Damaged Spy Satellite", 1)
            .put("Enhanced Enforcement", 1)
            .put("Failure To Communicate", 1)
            .put("First Come, First Serve", 1)
            .put("First Rule of Flying", 1)
            .put("Fly By Night Casino Ship", 1)
            .put("Fuel Coupling Failure", 1)
            .put("Grav Well Maneuver", 1)
            .put("He'll Come At You Sideways", 1)
            .put("Horowitz's Trading Scow", 1)
            .put("Leave No Ground To Go TO", 1)
            .put("Local Tariff Patrol", 1)
            .put("Locking Horns Over Scraps", 1)
            .put("Nav System On The Fritz", 1)
            .put("Niska's Ne'er Do-Wells", 1)
            .put("Objects In Space", 1)
            .put("Orphaned Cargo Pod", 1)
            .put("Persistent Pursuit", 4)
            .put("Ravaged Transport", 1)
            .put("Reaver Booby Trap", 1)
            .put("Reavers Dead Ahead", 1)
            .put("Reavers In Orbit", 3)
            .put("Reavers On The Hunt", 9)
            .put("She's Tore Up Plenty", 1)
            .put("Storm's Getting' Worse", 1)
            .build()
    );

    public RimNavDeckSpecification(int count, ImmutableMultimap<String, Integer> deckSpec) {
        super(count, deckSpec);
    }

    public RimNavDeckSpecification(final RimNavDeckSpecification basis, final int cumulativeCardCount, final ImmutableMultimap<String, Integer> addedCards) {
        super(basis, cumulativeCardCount, addedCards);
    }

}
