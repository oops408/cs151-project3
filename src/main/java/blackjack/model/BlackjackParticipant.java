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
        this.balance = Math.max(0, balance);
    }

    public int getCurrentBet() {
        return currentBet;
    }

    public void setCurrentBet(int currentBet) {
        this.currentBet = Math.max(0, currentBet);
    }

    public boolean isStanding() {
        return standing;
    }

    public void setStanding(boolean standing) {
        this.standing = standing;
    }

    public void stand() {
        this.standing = true;
    }

    // A new round clears the previous hand and prepares the player to act again.
    public void resetTurn() {
        this.standing = false;
        this.currentBet = 0;
        this.hand.clear();
    }

    // Keep the bet valid: no negative bets, no bet larger than the current balance.
    public void placeBet(int amount) {
        if (balance <= 0) {
            currentBet = 0;
            return;
        }
        currentBet = Math.max(1, Math.min(amount, balance));
    }

    public void winBet() {
        balance += currentBet;
    }

    public void loseBet() {
        balance = Math.max(0, balance - currentBet);
    }

    public void push() {
        // A tie keeps the player's money the same.
    }

    public abstract boolean shouldHit();
}
