package blackjack.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BlackjackGameTest {
    @Test
    void newGameStartsWithNoActiveRound() {
        BlackjackGame game = new BlackjackGame();

        assertFalse(game.isRoundGoing());
        assertEquals("No active round", game.getTurnName());
    }

    @Test
    void startNewRoundWithValidBetStartsRound() {
        BlackjackGame game = new BlackjackGame();

        game.startNewRound(50);

        assertNotNull(game.getMessage());
        assertTrue(game.getHumanPlayer().getHand().getSize() >= 2);
        assertTrue(game.getComputerOne().getHand().getSize() >= 2);
        assertTrue(game.getComputerTwo().getHand().getSize() >= 2);
        assertTrue(game.getDealer().getHand().getSize() >= 2);
    }

    @Test
    void startNewRoundRejectsInvalidBet() {
        BlackjackGame game = new BlackjackGame();

        game.startNewRound(0);

        assertFalse(game.isRoundGoing());
        assertEquals("Bet must be more than 0 and not more than your money.", game.getMessage());
    }

    @Test
    void saveStringCanBeCreated() {
        BlackjackGame game = new BlackjackGame();

        String saveString = game.makeSaveString();

        assertNotNull(saveString);
        assertFalse(saveString.isBlank());
    }
}
