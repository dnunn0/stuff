package com.whatgameapps.firefly;

import com.google.common.collect.ImmutableMultimap;
import org.apache.http.annotation.Immutable;

@Immutable
public class BountyDeckSpecification extends CardDeckSpecification {
    public static final BountyDeckSpecification RESHUFFLE = new BountyDeckSpecification(1, ImmutableMultimap.<String, Integer>builder()
            .put("Reaver Cutter - Reshuffle", 1)
            .build());

    public static final BountyDeckSpecification KALIDASA = new BountyDeckSpecification(RESHUFFLE, 58, ImmutableMultimap.<String, Integer>builder()
            .put("The Big Black", 21)
            .put("A Rogue Trader", 1)
            .put("An Adrift Transport", 1)
            .put("Dangerous Salvage", 1)
            .put("Derelict Ship", 2)
            .put("Enhanced Enforcement", 2)
            .put("\"Family\" Dinner", 1)
            .put("Ghost Ship", 1)
            .put("He'll Come At You Sideways", 1)
            .put("Hollowed Out Space-Liner", 1)
            .put("Leave No Ground To Go To", 1)
            .put("Nav Hazard: Asteroid", 1)
            .put("Nav Hazard: Debris Field", 1)
            .put("Patience's Posse", 1)
            .put("Persistent Pursuit", 4)
            .put("Punctured Fuel Lines", 1)
            .put("Reaver Bait!", 1)
            .put("Reavers, Dead Ahead", 3)
            .put("Reavers On The Hunt", 7)
            .put("Ruttin' Drive Core's Blown", 1)
            .put("Scrapper Ambush", 1)
            .put("She's Tore Up Plenty", 1)
            .put("Ship Graveyard", 1)
            .put("Space Pox!", 1)
            .put("Storm's Getting' Worse", 1)
            .put("What's That Noise?", 1)
            .build()
    );

    public BountyDeckSpecification(int count, ImmutableMultimap<String, Integer> deckSpec) {
        super(count, deckSpec);
    }

    public BountyDeckSpecification(final BountyDeckSpecification basis, final int cumulativeCardCount, final ImmutableMultimap<String, Integer> addedCards) {
        super(basis, cumulativeCardCount, addedCards);
    }

}
