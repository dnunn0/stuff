package com.whatgameapps.firefly.controller;

import spark.Service;

public class StopController {
    public StopController(Service spark) {
        spark.get("/bluenoyellow", (req, res) -> {
            spark.stop();
            return "OK";
        });
    }
}
