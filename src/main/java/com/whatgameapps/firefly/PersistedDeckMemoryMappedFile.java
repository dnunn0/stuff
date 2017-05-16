package com.whatgameapps.firefly;

import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class PersistedDeckMemoryMappedFile implements PersistedDeck {
    private final MemoryMappedFile memory;

    PersistedDeckMemoryMappedFile(FileChannel fc, MappedByteBuffer mbb) {
        mbb.rewind();
        this.memory = new MemoryMappedFile(fc, mbb);
    }

    @Override
    public void write(String storage) {
        this.memory.write(storage);
    }

    @Override
    public String read() {
        return this.memory.read();
    }
}
