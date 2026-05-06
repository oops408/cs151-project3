package blackjack.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BlackjackHandTest {
    @Test
    void aceAdjustsToPreventBust() {
        BlackjackHand hand = new BlackjackHand();
        hand.addCard(new Card(Rank.ACE, Suit.SPADES));
        hand.addCard(new Card(Rank.NINE, Suit.HEARTS));
        hand.addCard(new Card(Rank.FIVE, Suit.CLUBS));
        assertEquals(15, hand.getValue());
    }
}
