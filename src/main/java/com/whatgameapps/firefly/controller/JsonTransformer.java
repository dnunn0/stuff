package com.whatgameapps.firefly.controller;

import com.google.gson.Gson;
import spark.ResponseTransformer;

public class JsonTransformer implements ResponseTransformer {
    private static JsonTransformer INSTANCE = new JsonTransformer();
    private static Gson gson = new Gson();

    public static JsonTransformer getInstance() {
        return INSTANCE;
    }

    @Override
    public String render(Object model) throws Exception {
//        System.out.println("rendering " + model);
        return gson.toJson(model);
    }
}
