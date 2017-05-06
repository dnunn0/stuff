package com.whatgameapps.firefly.controller;

import com.whatgameapps.firefly.rest.AllianceNavCard;
import spark.Request;
import spark.Response;
import spark.Service;

public class AllianceSectorNavController {

    public static final String PATH = "/allianceSectorNav";

    public AllianceSectorNavController(Service spark) {
        spark.get(PATH, this::drawCard, JsonTransformer.getInstance());
    }

    private AllianceNavCard drawCard(Request req, Response res) {
        System.out.println("Received request");
        return AllianceNavCard.KEEP_FLYING;
    }
}
