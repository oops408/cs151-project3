package blackjack.model;

public class ComputerPlayer extends BlackjackParticipant {
    private final int hitThreshold;

    public ComputerPlayer(String name, int balance, int hitThreshold) {
        super(name, balance);
        this.hitThreshold = hitThreshold;
    }

    @Override
    public boolean shouldHit() {
        return getHand().getValue() < hitThreshold;
    }
}
