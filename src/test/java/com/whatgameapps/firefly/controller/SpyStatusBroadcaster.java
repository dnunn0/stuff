package com.whatgameapps.firefly.controller;

import org.eclipse.jetty.websocket.api.Session;

import java.util.HashMap;

public class SpyStatusBroadcaster extends StatusBroadcaster {

    private HashMap<Session, String> messages = new HashMap();

    public void send(Session user, String message) {
        this.messages.put(user, message);
    }
}
