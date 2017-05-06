package com.whatgameapps.firefly.rest;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class  AllianceNavCard {
    public static final AllianceNavCard KEEP_FLYING = new AllianceNavCard("Alliance: Tbe Big Black", "");
    public static final AllianceNavCard CRUISER_CONTACT = new AllianceNavCard("Alliance Cruiser Contact", "");
    public static final AllianceNavCard CORVETTE_CONTACT = new AllianceNavCard("Alliance Corvette Contact", "");
    public static final AllianceNavCard UNKNOWN = new AllianceNavCard("Alliance Huh", "");;

    public final String action;
    public final String text;

    AllianceNavCard(final String action, final String text) {
        this.action = action;
        this.text = text;
    }

    public AllianceNavCard clone() {
        return new AllianceNavCard(this.action, this.text);
    }

    @Override
    public boolean equals(Object o) {
        return EqualsBuilder.reflectionEquals(this, o);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public String toString() {
        return this.action + "\n" + this.text;
    }
}
