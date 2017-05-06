package com.whatgameapps.firefly.controller;

import com.whatgameapps.firefly.com.whatgameapps.firefly.helper.TestUtils;
import org.junit.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;

public class AllianceSectorControllerIntegratedTest {
    private static TestUtils testUtils;
    private PrintStream originalOut;
    private ByteArrayOutputStream output;


    @BeforeClass
    public static void setUp() {
        testUtils = new TestUtils(9876);
    }

    @AfterClass
    public static void tearDown() {
        testUtils.cleanup();
    }

    @Before
    public void redirectStdout() throws Exception {
        originalOut = System.out;
        output = new ByteArrayOutputStream();
        PrintStream stream = new PrintStream(output, true, StandardCharsets.UTF_8.name());
        System.setOut(stream);
    }

    @After
    public void restoreStdout() {
        System.setOut(originalOut);
    }

    @Test
    public void shouldReturn200WhenSuccessful() throws Exception {
        sendOnePacketToRecorderEndpoint();
    }

    @Test
    public void shouldLogRequestToStdout() throws Exception {
        sendOnePacketToRecorderEndpoint();

        String result = new String(output.toByteArray(), StandardCharsets.UTF_8);
        assertThat(result, containsString("Received"));
    }

    private void sendOnePacketToRecorderEndpoint() {
        testUtils.given()
                .when()
                .get(AllianceSectorController.PATH)
                .then()
                .statusCode(200);
    }

}
