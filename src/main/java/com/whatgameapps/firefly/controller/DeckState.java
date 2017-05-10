package com.whatgameapps.firefly.controller;

import com.whatgameapps.firefly.rest.NavCard;
import spark.Response;

public interface DeckState {

    String shuffle(AllianceSectorNavController controller, Response res);

    NavCard drawCard(AllianceSectorNavController controller, Response res);

    String lock(AllianceSectorNavController controller, Response res);

    String unlock(AllianceSectorNavController controller, Response res);

    default boolean isLocked() {
        return false;
    }

}
