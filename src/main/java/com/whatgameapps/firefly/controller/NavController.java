package com.whatgameapps.firefly.controller;

import com.whatgameapps.firefly.CardDeck;
import com.whatgameapps.firefly.rest.NavCard;
import com.whatgameapps.firefly.rest.NavDeckStatus;
import org.eclipse.jetty.http.HttpStatus;
import spark.Request;
import spark.Response;
import spark.Service;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Map;

public class NavController {
    public static final String ID_HEADER = "id";
    public static final int NOT_FOUND_ERROR = HttpStatus.NOT_FOUND_404;
    public static final int LOCK_ERROR = HttpStatus.CONFLICT_409;
    public static final String NAV = "/nav";
    public static final String LOCK = "/lock";
    public static final String STATUS = "/status";
    public static final String SPEC = "/spec";

    public static String ALLIANCE_SPACE = "/alliance";
    public static String RIM_SPACE = "/rim";
    public static String BORDER_SPACE = "/border";
    final String spaceSectorPath;
    final CardDeck deck;
    final StatusBroadcaster listeners;
    volatile DeckState deckState;

    public NavController(Service spark, String spaceSectorPath, CardDeck deck, StatusBroadcaster listeners) {
        this(spaceSectorPath, deck, listeners);
        spark.post(getLockPath(), this::lock);
        spark.delete(getLockPath(), this::unlock);
        spark.options(getLockPath(), this::allowCors);
        spark.get(getStatusPath(), this::status, JsonRenderer.getInstance());
        spark.options(getStatusPath(), this::allowCors);
        spark.get(getNavPath(), this::drawCard, JsonRenderer.getInstance());
        spark.post(getNavPath(), this::reset);
        spark.options(getNavPath(), this::allowCors);
        spark.get(getSpecPath(), this::describe, JsonRenderer.getInstance());
        spark.options(getSpecPath(), this::allowCors);
    }

    public NavController(String spaceSectorPath, CardDeck deck, StatusBroadcaster listeners) {
        this.spaceSectorPath = spaceSectorPath;
        this.deck = deck;
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

    public String getSpecPath() {
        return getNavPath() + SPEC;
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
        this.listeners.informSubscribers(this.status());
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

    public String getSpaceSectorPath() {
        return this.spaceSectorPath;
    }

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

    ArrayList<Map.Entry<String, Integer>> describe(Request req, Response res) {
        //TODO consider dropping guava altogether. this is ridculous.
        final ArrayList<Map.Entry<String, Integer>> reply = new ArrayList();
        this.deck.spec.entrySet().forEach(e -> reply.add(new AbstractMap.SimpleEntry<String, Integer>(e.getElement(), e.getCount())));
        return reply;
    }

    NavDeckStatus status(Request req, Response res) {
        return status();
    }

}
