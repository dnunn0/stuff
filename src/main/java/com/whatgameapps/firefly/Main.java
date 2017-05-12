package com.whatgameapps.firefly;

import com.whatgameapps.firefly.controller.AfterAll;
import com.whatgameapps.firefly.controller.AllianceSpaceNavController;
import com.whatgameapps.firefly.controller.BeforeAll;
import com.whatgameapps.firefly.controller.BorderSpaceNavController;
import com.whatgameapps.firefly.controller.DosFilter;
import com.whatgameapps.firefly.controller.RimSpaceNavController;
import com.whatgameapps.firefly.controller.StatusServer;
import com.whatgameapps.firefly.controller.StopController;
import spark.Service;

import java.util.Arrays;

public class Main {
    private SparkWrapper spark;
    private int port = 4567;
    private String spec;

    public Main(String[] args) throws Exception {
        processCommandLine(args);
        spark = new SparkWrapper(port);
        addEndpoints();
        System.out.println(String.format("Listening on: %s with %s", spark.url(), this.spec));
    }

    private void processCommandLine(String[] args) throws Exception {
        if (args.length != 2) {
            System.out.println("Expected 2 args, [port Spec-Name], got " + Arrays.asList(args));
            System.exit(-1);
        }
        port = Integer.parseInt(args[0]);
        spec = args[1];
    }

    private void addEndpoints() throws Exception {
        spark().staticFiles.location("/public");
        spark().staticFiles.expireTime(600);
        final StatusServer statusServer = new StatusServer();
        spark().webSocket("/status", statusServer);
        new BeforeAll(spark());
        new DosFilter(spark());
        new AllianceSpaceNavController(spark(), getSpecForSpecName(AllianceNavDeckSpecification.class, spec), statusServer.broadcaster);
        new BorderSpaceNavController(spark(), getSpecForSpecName(BorderNavDeckSpecification.class, spec), statusServer.broadcaster);
        new RimSpaceNavController(spark(), getSpecForSpecName(RimNavDeckSpecification.class, spec), statusServer.broadcaster);
        new StopController(spark());
        new AfterAll(spark());
    }

    public Service spark() {
        return spark.spark();
    }

    private NavDeckSpecification getSpecForSpecName(Class clazz, String specName) throws IllegalAccessException, NoSuchFieldException {
        return (NavDeckSpecification) clazz.getField(specName.toUpperCase()).get(null);
    }

    public static void main(String args[]) throws Exception {
        new Main(args);
    }

    public void stop() {
        spark.stop();
    }
}
