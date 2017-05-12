package com.whatgameapps.firefly.rest;

public class NavDeckStatus {
    public final String source;
    public final int remainingCardsCount;
    public final int discardsCount;
    public final boolean isLocked;

    public NavDeckStatus(String source, int remainingCardsCount, int discardsCount, boolean isLocked) {
        this.source = source;
        this.remainingCardsCount = remainingCardsCount;
        this.discardsCount = discardsCount;
        this.isLocked = isLocked;
    }
}
