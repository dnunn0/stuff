package com.whatgameapps.firefly.controller;

import org.apache.http.entity.ContentType;
import spark.Request;
import spark.Response;
import spark.Service;

public class AfterAll {
    public AfterAll(Service spark) {
        spark.after((Request req, Response res) -> {
            System.out.format("Request %s %s Status %d responseText %s\n", req.requestMethod(), req.uri(), res.status(), res.body());

            res.type(ContentType.APPLICATION_JSON.toString());
                    res.header("Cache-Control", "no-cache");
                    res.header("Access-Control-Allow-Origin", "*");
                    res.header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
                    res.header("Access-Control-Allow-Headers", "Content-Type, Cache-Control");
                }
        );
    }
}
