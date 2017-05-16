package com.whatgameapps.firefly;

public interface PersistedDeck {
    void write(String storage);

    String read();
}
