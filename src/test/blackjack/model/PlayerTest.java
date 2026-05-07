package blackjack.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PlayerTest {
    @Test
    public void humanPlayerCanBeCreated() {
        HumanPlayer player = new HumanPlayer("Student", 1000);

        assertEquals("Student", player.getName());
        assertEquals(1000, player.getMoney());
        assertNotNull(player.getHand());
    }

    @Test
    public void computerPlayerCanBeCreated() {
        ComputerPlayer player = new ComputerPlayer("Computer", 1000, 16);

        assertEquals("Computer", player.getName());
        assertEquals(1000, player.getMoney());
        assertNotNull(player.getHand());
    }

    @Test
    public void dealerCanBeCreated() {
        Dealer dealer = new Dealer("Dealer", 1000);

        assertEquals("Dealer", dealer.getName());
        assertNotNull(dealer.getHand());
    }
}
