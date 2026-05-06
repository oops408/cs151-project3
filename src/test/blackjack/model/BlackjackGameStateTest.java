package blackjack.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BlackjackGameStateTest {
    @Test
    public void newGameStartsWaitingForBet() {
        BlackjackGameState state = new BlackjackGameState("student");

        assertTrue(state.isRoundOver());
        assertEquals("student", state.getHumanPlayer().getName());
        assertEquals(4, state.getParticipants().size());
        assertEquals(0, state.getActiveIndex());
    }

    @Test
    public void startRoundDealsTwoCardsAndPlacesBets() {
        BlackjackGameState state = new BlackjackGameState("student");
        state.startRound(100);

        assertFalse(state.isRoundOver());
        assertEquals(100, state.getHumanPlayer().getCurrentBet());

        for (BlackjackParticipant participant : state.getParticipants()) {
            assertEquals(2, participant.getHand().getCards().size());
        }
    }

    @Test
    public void activeIndexIsKeptInValidRange() {
        BlackjackGameState state = new BlackjackGameState("student");

        state.setActiveIndex(-10);
        assertEquals(0, state.getActiveIndex());

        state.setActiveIndex(99);
        assertEquals(state.getParticipants().size() - 1, state.getActiveIndex());
    }

    @Test
    public void outOfMoneyPlayerCannotStartAnotherRound() {
        BlackjackGameState state = new BlackjackGameState("student");
        state.getHumanPlayer().setBalance(0);
        state.startRound(100);

        assertTrue(state.isRoundOver());
        assertTrue(state.getStatusMessage().contains("out of money"));
    }
}
