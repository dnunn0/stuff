package com.whatgameapps.firefly.controller;

import spark.Request;
import spark.Response;
import spark.Service;

public class AllianceSectorController {

    public static final String PATH = "/allianceSector";

    public AllianceSectorController(Service spark) {
        spark.get(PATH, this::drawCard);

    }

    private String drawCard(Request req, Response res) {
        System.out.println("Received request");
        return "OK";
    }
}
