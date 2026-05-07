package blackjack.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BlackjackGameTest {
    @Test
    public void newGameStartsWithNoActiveRound() {
        BlackjackGame game = new BlackjackGame();

        assertFalse(game.isRoundGoing());
        assertFalse(game.isHumanTurn());
        assertEquals("No active round", game.getTurnName());
        assertNotNull(game.getMessage());
    }

    @Test
    public void startNewRoundWithValidBetStartsRound() {
        BlackjackGame game = new BlackjackGame();

        game.startNewRound(50);

        assertTrue(game.isRoundGoing());
        assertTrue(game.isHumanTurn());
        assertEquals(0, game.getCurrentTurn());
        assertEquals("Human Player", game.getTurnName());
        assertTrue(game.shouldHideDealerCard());
    }

    @Test
    public void startNewRoundRejectsInvalidBet() {
        BlackjackGame game = new BlackjackGame();

        game.startNewRound(0);

        assertFalse(game.isRoundGoing());
        assertTrue(game.getMessage().contains("Bet must be more than 0"));
    }

    @Test
    public void saveStringCanBeCreated() {
        BlackjackGame game = new BlackjackGame();

        String saveText = game.makeSaveString();

        assertNotNull(saveText);
        assertFalse(saveText.isBlank());
    }
}
