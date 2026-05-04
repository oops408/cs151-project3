package blackjack.model;

import persistence.BlackjackSaveService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class BlackjackSaveServiceTest {
    @Test
    public void saveAndLoadRestoresImportantState() {
        BlackjackGameState state = new BlackjackGameState("student");
        state.startRound(100);
        state.hitActivePlayer();

        BlackjackSaveService service = new BlackjackSaveService();
        String saveText = service.save(state);
        BlackjackGameState loaded = service.load(saveText, "student");

        assertEquals(state.getActiveIndex(), loaded.getActiveIndex());
        assertEquals(state.getHumanPlayer().getBalance(), loaded.getHumanPlayer().getBalance());
        assertEquals(state.getHumanPlayer().getCurrentBet(), loaded.getHumanPlayer().getCurrentBet());
        assertEquals(state.getHumanPlayer().getHand().getCards().size(), loaded.getHumanPlayer().getHand().getCards().size());
        assertEquals(state.getDeck().getRemainingCards().size(), loaded.getDeck().getRemainingCards().size());
        assertFalse(saveText.contains("HEARTS"));
    }
}
