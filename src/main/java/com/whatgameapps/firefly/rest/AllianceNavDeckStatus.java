package com.whatgameapps.firefly.rest;

public class AllianceNavDeckStatus {
    public final int cardCount;
    public final int discardsCount;
    public final boolean isLocked;

    public AllianceNavDeckStatus(int cardCount, int discardsCount, boolean isLocked) {
        this.cardCount = cardCount;
        this.discardsCount = discardsCount;
        this.isLocked = isLocked;
    }
}
