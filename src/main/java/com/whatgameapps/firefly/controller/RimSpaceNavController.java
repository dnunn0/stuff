package com.whatgameapps.firefly.controller;

import com.whatgameapps.firefly.NavDeckSpecification;
import spark.Service;

public class RimSpaceNavController extends NavController {

    public static final String DECK_PATH = "/rim";

    public RimSpaceNavController(Service spark, NavDeckSpecification spec) {
        super(spark, spec);
    }

    RimSpaceNavController(NavDeckSpecification spec) {
        super(spec);
    }

    @Override
    public String getSpaceSectorPath() {
        return DECK_PATH;
    }

}
