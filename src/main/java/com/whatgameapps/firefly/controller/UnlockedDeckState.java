package com.whatgameapps.firefly.controller;

import com.whatgameapps.firefly.rest.NavCard;
import org.eclipse.jetty.http.HttpStatus;
import spark.Response;

import java.util.Optional;

public class UnlockedDeckState implements DeckState {

    @Override
    public String shuffle(NavController controller, Response response) {
        controller.deck.shuffle();
        response.status(HttpStatus.OK_200);
        return "OK";
    }

    @Override
    public NavCard drawCard(NavController controller, Response res) {
        Optional<NavCard> card = controller.deck.take();

        int status = NavController.NOT_FOUND_ERROR;
        NavCard reply = null;

        if (card.isPresent()) {
            reply = card.get();
            status = HttpStatus.OK_200;
        }
        res.status(status);
        return reply;
    }

    @Override
    public String lock(NavController controller, Response res) {
        controller.deckState = new LockedDeckState();
        return controller.deckState.lock(controller, res);
    }

    @Override
    public String unlock(NavController controller, Response res) {
        res.status(HttpStatus.OK_200);
        return "OK";
    }
}
