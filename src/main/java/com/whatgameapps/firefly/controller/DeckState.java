package com.whatgameapps.firefly.controller;

import com.whatgameapps.firefly.rest.AllianceNavCard;
import spark.Response;

public interface DeckState {

    String shuffle(AllianceSectorNavController controller, Response res);

    AllianceNavCard drawCard(AllianceSectorNavController controller, Response res);

    String lock(AllianceSectorNavController controller, Response res);

    String unlock(AllianceSectorNavController controller, Response res);
}