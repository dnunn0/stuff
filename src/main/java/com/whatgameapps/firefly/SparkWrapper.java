package com.whatgameapps.firefly;

import spark.Service;

public class SparkWrapper {
    public Service spark;

    public SparkWrapper(int port) {
        spark = Service.ignite().port(port).threadPool(10);
    }

    public String url() {
        return urlFor("/");
    }

    public String urlFor(String endpoint) {
        String checkedEndpoint = endpoint.startsWith("/") ? endpoint : String.format("/%s", endpoint);
        return String.format("http://localhost:%d%s", spark().port(), checkedEndpoint);
    }

    public Service spark() {
        return spark;
    }

    public void stop() {
        spark.stop();
    }

    public int port() {
        return spark.port();
    }
}
