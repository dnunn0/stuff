package com.whatgameapps.firefly;

import com.whatgameapps.firefly.controller.AfterAll;
import com.whatgameapps.firefly.controller.AllianceSectorNavController;
import com.whatgameapps.firefly.controller.BeforeAll;
import com.whatgameapps.firefly.controller.DosFilter;
import com.whatgameapps.firefly.controller.StopController;
import spark.Service;

import java.util.Arrays;

public class Main {
    private SparkWrapper spark;
    private int port = 4567;
    private AllianceNavDeckSpecification spec;

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
        spec = getSpecForSpecName(args[1]);
    }

    private void addEndpoints() throws Exception {
        spark().staticFiles.location("/public");
        new BeforeAll(spark());
        new DosFilter(spark());
        new AllianceSectorNavController(spark(), spec);
        new StopController(spark());
        new AfterAll(spark());
    }

    private AllianceNavDeckSpecification getSpecForSpecName(final String specName) throws IllegalAccessException, NoSuchFieldException {
        return (AllianceNavDeckSpecification) AllianceNavDeckSpecification.class.getField(specName).get(null);
    }

    public Service spark() {
        return spark.spark();
    }
    public static void main(String args[]) throws Exception {
        new Main(args);
    }

    public void stop() {
        spark.stop();
    }
}
