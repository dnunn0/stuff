package com.whatgameapps.firefly.com.whatgameapps.firefly.helper;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.builder.ResponseSpecBuilder;
import com.jayway.restassured.http.ContentType;
import com.whatgameapps.firefly.Main;

import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class TestUtils {
    private Main main;

    public TestUtils(int port) {
        squelchLogs();
        main = startApplication(port);
        RestAssured.port = port;
    }

    public static void squelchLogs() {
        Logger log = LogManager.getLogManager().getLogger("");
        for (Handler h : log.getHandlers()) {
            h.setLevel(Level.WARNING);
        }
    }

    public void cleanup() {
        stopApplication();
    }

    private Main startApplication(int port) {
        return new Main(new String[]{Integer.toString(port)});
    }

    private void stopApplication() {
        main.stop();
    }

    public ResponseSpecBuilder getSpecBuilder(int status) {
        ResponseSpecBuilder builder = new ResponseSpecBuilder();
        builder.expectStatusCode(status);
        builder.expectContentType(ContentType.JSON);
        return builder;
    }

}
