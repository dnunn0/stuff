package com.whatgameapps.firefly.rest;

public class NavDeckStatus {
    public final int remainingCardsCount;
    public final int discardsCount;
    public final boolean isLocked;

    public NavDeckStatus(int remainingCardsCount, int discardsCount, boolean isLocked) {
        this.remainingCardsCount = remainingCardsCount;
        this.discardsCount = discardsCount;
        this.isLocked = isLocked;
    }
}
