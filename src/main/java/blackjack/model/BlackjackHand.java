package blackjack.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BlackjackHand {
    private final List<Card> cards;
    private final HandValueStrategy handValueStrategy;

    public BlackjackHand() {
        this.cards = new ArrayList<>();
        this.handValueStrategy = new StandardHandValueStrategy();
    }

    public void addCard(Card card) {
        cards.add(card);
    }

    public void clear() {
        cards.clear();
    }

    public List<Card> getCards() {
        return Collections.unmodifiableList(cards);
    }

    public int getValue() {
        return handValueStrategy.calculate(this);
    }

    public boolean isBust() {
        return getValue() > 21;
    }

    public boolean isSoft17OrLess() {
        return getValue() <= 17;
    }
}
