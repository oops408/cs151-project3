package blackjack.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DeckTest {
    @Test
    public void deckCanBeCreated() {
        Deck deck = new Deck();

        assertNotNull(deck);
    }

    @Test
    public void deckCanDrawCard() {
        Deck deck = new Deck();

        Card card = deck.drawCard();

        assertNotNull(card);
    }
}
