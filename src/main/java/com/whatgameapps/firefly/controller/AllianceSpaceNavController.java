package com.whatgameapps.firefly.controller;

import com.whatgameapps.firefly.NavDeckSpecification;
import spark.Service;

public class AllianceSpaceNavController extends NavController {

    public static final String DECK_PATH = "/alliance";

    public AllianceSpaceNavController(Service spark, NavDeckSpecification spec, NewsSources listeners) {
        super(spark, spec, listeners);
    }

    AllianceSpaceNavController(NavDeckSpecification spec, NewsSources listeners) {
        super(spec, listeners);
    }

    @Override
    public String getSpaceSectorPath() {
        return DECK_PATH;
    }

}
