package com.whatgameapps.firefly;

import com.google.common.collect.ImmutableMap;
import com.whatgameapps.firefly.rest.AllianceNavCard;

public class AllianceNavDeckSpecification {
    public static final ImmutableMap<AllianceNavCard,Integer> BASIC = ImmutableMap.<AllianceNavCard, Integer>builder()
            .put(AllianceNavCard.KEEP_FLYING, 25)
            .put(AllianceNavCard.CRUISER_CONTACT, 4)
            .put(AllianceNavCard.UNKNOWN, 11)

            .build();

    public static final ImmutableMap<AllianceNavCard,Integer> KALIDASA = ImmutableMap.<AllianceNavCard, Integer>builder()
            .put(AllianceNavCard.KEEP_FLYING, 25)
            .put(AllianceNavCard.CRUISER_CONTACT, 4)
            .put(AllianceNavCard.CORVETTE_CONTACT, 4)
            .put(AllianceNavCard.UNKNOWN, 27)
            .build();

}
