package com.whatgameapps.firefly.com.whatgameapps.firefly.helper;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.builder.ResponseSpecBuilder;
import com.jayway.restassured.http.ContentType;
import com.whatgameapps.firefly.Main;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class TestUtils {
    public final PrintStream originalOut = System.out;
    public final ByteArrayOutputStream outStream = new ByteArrayOutputStream();
    public final PrintStream output = new PrintStream(outStream, true);
    public Main main;

    public TestUtils(int port, String spec) {
        this();
        startApplication(port, spec);
        RestAssured.port = port;
    }

    public TestUtils() {
        squelchLogs();
    }

    public void startApplication(int port, String spec) {
        main = null;
        try {
            main = new Main(new String[]{Integer.toString(port), spec});
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void squelchLogs() {
        Logger log = LogManager.getLogManager().getLogger("");
        for (Handler h : log.getHandlers()) {
            h.setLevel(Level.WARNING);
        }
    }

    public void redirectStdout() {
        System.setOut(output);
    }

    public void restoreStdout() {
        System.setOut(originalOut);
        //           System.out.println(output.toString());
    }

    public void cleanup() {
        stopApplication();
    }

    public void stopApplication() {
        main.stop();
        sleep(30);
    }

    public void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public ResponseSpecBuilder getSpecBuilder(int status) {
        ResponseSpecBuilder builder = new ResponseSpecBuilder();
        builder.expectStatusCode(status);
        builder.expectContentType(ContentType.JSON);
        return builder;
    }
}
