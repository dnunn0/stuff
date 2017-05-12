package com.whatgameapps.firefly.controller;

import com.whatgameapps.firefly.NavDeckSpecification;
import spark.Service;

public class RimSpaceNavController extends NavController {

    public static final String DECK_PATH = "/rim";

    public RimSpaceNavController(Service spark, NavDeckSpecification spec, StatusBroadcaster listeners) {
        super(spark, spec, listeners);
    }

    RimSpaceNavController(NavDeckSpecification spec, StatusBroadcaster listeners) {
        super(spec, listeners);
    }

    @Override
    public String getSpaceSectorPath() {
        return DECK_PATH;
    }

}
