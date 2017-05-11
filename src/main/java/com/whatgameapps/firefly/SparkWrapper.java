package com.whatgameapps.firefly;

import spark.Service;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class SparkWrapper {
    public final Service spark;
    public final int port;

    public SparkWrapper(int port) {
        this.port = port;
        spark = Service.ignite();
    }

    public void ignite() {
        Service.ignite().port(port).threadPool(10);
    }

    public String url() {
        return urlFor("/");
    }

    public String urlFor(String endpoint) {
        final String checkedEndpoint = endpoint.startsWith("/") ? endpoint : String.format("/%s", endpoint);
        try {
            final InetAddress hostname = InetAddress.getLocalHost();
            return String.format("http://%s:%d%s", hostname.getHostAddress(), spark().port(), checkedEndpoint);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Service spark() {
        return spark;
    }

    public void stop() {
        spark.stop();
    }
}
