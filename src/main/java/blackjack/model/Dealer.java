package blackjack.model;

public class Dealer extends BlackjackParticipant {
    public Dealer() {
        super("Dealer", 0);
    }

    @Override
    public boolean shouldHit() {
        return getHand().isSoft17OrLess();
    }
}
