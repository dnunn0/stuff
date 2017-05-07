package com.whatgameapps.firefly.controller;

import com.whatgameapps.firefly.rest.AllianceNavCard;
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
    public AllianceNavCard drawCard(AllianceSectorNavController controller, Response res) {
        Optional<AllianceNavCard> card = controller.deck.take();
        System.out.println(AllianceSectorNavController.PREFIX + card);

        int status = AllianceSectorNavController.NOT_FOUND_ERROR;
        AllianceNavCard reply = null;

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
