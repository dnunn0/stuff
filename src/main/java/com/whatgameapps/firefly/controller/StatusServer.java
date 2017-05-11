package com.whatgameapps.firefly.controller;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

@WebSocket
public class StatusServer {
    private StatusBroadcaster broadcaster = new StatusBroadcaster();

    @OnWebSocketConnect
    public void onConnect(Session user) throws Exception {
        System.out.println("============received connect request " + user);
        broadcaster.addSubscriber(user);
    }

    @OnWebSocketClose
    public void onClose(Session user, int statusCode, String reason) {
        System.out.println("=====================received close request " + user);

        broadcaster.removeSubscriber(user);
    }

    @OnWebSocketMessage
    public void onMessage(Session user, String message) {
        System.out.println("Received message: " + message);
        broadcaster.broadcast();
    }
}
