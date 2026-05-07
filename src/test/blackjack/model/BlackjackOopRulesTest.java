package blackjack.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BlackjackOopRulesTest {
    @Test
    void computerPlayerExtendsPlayerAndImplementsAutoPlayer() {
        ComputerPlayer player = new ComputerPlayer("Computer", 1000, 16);

        assertTrue(player instanceof Player);
        assertTrue(player instanceof AutoPlayer);
    }

    @Test
    void dealerExtendsPlayerAndImplementsAutoPlayer() {
        Dealer dealer = new Dealer("Dealer", 1000);

        assertTrue(dealer instanceof Player);
        assertTrue(dealer instanceof AutoPlayer);
    }

    @Test
    void computerPlayersCanUseDifferentStandThresholds() {
        ComputerPlayer cautious = new ComputerPlayer("Cautious", 1000, 16);
        ComputerPlayer bold = new ComputerPlayer("Bold", 1000, 17);

        cautious.getHand().addCard(new Card("10", "H"));
        cautious.getHand().addCard(new Card("6", "S"));

        bold.getHand().addCard(new Card("10", "D"));
        bold.getHand().addCard(new Card("6", "C"));

        assertFalse(cautious.wantsToHit());
        assertTrue(bold.wantsToHit());
    }

    @Test
    void dealerHitsOnSoftSeventeen() {
        Dealer dealer = new Dealer("Dealer", 1000);

        dealer.getHand().addCard(new Card("A", "H"));
        dealer.getHand().addCard(new Card("6", "S"));

        assertEquals(17, dealer.getHand().getBestValue());
        assertTrue(dealer.getHand().isSoft());
        assertTrue(dealer.wantsToHit());
    }

    @Test
    void playerWinLoseAndPushUpdateMoneyAndBet() {
        Player player = new Player("Human", 1000);

        player.placeBet(100);
        assertEquals(900, player.getMoney());
        assertEquals(100, player.getBet());

        player.pushBet();
        assertEquals(1000, player.getMoney());
        assertEquals(0, player.getBet());

        player.placeBet(100);
        player.winBet();
        assertEquals(1100, player.getMoney());
        assertEquals(0, player.getBet());

        player.placeBet(100);
        player.loseBet();
        assertEquals(1000, player.getMoney());
        assertEquals(0, player.getBet());
    }
}
