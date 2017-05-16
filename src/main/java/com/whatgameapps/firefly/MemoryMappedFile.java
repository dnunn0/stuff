package com.whatgameapps.firefly;

import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;

public class MemoryMappedFile {
    private final MappedByteBuffer mbb; //TODO do I need this? or just recreate it?
    private final FileChannel fc;
    private final int position;

    public MemoryMappedFile(FileChannel fc, MappedByteBuffer mbb) {
        this.fc = fc;
        this.mbb = mbb;
        this.position = mbb.position();
    }

    public void write(String storage) {
        try {
            try (FileLock lock = fc.lock(this.position, this.mbb.capacity(), false)) {
                byte[] buffer = storage.getBytes();
                mbb.position(position);
                mbb.putInt(buffer.length);
                System.out.format("put %d as count, pos now at %d %d\n", buffer.length, mbb.position(), fc.position());
                mbb.put(buffer);
            }
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public String read() {
        try {

            try (FileLock lock = fc.lock(this.position, this.mbb.capacity(), false)) {
                mbb.position(position);
                int size = mbb.getInt(0);
                mbb.position(position + 4);
                System.out.format("read size of %d mbb at %d fc at %d", size, mbb.position(), fc.position());
                byte[] buffer = new byte[size];
                mbb.get(buffer);
                return new String(buffer);
            }
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}
