package com.whatgameapps.firefly.controller;

import com.whatgameapps.firefly.rest.AllianceNavCard;
import org.eclipse.jetty.http.HttpStatus;
import spark.Response;

public class LockedDeckState implements DeckState {

    @Override
    public String shuffle(AllianceSectorNavController controller, Response res) {
        res.status(AllianceSectorNavController.LOCK_ERROR);
        return "";
    }

    @Override
    public AllianceNavCard drawCard(AllianceSectorNavController controller, Response res) {
        res.status(AllianceSectorNavController.LOCK_ERROR);
        return null;
    }

    @Override
    public String lock(final AllianceSectorNavController controller, Response res) {
        res.status(HttpStatus.OK_200);
        return "OK";
    }

    @Override
    public String unlock(AllianceSectorNavController controller, Response res) {
        controller.deckState = new UnlockedDeckState();
        return controller.deckState.unlock(controller, res);
    }
}
