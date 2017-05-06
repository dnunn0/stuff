package com.whatgameapps.firefly;

import com.whatgameapps.firefly.rest.AllianceNavCard;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class AllianceNavDeck {

    List<AllianceNavCard> cards = new ArrayList<>();

    public AllianceNavDeck(Map<AllianceNavCard, Integer> deckSpec) {
//        for(Map.Entry<AllianceNavCard, Integer> cardsSpec: deckSpec.entrySet())
//            for(int i = 0; i < cardsSpec.getValue(); ++i)
//                cards.add(cardsSpec.getKey().clone());

        deckSpec.entrySet().stream().forEach(
                (cardsSpec) ->
                        cards.addAll(Collections.nCopies(cardsSpec.getValue(), cardsSpec.getKey()))
        );
    }

    public int size() {
        return cards.size();
    }

    public int countCards(final AllianceNavCard target) {
        return (int) cards.stream().filter(c -> c.equals(target)).count();
    }
}
