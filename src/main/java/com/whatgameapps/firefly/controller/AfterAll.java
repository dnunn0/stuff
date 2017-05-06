package com.whatgameapps.firefly.controller;

import org.apache.http.entity.ContentType;
import spark.Service;

public class AfterAll {
    public AfterAll(Service spark) {
        spark.after((req, res) -> res.type(ContentType.APPLICATION_JSON.toString()));
    }
}
