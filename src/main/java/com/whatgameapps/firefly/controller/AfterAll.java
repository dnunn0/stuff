package com.whatgameapps.firefly.controller;

import org.apache.http.entity.ContentType;
import spark.Request;
import spark.Response;
import spark.Service;

import java.sql.Timestamp;

public class AfterAll {
    public AfterAll(Service spark) {
        spark.after((Request req, Response res) -> {
            System.out.format("\t%TD:%<TT - %s - Status %d. ResponseText: [%s]\n", new Timestamp(System.currentTimeMillis()), res.raw().getHeader(NavController.ID_HEADER), res.status(), res.body());

            res.type(ContentType.APPLICATION_JSON.toString());
                    res.header("Access-Control-Allow-Origin", "*");
                    res.header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
            res.header("Access-Control-Allow-Headers", "Content-Type, Authorize");
            res.header("Access-Control-Expose-Headers", NavController.ID_HEADER);
                }
        );
    }
}
