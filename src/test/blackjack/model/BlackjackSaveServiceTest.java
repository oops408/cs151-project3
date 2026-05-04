package blackjack.model;

import persistence.BlackjackSaveService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BlackjackSaveServiceTest {
    @Test
    public void saveAndLoadRestoresImportantState() {
        BlackjackGameState state = new BlackjackGameState("student");
        state.startRound(100);
        state.hitActivePlayer();
        state.setStatusMessage("Testing save and load");

        BlackjackSaveService service = new BlackjackSaveService();
        String saveText = service.save(state);
        BlackjackGameState loaded = service.load(saveText, "student");

        assertEquals(state.getActiveIndex(), loaded.getActiveIndex());
        assertEquals(state.isRoundOver(), loaded.isRoundOver());
        assertEquals(state.getStatusMessage(), loaded.getStatusMessage());
        assertEquals(state.getHumanPlayer().getBalance(), loaded.getHumanPlayer().getBalance());
        assertEquals(state.getHumanPlayer().getCurrentBet(), loaded.getHumanPlayer().getCurrentBet());
        assertEquals(state.getHumanPlayer().isStanding(), loaded.getHumanPlayer().isStanding());
        assertEquals(state.getHumanPlayer().getHand().getCards().size(), loaded.getHumanPlayer().getHand().getCards().size());
        assertEquals(state.getDeck().getRemainingCards().size(), loaded.getDeck().getRemainingCards().size());
        assertFalse(saveText.contains("HEARTS"));
    }

    @Test
    public void participantCannotLoseBelowZero() {
        HumanPlayer player = new HumanPlayer("student", 25);
        player.placeBet(100);
        player.loseBet();
        assertEquals(0, player.getBalance());
        assertTrue(player.getCurrentBet() <= 25);
    }
}
