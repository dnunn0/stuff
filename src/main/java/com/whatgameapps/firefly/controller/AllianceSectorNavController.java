package com.whatgameapps.firefly.controller;

import com.whatgameapps.firefly.AllianceNavDeck;
import com.whatgameapps.firefly.AllianceNavDeckSpecification;
import com.whatgameapps.firefly.rest.AllianceNavCard;
import org.eclipse.jetty.http.HttpStatus;
import spark.Request;
import spark.Response;
import spark.Service;

public class AllianceSectorNavController {
    public static final String PREFIX = "Returning - ";
    public static final String PATH = "/alliance/nav";
    public static final String LOCK_PATH = PATH + "/lock";
    public static final int NOT_FOUND_ERROR = HttpStatus.NOT_FOUND_404;
    public static final int LOCK_ERROR = HttpStatus.CONFLICT_409;
    final AllianceNavDeck deck;
    volatile DeckState deckState;

    public AllianceSectorNavController(Service spark, AllianceNavDeckSpecification spec) {
        this(spec);
        spark.post(PATH, this::reset);
        spark.post(LOCK_PATH, this::lock);
        spark.delete(LOCK_PATH, this::unlock);
        spark.options(LOCK_PATH, this::lockoptions);

        spark.get(PATH, this::drawCard, JsonTransformer.getInstance());
    }

    private String lockoptions(Request request, Response response) {
        //apparently needed for cross site request for chrome. AfterAll takes care of headers
        //http://stackoverflow.com/questions/33297190/response-for-preflight-has-invalid-http-status-code-404-angular-js
        return "OK";
    }

    AllianceSectorNavController(AllianceNavDeckSpecification spec) {
        this.deck = new AllianceNavDeck(spec);
        this.deckState = new UnlockedDeckState();
    }

    String reset(Request req, Response res) {
        String reply = this.deckState.shuffle(this, res);
        logReply("shuffle", res, reply);
        return reply;
    }

    private void logReply(String action, Response res, Object reply) {
        System.out.println(String.format("%s, Status: %d - Reply Object: %s", action, res.status(), reply));
    }

    AllianceNavCard drawCard(Request req, Response res) {
        AllianceNavCard reply = this.deckState.drawCard(this, res);
        logReply("draw", res, reply);
        return reply;
    }

    String lock(Request req, Response res) {
        String reply = this.deckState.lock(this, res);
        logReply("lock", res, reply);
        return reply;
    }

    String unlock(Request req, Response res) {
        String reply = this.deckState.unlock(this, res);
        logReply("unlock", res, reply);
        return reply;
    }
}
