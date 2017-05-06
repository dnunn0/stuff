package com.whatgameapps.firefly.com.whatgameapps.firefly.helper;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.builder.RequestSpecBuilder;
import com.jayway.restassured.specification.RequestSpecification;
import com.whatgameapps.firefly.Main;

import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class TestUtils {
    private Main main;
    private RequestSpecification serverConnection;

    public TestUtils(int port) {
        squelchLogs();
        main = startApplication(port);
        serverConnection = new RequestSpecBuilder().setBaseUri(main.url()).build();
    }

    public static void squelchLogs() {
        Logger log = LogManager.getLogManager().getLogger("");
        for (Handler h : log.getHandlers()) {
            h.setLevel(Level.WARNING);
        }
    }

    public void cleanup() {
        stopApplication();
        serverConnection = null;
    }

    private Main startApplication(int port) {
        return new Main(new String[]{Integer.toString(port)});
    }

    private void stopApplication() {
        main.stop();
    }

    public RequestSpecification given() {
        return RestAssured.given(serverConnection);
    }


}
