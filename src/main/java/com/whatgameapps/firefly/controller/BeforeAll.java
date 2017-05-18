package com.whatgameapps.firefly.controller;

import spark.Request;
import spark.Response;
import spark.Service;

import java.util.concurrent.atomic.AtomicLong;

public class BeforeAll {
    public static final AtomicLong id = new AtomicLong();

    public BeforeAll(Service spark) {
        spark.before((Request req, Response res) -> {
            res.header(NavController.ID_HEADER, System.currentTimeMillis() + ":" + id.incrementAndGet());
            String ipAddress = req.raw().getHeader("X-FORWARDED-FOR");
            if (ipAddress == null) {
                ipAddress = String.format("%s:%s", req.raw().getRemoteAddr(), req.raw().getRemotePort());
            }
            System.out.format("\n %TD:%<TT - %s - Received request for %s %s from %s\n",
                    System.currentTimeMillis(), res.raw().getHeader(NavController.ID_HEADER),
                    req.requestMethod(), req.uri(), ipAddress);
                }
        );
    }
}
