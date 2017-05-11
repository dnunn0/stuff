package com.whatgameapps.firefly.controller;

import spark.Request;
import spark.Response;
import spark.Service;

import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

public class DosFilter {
    /**
     * Span over which to guess a DOS is occurring.
     */
    public static final long DURATION_MS = 1000 * 60;
    /**
     * nbr of http requests allowed in the duration. typically there are 4 http requests per user request
     * (OPTIONS, user request, OPTIONS, status).
     */
    public static final int maxHttpRequestsAllowedPerDuration = 4 * 30;
    final BlockingDeque<Long> history = new LinkedBlockingDeque<>(maxHttpRequestsAllowedPerDuration + 10);

    public DosFilter(Service spark) {
        spark.before((Request req, Response res) -> {
            long currentTime = System.currentTimeMillis();
            history.add(currentTime);
            if (history.size() >= maxHttpRequestsAllowedPerDuration) {
                long currentDuration = currentTime - history.getFirst();
                history.removeFirst();
                if (currentDuration <= DURATION_MS) {
                    System.out.format("FATAL: Too many requests in too short a time: %d in %ds\n", history.size(), currentDuration / 1000);
                    spark.stop();
                }
            }
        });
    }
}
