package com.whatgameapps.firefly.controller;

import com.whatgameapps.firefly.AllianceNavDeck;
import com.whatgameapps.firefly.AllianceNavDeckSpecification;
import com.whatgameapps.firefly.rest.AllianceNavCard;
import org.eclipse.jetty.http.HttpStatus;
import spark.Request;
import spark.Response;
import spark.Service;

import java.util.Optional;

public class AllianceSectorNavController {
    public static final String PREFIX = "Returning - ";

    public static final String PATH = "/allianceSectorNav";
    private final AllianceNavDeck deck;

    public AllianceSectorNavController(Service spark) {
        this();
        spark.delete(PATH, this::resetDeck);
        spark.get(PATH, this::drawCard, JsonTransformer.getInstance());
    }

    public AllianceSectorNavController() {
        this.deck = new AllianceNavDeck(AllianceNavDeckSpecification.BASIC);
    }

    public String resetDeck(Request request, Response response) {
        this.deck.shuffle();
        return "OK";
    }

    AllianceNavCard drawCard(Request req, Response res) {
        Optional<AllianceNavCard> card = this.deck.take();
        System.out.println(PREFIX + card);
        if (card.isPresent())
            return card.get();
        res.status(HttpStatus.REQUESTED_RANGE_NOT_SATISFIABLE_416);
        return null;
    }
}
