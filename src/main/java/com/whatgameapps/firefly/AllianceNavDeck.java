package com.whatgameapps.firefly;

import com.google.common.collect.ImmutableList;
import com.whatgameapps.firefly.rest.AllianceNavCard;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AllianceNavDeck {

    public final ImmutableList<AllianceNavCard> cards;

    public AllianceNavDeck(AllianceNavDeckSpecification deckSpec) {
//        for(Map.Entry<AllianceNavCard, Integer> cardsSpec: deckSpec.entrySet())
//            for(int i = 0; i < cardsSpec.getValue(); ++i)
//                cards.add(cardsSpec.getKey().clone());

        List<AllianceNavCard> tempCards = new ArrayList<>();

        deckSpec.entrySet().stream().forEach(
                (cardsSpec) ->
                        tempCards.addAll(Collections.nCopies(cardsSpec.getValue(), cardsSpec.getKey()))
        );
        tempCards.addAll(Collections.nCopies(deckSpec.count - tempCards.size(), AllianceNavCard.UNKNOWN));

        Collections.shuffle(tempCards);
        this.cards = ImmutableList.copyOf(tempCards);
    }

    public int size() {
        return cards.size();
    }

    public int countCards(final AllianceNavCard target) {
        return (int) cards.stream().filter(c -> c.equals(target)).count();
    }
}
