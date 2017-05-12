package com.whatgameapps.firefly.controller;

import com.google.gson.Gson;
import com.whatgameapps.firefly.rest.NavDeckStatus;
import org.eclipse.jetty.websocket.api.Session;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class StatusBroadcaster {
    private static final Gson gson = new Gson();
    public final NewsSources sources = new NewsSources(this);
    private final Map<Session, String> subscribers = new ConcurrentHashMap<>();

    public synchronized void broadcast(NavDeckStatus status) {
        String statusJson = gson.toJson(status);

        subscribers.keySet().stream()
                .filter(Session::isOpen)
                .forEach(session -> send(session, statusJson));
    }

    public void send(Session user, String message) {
        user.getRemote().sendStringByFuture(message);
    }

    synchronized void addSubscriber(Session user) {
        subscribers.put(user, "string");
    }

    synchronized void removeSubscriber(Session user) {
        subscribers.put(user, "string");

    }

    public void update(Session user) {
        sources.update(user);
    }
}
