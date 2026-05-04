package blackjack.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Deck {
    private final List<Card> cards = new ArrayList<>();

    public Deck() {
        reset();
    }

    public void reset() {
        cards.clear();
        for (Suit suit : Suit.values()) {
            for (Rank rank : Rank.values()) {
                cards.add(new Card(rank, suit));
            }
        }
        Collections.shuffle(cards);
    }

    public Card drawCard() {
        if (cards.isEmpty()) {
            reset();
        }
        return cards.remove(0);
    }

    public List<Card> getRemainingCards() {
        return Collections.unmodifiableList(cards);
    }

    public void replaceRemainingCards(List<Card> newCards) {
        cards.clear();
        cards.addAll(newCards);
    }
}
