package com.whatgameapps.firefly.controller;

import com.whatgameapps.firefly.rest.NavCard;
import org.eclipse.jetty.http.HttpStatus;
import spark.Response;

public class LockedDeckState implements DeckState {

    @Override
    public String shuffle(NavController controller, Response res) {
        res.status(NavController.LOCK_ERROR);
        return "";
    }

    @Override
    public NavCard drawCard(NavController controller, Response res) {
        res.status(NavController.LOCK_ERROR);
        return null;
    }

    @Override
    public String lock(NavController controller, Response res) {
        res.status(HttpStatus.OK_200);
        return "OK";
    }

    @Override
    public String unlock(NavController controller, Response res) {
        controller.deckState = new UnlockedDeckState();
        return controller.deckState.unlock(controller, res);
    }

    public boolean isLocked() {
        return true;
    }
}
