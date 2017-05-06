package com.whatgameapps.firefly;

import com.whatgameapps.firefly.controller.AfterAll;
import com.whatgameapps.firefly.controller.AllianceSectorNavController;
import com.whatgameapps.firefly.controller.StopController;
import spark.Service;

public class Main {
    private SparkWrapper spark;
    private int port = 4567;

    public Main(String[] args) {
        processCommandLine(args);
        spark = new SparkWrapper(port);
        addEndpoints();
    }

    public static void main(String args[]) {
        Main main = new Main(args);
        System.out.println(String.format("Listening on: %s", main.url()));
    }

    public String url() {
        return spark.url();
    }

    private void addEndpoints() {

        new AllianceSectorNavController(spark());
        new AfterAll(spark());
        new StopController(spark());
    }

    private void processCommandLine(String[] args) {
        if (args.length > 0)
            port = Integer.parseInt(args[0]);
    }

    public Service spark() {
        return spark.spark();
    }

    public void stop() {
        spark.stop();
    }
}
