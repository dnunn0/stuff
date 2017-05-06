package com.whatgameapps.firefly.rest;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.http.annotation.Immutable;

@Immutable
public class  AllianceNavCard {
    public static final AllianceNavCard RESHUFFLE = new AllianceNavCard("Alliance Cruiser Contact - Reshuffle", "");
    public static final AllianceNavCard UNKNOWN = new AllianceNavCard("Alliance Huh", "");
    public static final AllianceNavCard KEEP_FLYING = new AllianceNavCard("Alliance: Tbe Big Black", "");
    public static final AllianceNavCard ALLIANCE_CRUISER = new AllianceNavCard("Alliance Cruiser", "");
    public static final AllianceNavCard CUSTOMS_INSPECTION = new AllianceNavCard("Alliance Customs Inspection", "");
    public static final AllianceNavCard CORVETTE_CONTACT = new AllianceNavCard("Alliance Corvette Contact", "");
    public static final AllianceNavCard BREAKDOWN = new AllianceNavCard("Alliance Breakdown", "");
    public static final AllianceNavCard SALVAGE_OPS = new AllianceNavCard("Alliance Salvage Ops", "");
    public static final AllianceNavCard SALVAGE_OPS_ABANDONED = new AllianceNavCard("Alliance Salvage Ops-Abandoned", "");
    public static final AllianceNavCard FAMILY_DINNER = new AllianceNavCard("Alliance Family Dinner", "");
    public static final AllianceNavCard DISTRESS_SIGNAL = new AllianceNavCard("Alliance Distress Signal", "");
    public static final AllianceNavCard FREIGHTER_CONVOY = new AllianceNavCard("Alliance Freighter Convey", "");

    public final String action;
    public final String text;

    AllianceNavCard(final String action, final String text) {
        this.action = action;
        this.text = text;
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public boolean equals(Object o) {
        return EqualsBuilder.reflectionEquals(this, o);
    }

    public AllianceNavCard clone() {
        return new AllianceNavCard(this.action, this.text);
    }

    @Override
    public String toString() {
        return this.action + "\n" + this.text;
    }
}
