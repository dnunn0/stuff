package com.whatgameapps.firefly.controller;

import com.google.gson.Gson;
import spark.ResponseTransformer;

public class JsonRenderer implements ResponseTransformer {
    private static JsonRenderer INSTANCE = new JsonRenderer();
    private static Gson gson = new Gson();

    public static JsonRenderer getInstance() {
        return INSTANCE;
    }

    @Override
    public String render(Object model) {
//        System.out.println("rendering " + model);
        return gson.toJson(model);
    }
}
