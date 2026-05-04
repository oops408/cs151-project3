package blackjack.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DeckTest {
    @Test
    public void drawingCardReducesDeckSize() {
        Deck deck = new Deck();
        int before = deck.getRemainingCards().size();
        deck.drawCard();
        assertEquals(before - 1, deck.getRemainingCards().size());
    }
}
