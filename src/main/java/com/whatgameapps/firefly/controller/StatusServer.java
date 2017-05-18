package com.whatgameapps.firefly.controller;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

@WebSocket
public class StatusServer {
    public final StatusBroadcaster broadcaster = new StatusBroadcaster();

    @OnWebSocketConnect
    public void onConnect(Session user) throws Exception {
        System.out.format("\n%TD:%<TT - Received connect request %s",
                System.currentTimeMillis(), user.getRemoteAddress());
        broadcaster.addSubscriber(user);
    }

    @OnWebSocketClose
    public void onClose(Session user, int statusCode, String reason) {
        System.out.format("\n%TD:%<TT - Received close request from %s reason: %d %s\n",
                System.currentTimeMillis(), user.getRemoteAddress(), statusCode, reason);

        broadcaster.removeSubscriber(user);
    }

    @OnWebSocketMessage
    public void onMessage(Session user, String message) {
        System.out.format("\n%TD:%<TT - Received message from %s message: [%s]\n",
                System.currentTimeMillis(), user.getRemoteAddress(), message);
        broadcaster.update(user);
    }

}
