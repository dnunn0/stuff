package com.whatgameapps.firefly.controller;

import com.whatgameapps.firefly.NavDeck;
import com.whatgameapps.firefly.NavDeckSpecification;
import com.whatgameapps.firefly.rest.NavCard;
import com.whatgameapps.firefly.rest.NavDeckStatus;
import org.eclipse.jetty.http.HttpStatus;
import spark.Request;
import spark.Response;
import spark.Service;

public class AllianceSectorNavController {
    public static final String PATH = "/alliance/nav";
    public static final String LOCK_PATH = PATH + "/lock";
    public static final String STATUS_PATH = PATH + "/status";
    public static final String ID_HEADER = "id";

    public static final int NOT_FOUND_ERROR = HttpStatus.NOT_FOUND_404;
    public static final int LOCK_ERROR = HttpStatus.CONFLICT_409;
    final NavDeck deck;
    volatile DeckState deckState;

    public AllianceSectorNavController(Service spark, NavDeckSpecification spec) {
        this(spec);
        spark.post(LOCK_PATH, this::lock);
        spark.delete(LOCK_PATH, this::unlock);
        spark.options(LOCK_PATH, this::allowCors);
        spark.get(STATUS_PATH, this::status, JsonTransformer.getInstance());
        spark.options(STATUS_PATH, this::allowCors);
        spark.get(PATH, this::drawCard, JsonTransformer.getInstance());
        spark.post(PATH, this::reset);
        spark.options(PATH, this::allowCors);
    }

    AllianceSectorNavController(NavDeckSpecification spec) {
        this.deck = new NavDeck(spec);
        this.deckState = new UnlockedDeckState();
    }

    private String allowCors(Request request, Response response) {
        //apparently needed for cross site request for chrome. AfterAll takes care of headers
        //http://stackoverflow.com/questions/33297190/response-for-preflight-has-invalid-http-status-code-404-angular-js
        return "OK";
    }

    String reset(Request req, Response res) {
        String reply = this.deckState.shuffle(this, res);
        return reply;
    }

    NavCard drawCard(Request req, Response res) {
        NavCard reply = this.deckState.drawCard(this, res);
        return reply;
    }

    String lock(Request req, Response res) {
        String reply = this.deckState.lock(this, res);
        return reply;
    }

    String unlock(Request req, Response res) {
        String reply = this.deckState.unlock(this, res);
        return reply;
    }

    NavDeckStatus status(Request req, Response res) {
        int cardCount = this.deck.size();
        int discardsCount = this.deck.discards().size();
        boolean isLocked = this.deckState.isLocked();
        NavDeckStatus status = new NavDeckStatus(cardCount, discardsCount, isLocked);
        return status;
    }
}
