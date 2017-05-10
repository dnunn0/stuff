package com.whatgameapps.firefly.controller;

import com.whatgameapps.firefly.rest.NavCard;
import org.eclipse.jetty.http.HttpStatus;
import spark.Response;

import java.util.Optional;

public class UnlockedDeckState implements DeckState {

    @Override
    public String shuffle(AllianceSectorNavController controller, Response response) {
        controller.deck.shuffle();
        response.status(HttpStatus.OK_200);
        return "OK";
    }

    @Override
    public NavCard drawCard(AllianceSectorNavController controller, Response res) {
        Optional<NavCard> card = controller.deck.take();

        int status = AllianceSectorNavController.NOT_FOUND_ERROR;
        NavCard reply = null;

        if (card.isPresent()) {
            reply = card.get();
            status = HttpStatus.OK_200;
        }
        res.status(status);
        return reply;
    }

    @Override
    public String lock(AllianceSectorNavController controller, Response res) {
        controller.deckState = new LockedDeckState();
        return controller.deckState.lock(controller, res);
    }

    @Override
    public String unlock(AllianceSectorNavController controller, Response res) {
        res.status(HttpStatus.OK_200);
        return "OK";
    }
}
