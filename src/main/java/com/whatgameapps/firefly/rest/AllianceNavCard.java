package com.whatgameapps.firefly.rest;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.apache.http.annotation.Immutable;

@Immutable
public class  AllianceNavCard {
    public static final String RESHUFFLE = "RESHUFFLE";
    public static final String UNKNOWN = "Missed one (or more)";

    public final String action;

    public AllianceNavCard(final String action) {
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

    public boolean isReshuffle() {
        return this.action.toUpperCase().contains(AllianceNavCard.RESHUFFLE.toUpperCase());
    }
}
