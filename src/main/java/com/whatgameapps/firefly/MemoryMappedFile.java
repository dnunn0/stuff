package com.whatgameapps.firefly;

import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;

public class MemoryMappedFile {
    private static final int MAX_RETRIES = 3;
    private final MappedByteBuffer mbb; //TODO do I need this? or just recreate it?
    private final FileChannel fc;
    private final int position;

    public MemoryMappedFile(FileChannel fc, MappedByteBuffer mbb) {
        this.fc = fc;
        this.mbb = mbb;
        this.position = mbb.position();
    }

    public void write(String storage) {
        write(storage, 0);
    }

    public void write(String storage, int attemptCount) {
        try {
            try (FileLock lock = fc.lock(this.position, this.mbb.capacity(), false)) {
                byte[] buffer = storage.getBytes();
                mbb.position(position);
                mbb.putInt(buffer.length);
//                System.out.format("put %d mbb %d fc %d\n", buffer.length, mbb.position(), fc.position());
                mbb.put(buffer);
            }
        } catch (IOException ex) {
            if (attemptCount < MAX_RETRIES) write(storage, attemptCount + 1);
            throw new RuntimeException(ex);
        }
    }

    public String read() {
        return read(0);
    }

    private String read(int attemptCount) {
        try {

            try (FileLock lock = fc.lock(this.position, this.mbb.capacity(), false)) {
                mbb.position(position);
                int size = mbb.getInt(0);
                mbb.position(position + 4);
//                System.out.format("read %d mbb %d fc %d\n", size, mbb.position(), fc.position());
                byte[] buffer = new byte[size];
                mbb.get(buffer);
                return new String(buffer);
            }
        } catch (IOException ex) {
            if (attemptCount < MAX_RETRIES) return read(attemptCount + 1);
            throw new RuntimeException(ex);
        }
    }
}
