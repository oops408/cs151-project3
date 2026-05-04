package blackjack.model;

public abstract class BlackjackParticipant {
    private final String name;
    private final BlackjackHand hand;
    private int balance;
    private int currentBet;
    private boolean standing;

    protected BlackjackParticipant(String name, int balance) {
        this.name = name;
        this.balance = balance;
        this.currentBet = 0;
        this.hand = new BlackjackHand();
        this.standing = false;
    }

    public String getName() {
        return name;
    }

    public BlackjackHand getHand() {
        return hand;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public int getCurrentBet() {
        return currentBet;
    }

    public void setCurrentBet(int currentBet) {
        this.currentBet = currentBet;
    }

    public boolean isStanding() {
        return standing;
    }

    public void stand() {
        this.standing = true;
    }

    public void resetTurn() {
        this.standing = false;
        this.currentBet = 0;
        this.hand.clear();
    }

    public void placeBet(int amount) {
        currentBet = Math.max(1, Math.min(amount, balance));
    }

    public void winBet() {
        balance += currentBet;
    }

    public void loseBet() {
        balance -= currentBet;
    }

    public void push() {
        // no change for ties
    }

    public abstract boolean shouldHit();
}
