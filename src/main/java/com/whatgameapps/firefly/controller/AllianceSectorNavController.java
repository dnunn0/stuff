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
    final AllianceNavDeck deck;

    public AllianceSectorNavController(Service spark, AllianceNavDeckSpecification spec) {
        this(spec);
        spark.post(PATH, this::resetDeck);
        spark.get(PATH, this::drawCard, JsonTransformer.getInstance());
    }

    public AllianceSectorNavController(AllianceNavDeckSpecification spec) {
        this.deck = new AllianceNavDeck(spec);
    }

    public String resetDeck(Request request, Response response) {
        this.deck.shuffle();
        return "OK";
    }

    AllianceNavCard drawCard(Request req, Response res) {
        Optional<AllianceNavCard> card = this.deck.take();
        System.out.println(PREFIX + card);

        int status = HttpStatus.REQUESTED_RANGE_NOT_SATISFIABLE_416;
        AllianceNavCard reply = null;

        if (card.isPresent()) {
            reply = card.get();
            status = HttpStatus.OK_200;
        }
        res.status(status);
        return reply;
    }
}
