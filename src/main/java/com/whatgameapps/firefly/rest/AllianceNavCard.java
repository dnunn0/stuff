package com.whatgameapps.firefly.rest;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.apache.http.annotation.Immutable;

@Immutable
public class  AllianceNavCard {
    public static final AllianceNavCard RESHUFFLE = new AllianceNavCard("Alliance Cruiser - Reshuffle");
    public static final AllianceNavCard UNKNOWN = new AllianceNavCard("Alliance Huh");
    public static final AllianceNavCard KEEP_FLYING = new AllianceNavCard("The Big Black");
    public static final AllianceNavCard ALLIANCE_CRUISER = new AllianceNavCard("Alliance Contact - Cruiser");
    public static final AllianceNavCard CUSTOMS_INSPECTION = new AllianceNavCard("Customs Inspection");
    public static final AllianceNavCard MINOR_TECHNICAL = new AllianceNavCard(" Minor Technical Difficulty");
    public static final AllianceNavCard IFN_THE_COIL = new AllianceNavCard("If'n The Coil Busts We're Driftin'");
    public static final AllianceNavCard WHATS_GOING_ON = new AllianceNavCard("What's Going On In The Engine Room?");
    public static final AllianceNavCard SALVAGE_OPS = new AllianceNavCard("Alliance Salvage Ops");
    public static final AllianceNavCard SALVAGE_OPS_ABANDONED = new AllianceNavCard("Abandoned Ship");
    public static final AllianceNavCard REGULATED_SALVAGE = new AllianceNavCard("Alliance Regulated Salvage");
    public static final AllianceNavCard FAMILY_DINNER = new AllianceNavCard("Alliance Family Dinner");
    public static final AllianceNavCard DISTRESS_SIGNAL = new AllianceNavCard("Alliance Distress Signal");
    public static final AllianceNavCard FREIGHTER_CONVOY = new AllianceNavCard("Alliance Freighter Convey");
    // ======== EXPANSIONS
    public static final AllianceNavCard ALLIANCE_HARASSMENT = new AllianceNavCard("Alliance Corvette Contact");
    public static final AllianceNavCard ENHANCED_ENFORCEMENT = new AllianceNavCard("Alliance Enhanced Enforcement");
    public static final AllianceNavCard OUTBOUND_COLONISTS = new AllianceNavCard("Alliance Outbound Colonists");
    public static final AllianceNavCard ALLIANCE_ENTANGLEMENTS = new AllianceNavCard("Alliance Entanglements");
    public static final AllianceNavCard HE_LL_COME_AT_YOU = new AllianceNavCard("He'll Come at you Sideways");
    public static final AllianceNavCard LEAVE_NO_GROUND = new AllianceNavCard("Leave No Ground To Go To");
    public static final AllianceNavCard PERSISTENT_PURSUIT = new AllianceNavCard("Persistent Pursuit");
    public static final AllianceNavCard PASSENGER_UNREST = new AllianceNavCard("Passenger Unrest");
    public static final AllianceNavCard BADGERS_BOYS = new AllianceNavCard("Badger's Boys");
    public static final AllianceNavCard BROKEN_DOWN_SHUTTLE = new AllianceNavCard("Broken Down Shuttle");
    public static final AllianceNavCard ALLIANCE_CHECKPOINT = new AllianceNavCard("Alliance Checkpoint");
    public static final AllianceNavCard ALLIANCE_INTERROGATION = new AllianceNavCard("Alliance Interrogation");
    public static final AllianceNavCard FIRE_IN_THE_ENGINE_ROOM = new AllianceNavCard("Fire in the Engine Room");



    public final String action;

    AllianceNavCard(final String action) {
        this.action = action;
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
        return new AllianceNavCard(this.action);
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
