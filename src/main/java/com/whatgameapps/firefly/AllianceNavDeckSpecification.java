package com.whatgameapps.firefly;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.whatgameapps.firefly.rest.AllianceNavCard;
import org.apache.http.annotation.Immutable;

import java.util.Map;

@Immutable
public class AllianceNavDeckSpecification {
    public static final AllianceNavDeckSpecification RESHUFFLE = new AllianceNavDeckSpecification(1, ImmutableMultimap.<AllianceNavCard, Integer>builder()
            .put(AllianceNavCard.RESHUFFLE, 1)
            .build());

    public static final AllianceNavDeckSpecification BASIC = new AllianceNavDeckSpecification(RESHUFFLE, 40, ImmutableMultimap.<AllianceNavCard, Integer>builder()
            .put(AllianceNavCard.KEEP_FLYING, 25)
            .put(AllianceNavCard.ALLIANCE_CRUISER, 3)
            .put(AllianceNavCard.CUSTOMS_INSPECTION, 2)
            .put(AllianceNavCard.MINOR_TECHNICAL, 1)
            .put(AllianceNavCard.IFN_THE_COIL, 1)
            .put(AllianceNavCard.WHATS_GOING_ON, 1)
            .put(AllianceNavCard.SALVAGE_OPS, 1)
            .put(AllianceNavCard.REGULATED_SALVAGE, 1)
            .put(AllianceNavCard.SALVAGE_OPS_ABANDONED, 1)
            .put(AllianceNavCard.FAMILY_DINNER, 1)
            .put(AllianceNavCard.DISTRESS_SIGNAL, 1)
            .put(AllianceNavCard.FREIGHTER_CONVOY, 1)
            .build()
    );

    public static final AllianceNavDeckSpecification KALIDASA = new AllianceNavDeckSpecification(BASIC, 60, ImmutableMultimap.<AllianceNavCard, Integer>builder()
            .put(AllianceNavCard.KEEP_FLYING, 2)
            .put(AllianceNavCard.ALLIANCE_HARASSMENT, 2)
            .put(AllianceNavCard.ENHANCED_ENFORCEMENT, 1)
            .put(AllianceNavCard.OUTBOUND_COLONISTS, 1)
            .put(AllianceNavCard.ALLIANCE_ENTANGLEMENTS, 1)
            .put(AllianceNavCard.ALLIANCE_CRUISER, 2)
            .put(AllianceNavCard.HE_LL_COME_AT_YOU, 1)
            .put(AllianceNavCard.LEAVE_NO_GROUND, 1)
            .put(AllianceNavCard.PERSISTENT_PURSUIT, 4)
            .put(AllianceNavCard.ALLIANCE_INTERROGATION, 1)
            .put(AllianceNavCard.BADGERS_BOYS, 1)
            .put(AllianceNavCard.BROKEN_DOWN_SHUTTLE, 1)
            .put(AllianceNavCard.PASSENGER_UNREST, 1)
            .put(AllianceNavCard.FIRE_IN_THE_ENGINE_ROOM, 1)
            .build()
    );

    public final int count;
    public final ImmutableMultimap<AllianceNavCard, Integer> deckSpec;

    public AllianceNavDeckSpecification(int count, ImmutableMultimap<AllianceNavCard, Integer> deckSpec) {
        this.count = count;
        this.deckSpec = deckSpec;
    }

    public AllianceNavDeckSpecification(final AllianceNavDeckSpecification basis, final int cumulativeCardCount, final ImmutableMultimap<AllianceNavCard, Integer> addedCards) {
        Multimap<AllianceNavCard, Integer> cards = ArrayListMultimap.create(basis.deckSpec);
        cards.putAll(addedCards);
        this.deckSpec = ImmutableMultimap.copyOf(cards);
        this.count = cumulativeCardCount;
    }

    public ImmutableCollection<Map.Entry<AllianceNavCard, Integer>> entrySet() {
        return this.deckSpec.entries();
    }

    public String toString() {
        return "Deck with " + this.count + " cards";
    }
}
