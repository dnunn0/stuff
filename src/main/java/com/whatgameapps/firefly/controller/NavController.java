package com.whatgameapps.firefly.controller;

import com.whatgameapps.firefly.NavDeck;
import com.whatgameapps.firefly.NavDeckSpecification;
import com.whatgameapps.firefly.rest.NavCard;
import com.whatgameapps.firefly.rest.NavDeckStatus;
import org.eclipse.jetty.http.HttpStatus;
import spark.Request;
import spark.Response;
import spark.Service;

public abstract class NavController {
    public static final String ID_HEADER = "id";
    public static final int NOT_FOUND_ERROR = HttpStatus.NOT_FOUND_404;
    public static final int LOCK_ERROR = HttpStatus.CONFLICT_409;
    public static final String NAV = "/nav";
    public static final String LOCK = "/lock";
    public static final String STATUS = "/status";
    final NavDeck deck;
    final NewsSources listeners;
    volatile DeckState deckState;

    public NavController(Service spark, NavDeckSpecification spec, NewsSources listeners) {
        this(spec, listeners);
        spark.post(getLockPath(), this::lock);
        spark.delete(getLockPath(), this::unlock);
        spark.options(getLockPath(), this::allowCors);
        spark.get(getStatusPath(), this::status, JsonTransformer.getInstance());
        spark.options(getStatusPath(), this::allowCors);
        spark.get(getNavPath(), this::drawCard, JsonTransformer.getInstance());
        spark.post(getNavPath(), this::reset);
        spark.options(getNavPath(), this::allowCors);
    }

    public NavController(NavDeckSpecification spec, NewsSources listeners) {
        this.deck = new NavDeck(spec);
        this.listeners = listeners;
        this.listeners.addSource(this);
        this.deckState = new UnlockedDeckState();
    }

    public String getLockPath() {
        return getNavPath() + LOCK;
    }

    public String getStatusPath() {
        return getNavPath() + STATUS;
    }

    protected String allowCors(Request request, Response response) {
        //apparently needed for cross site request for chrome. AfterAll takes care of headers
        //http://stackoverflow.com/questions/33297190/response-for-preflight-has-invalid-http-status-code-404-angular-js
        return "OK";
    }

    String reset(Request req, Response res) {
        String reply = this.deckState.shuffle(this, res);
        informListeners();
        return reply;
    }

    void informListeners() {
        this.listeners.informListeners(this.status());
    }

    public NavDeckStatus status() {
        int cardCount = this.deck.size();
        int discardsCount = this.deck.discards().size();
        boolean isLocked = this.deckState.isLocked();
        NavDeckStatus status = new NavDeckStatus(this.getNavPath(), cardCount, discardsCount, isLocked);
        return status;
    }

    public String getNavPath() {
        return getSpaceSectorPath() + NAV;
    }

    public abstract String getSpaceSectorPath();

    NavCard drawCard(Request req, Response res) {
        NavCard reply = this.deckState.drawCard(this, res);
        informListeners();
        return reply;
    }

    String lock(Request req, Response res) {
        String reply = this.deckState.lock(this, res);
        informListeners();
        return reply;
    }

    String unlock(Request req, Response res) {
        String reply = this.deckState.unlock(this, res);
        informListeners();
        return reply;
    }

    NavDeckStatus status(Request req, Response res) {
        return status();
    }

}
