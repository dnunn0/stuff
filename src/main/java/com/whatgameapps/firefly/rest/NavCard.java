package com.whatgameapps.firefly.rest;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.apache.http.annotation.Immutable;

@Immutable
public class NavCard {
    public static final String RESHUFFLE = "RESHUFFLE";
    public static final String UNKNOWN = "Missed one (or more)";

    public final String action;

    public NavCard(String action) {
        this.action = action;
    }

    public static NavCard From(String storage) {
        return new NavCard(storage);
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
    public NavCard clone() {
        return new NavCard(this.action);
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

    public boolean isReshuffle() {
        return this.action.toUpperCase().contains(NavCard.RESHUFFLE.toUpperCase());
    }

    public String externalized() {
        return this.action;
    }
}
