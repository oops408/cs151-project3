package blackjack.model;

public class HumanPlayer extends BlackjackParticipant {
    public HumanPlayer(String name, int balance) {
        super(name, balance);
    }

    @Override
    public boolean shouldHit() {
        return false;
    }
}
