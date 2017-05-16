package com.whatgameapps.firefly;

import com.whatgameapps.firefly.controller.AfterAll;
import com.whatgameapps.firefly.controller.BeforeAll;
import com.whatgameapps.firefly.controller.DosFilter;
import com.whatgameapps.firefly.controller.NavController;
import com.whatgameapps.firefly.controller.StatusServer;
import com.whatgameapps.firefly.controller.StopController;
import spark.Service;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Arrays;

public class Main {
    public static final char METADATA_DELIMITER = '\n';
    private static final int DECK_STORAGE_SIZE = 2048;
    private static final int METADATA_STORAGE_SIZE = 1024;
    private static final int MAX_SIZE = METADATA_STORAGE_SIZE + (10 * (DECK_STORAGE_SIZE * 3));
    private final File tmpFile = new File(System.getProperty("java.io.tmpdir"), "fireflyNavDeck.txt");
    private final MemoryMappedFile memory;

    private SparkWrapper spark;
    private int port = 4567;
    private String specName;
    private FileChannel fc;

    public Main(String[] args) throws Exception {
        processCommandLine(args);
        spark = new SparkWrapper(port);

        this.fc = new RandomAccessFile(tmpFile, "rw").getChannel();
        final MappedByteBuffer mbb = fc.map(FileChannel.MapMode.READ_WRITE, 0, METADATA_STORAGE_SIZE);
        this.memory = new MemoryMappedFile(fc, mbb);
        this.memory.write("");

        addEndpoints();
        System.out.println(String.format("Listening on: %s with %s", spark.url(), this.specName));
    }

    private void processCommandLine(String[] args) throws Exception {
        if (args.length != 2) {
            System.out.println("Expected 2 args, [port Spec-Name], got " + Arrays.asList(args));
            System.exit(-1);
        }
        port = Integer.parseInt(args[0]);
        specName = args[1];
    }

    private void addEndpoints() throws Exception {
        spark().staticFiles.location("/public");
        int expirySeconds = 60 * 60 * 5;
        spark().staticFiles.expireTime(expirySeconds);
        final StatusServer statusServer = new StatusServer();
        spark().webSocket("/status", statusServer);
        new BeforeAll(spark());
        new DosFilter(spark());

        //TODO need to implement join here. for now, just assume no joining
        NavDeck alliance = configureNavDeck(AllianceNavDeckSpecification.class);
        NavDeck border = configureNavDeck(BorderNavDeckSpecification.class);
        NavDeck rim = configureNavDeck(BorderNavDeckSpecification.class);

        new NavController(spark(), NavController.ALLIANCE_SPACE, alliance, statusServer.broadcaster);
        new NavController(spark(), NavController.BORDER_SPACE, border, statusServer.broadcaster);
        new NavController(spark(), NavController.RIM_SPACE, rim, statusServer.broadcaster);

        new StopController(spark());
        new AfterAll(spark());
    }

    public Service spark() {
        return spark.spark();
    }

    private NavDeck configureNavDeck(Class<? extends NavDeckSpecification> specClass) throws Exception {
        String decks = this.memory.read();
        int deckNbr = (int) decks.chars().filter(ch -> ch == METADATA_DELIMITER).count();
        this.memory.write(decks + specClass.getName() + ":" + this.specName + METADATA_DELIMITER);

        int position = METADATA_STORAGE_SIZE + (DECK_STORAGE_SIZE * deckNbr);
        final MappedByteBuffer mbb = fc.map(FileChannel.MapMode.READ_WRITE, position, DECK_STORAGE_SIZE);
        final PersistedDeck storage = new PersistedDeckMemoryMappedFile(fc, mbb);
        return new NavDeck(getSpecForSpecName(specClass, specName), storage);
    }

    private NavDeckSpecification getSpecForSpecName(Class clazz, String specName) throws IllegalAccessException, NoSuchFieldException {
        return (NavDeckSpecification) clazz.getField(specName.toUpperCase()).get(null);
    }

    public static void main(String args[]) throws Exception {
        new Main(args);
    }

    public void stop() {
        try {
            this.fc.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        spark.stop();
    }
}
