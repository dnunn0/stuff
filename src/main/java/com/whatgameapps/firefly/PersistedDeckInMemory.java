package com.whatgameapps.firefly;

public class PersistedDeckInMemory implements PersistedDeck {
    private String storage;

    @Override
    public void write(String storage) {
        this.storage = storage;

    }

    @Override
    public String read() {
        return this.storage;
    }
}
