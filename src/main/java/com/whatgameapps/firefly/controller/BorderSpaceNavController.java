package com.whatgameapps.firefly.controller;

import com.whatgameapps.firefly.NavDeckSpecification;
import spark.Service;

public class BorderSpaceNavController extends NavController {

    public static final String DECK_PATH = "/border";

    public BorderSpaceNavController(Service spark, NavDeckSpecification spec, StatusBroadcaster listeners) {
        super(spark, spec, listeners);
    }

    BorderSpaceNavController(NavDeckSpecification spec, StatusBroadcaster listeners) {
        super(spec, listeners);
    }

    @Override
    public String getSpaceSectorPath() {
        return DECK_PATH;
    }

}
