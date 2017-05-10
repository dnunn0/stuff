package com.whatgameapps.firefly.controller;

import com.whatgameapps.firefly.rest.NavCard;
import spark.Response;

public interface DeckState {

    String shuffle(NavController controller, Response res);

    NavCard drawCard(NavController controller, Response res);

    String lock(NavController controller, Response res);

    String unlock(NavController controller, Response res);

    default boolean isLocked() {
        return false;
    }

}
