package com.whatgameapps.firefly.controller;

import com.whatgameapps.firefly.rest.NavDeckStatus;
import org.eclipse.jetty.websocket.api.Session;

import java.util.HashSet;
import java.util.Set;

public class StatusBroadcaster {
    private final Set<Session> subscribers = new HashSet();
    private final Set<NavController> sources = new HashSet();

    synchronized void addSubscriber(Session user) {
        subscribers.add(user);
    }

    synchronized void removeSubscriber(Session user) {
        subscribers.add(user);
    }

    public synchronized void addSource(NavController source) {
        this.sources.add(source);
    }

    void informListeners(NavDeckStatus status) {
        String statusJson = toJson(status);
        this.broadcast(statusJson);
    }

    private String toJson(NavDeckStatus status) {
        return JsonRenderer.getInstance().render(status);
    }

    public synchronized void broadcast(String message) {
        subscribers.stream()
                .filter(Session::isOpen)
                .forEach(session -> this.send(session, message));
    }

    public void send(Session user, String message) {
        user.getRemote().sendStringByFuture(message);
        System.out.format("Send to %s: [%s]\n", user.getRemoteAddress(), message);
    }

    public void update(Session user) {
        sources.stream()
                .map(nc -> nc.status())
                .map(statusObject -> toJson(statusObject))
                .forEach(status -> this.send(user, status));
    }

}
