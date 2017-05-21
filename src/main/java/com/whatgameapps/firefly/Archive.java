package com.whatgameapps.firefly;

public interface Archive {
    void write(String storage);

    String read();
}
