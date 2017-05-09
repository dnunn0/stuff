package com.whatgameapps.firefly.rest;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.apache.http.annotation.Immutable;

@Immutable
public class  AllianceNavCard {
    public static final AllianceNavCard RESHUFFLE = new AllianceNavCard("Alliance Cruiser - Reshuffle", true);
    public static final AllianceNavCard UNKNOWN = new AllianceNavCard("Alliance Huh");
    public static final AllianceNavCard KEEP_FLYING = new AllianceNavCard("The Big Black", true);
    public static final AllianceNavCard ALLIANCE_CRUISER = new AllianceNavCard("Alliance Contact - Cruiser", true);
    public static final AllianceNavCard CUSTOMS_INSPECTION = new AllianceNavCard("Customs Inspection", true);
    public static final AllianceNavCard CORVETTE_CONTACT = new AllianceNavCard("Alliance Corvette Contact");
    public static final AllianceNavCard BREAKDOWN = new AllianceNavCard("Alliance Breakdown");
    public static final AllianceNavCard IFN_THE_COIL = new AllianceNavCard("If'n The Coil Busts We're Driftin'", true);
    public static final AllianceNavCard WHATS_GOING_ON = new AllianceNavCard("What's Going On In The Engine Room?", true);
    public static final AllianceNavCard SALVAGE_OPS = new AllianceNavCard("Alliance Salvage Ops");
    public static final AllianceNavCard SALVAGE_OPS_ABANDONED = new AllianceNavCard("Abandoned Ship", true);
    public static final AllianceNavCard FAMILY_DINNER = new AllianceNavCard("Alliance Family Dinner");
    public static final AllianceNavCard DISTRESS_SIGNAL = new AllianceNavCard("Alliance Distress Signal");
    public static final AllianceNavCard FREIGHTER_CONVOY = new AllianceNavCard("Alliance Freighter Convey");

    public final String action;
    private final boolean hasPic;

    AllianceNavCard(final String action) {
        this(action, false);
    }

    AllianceNavCard(final String action, boolean hasPic) {
        this.action = action;
        this.hasPic = hasPic;
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public boolean equals(Object o) {
        return EqualsBuilder.reflectionEquals(this, o);
    }

    @Override
    public AllianceNavCard clone() {
        return new AllianceNavCard(this.action, this.hasPic);
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
