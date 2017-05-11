package com.whatgameapps.firefly.controller;

import com.google.gson.Gson;
import com.whatgameapps.firefly.rest.NavDeckStatus;
import org.eclipse.jetty.websocket.api.Session;
import spark.Spark;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class StatusBroadcaster {
    private static final Gson gson = new Gson();

    private final Map<Session, String> subscribers = new ConcurrentHashMap<>();

    public static void main(String[] args) {
        Spark.staticFiles.location("/public"); //index.html is served at localhost:4567 (default port)
        Spark.staticFiles.expireTime(600);
        Spark.webSocket("/statusBroadcast", StatusServer.class);
        Spark.init();
    }

    public void broadcast() {

        subscribers.keySet().stream().filter(Session::isOpen).forEach(session -> {
            try {
                session.getRemote().sendString(gson.toJson(new NavDeckStatus(1, 1, false)));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public synchronized void addSubscriber(Session user) {
        subscribers.put(user, "string");
    }

    public synchronized void removeSubscriber(Session user) {
        subscribers.put(user, "string");

    }
}
