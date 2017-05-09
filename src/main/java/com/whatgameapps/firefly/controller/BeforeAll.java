package com.whatgameapps.firefly.controller;

import spark.Request;
import spark.Response;
import spark.Service;

import java.util.concurrent.atomic.AtomicLong;

public class BeforeAll {
    public static final AtomicLong id = new AtomicLong();

    public BeforeAll(Service spark) {
        spark.before((Request req, Response res) -> {
                    res.header(AllianceSectorNavController.ID_HEADER, System.currentTimeMillis() + ":" + id.incrementAndGet());
                    System.out.format("\n%s - Received request for %s %s\n", res.raw().getHeader(AllianceSectorNavController.ID_HEADER), req.requestMethod(), req.uri());
                }
        );
    }
}
