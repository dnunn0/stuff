package com.whatgameapps.firefly.controller;

import com.google.gson.Gson;
import com.whatgameapps.firefly.rest.NavDeckStatus;
import org.eclipse.jetty.websocket.api.Session;

import java.util.ArrayList;
import java.util.List;

public class NewsSources {
    private static Gson gson = new Gson();
    final StatusBroadcaster listener;
    private final List<NavController> sources = new ArrayList<>();

    public NewsSources(StatusBroadcaster listener) {
        this.listener = listener;
    }

    void informListeners(NavDeckStatus status) {
        listener.broadcast(status);
    }

    public synchronized void addSource(NavController source) {
        this.sources.add(source);
    }

    public void update(Session user) {
        sources.stream()
                .map(nc -> nc.status())
                .map(statusObject -> gson.toJson(statusObject))
                .forEach(status -> listener.send(user, status));
    }
}
