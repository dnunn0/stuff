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
    public static final String METADATA_SEPARATOR = ":";
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
        if (!shouldJoinExisting()) this.memory.write("");

        addEndpoints();
        System.out.println(String.format("Listening on: %s with %s", spark.url(), this.specName));
    }

    private void processCommandLine(String[] args) throws Exception {
        if (args.length != 2) {
            System.out.println("Expected 2 args, [port Spec-Name], got " + Arrays.asList(args));
            System.exit(-1);
        }
        port = Integer.parseInt(args[0]);
        specName = args[1].toUpperCase();
    }

    private boolean shouldJoinExisting() {
        return "JOIN".equalsIgnoreCase(this.specName);
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
        NavDeck rim = configureNavDeck(RimNavDeckSpecification.class);

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
        String entryKey = specClass.getName();
        String decks = getMetadata();

        if (shouldJoinExisting()) {
            String[] split = decks.split(entryKey);
            String decksBefore = split[0];
            int deckNbr = countEntries(decksBefore);
            final PersistedDeck storage = createStorage(deckNbr);
            System.out.format("Spec class %s deckNbr %d split %s\n", entryKey, deckNbr, split);
            String runningSpecName = split[1].split("" + METADATA_DELIMITER)[0].replace(METADATA_SEPARATOR, "");
            return NavDeck.OldFrom(getSpecForSpecName(specClass, runningSpecName), storage);
        } else {
            int deckNbr = countEntries(decks);
            final PersistedDeck storage = createStorage(deckNbr);
            this.memory.write(decks + entryKey + METADATA_SEPARATOR + this.specName + METADATA_DELIMITER);
            return NavDeck.NewFrom(getSpecForSpecName(specClass, specName), storage);
        }
    }

    public String getMetadata() {
        return this.memory.read();
    }

    private int countEntries(String decks) {
        return (int) decks.chars().filter(ch -> ch == METADATA_DELIMITER).count();
    }

    private PersistedDeck createStorage(int deckNbr) throws IOException {
        int position = METADATA_STORAGE_SIZE + (DECK_STORAGE_SIZE * deckNbr);
        final MappedByteBuffer mbb = fc.map(FileChannel.MapMode.READ_WRITE, position, DECK_STORAGE_SIZE);
        return new PersistedDeckMemoryMappedFile(fc, mbb);
    }

    private NavDeckSpecification getSpecForSpecName(Class clazz, String specName) throws IllegalAccessException, NoSuchFieldException {
        return (NavDeckSpecification) clazz.getField(specName).get(null);
    }

    public static void main(String args[]) throws Exception {
        try {
            new Main(args);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.format("Fatal error. Exiting.============================\n");
            System.exit(-1);
        }
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
