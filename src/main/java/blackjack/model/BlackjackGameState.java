package blackjack.model;

import java.util.ArrayList;
import java.util.List;

public class BlackjackGameState {
    private final HumanPlayer humanPlayer;
    private final List<BlackjackParticipant> participants;
    private final Dealer dealer;
    private final Deck deck;
    private int activeIndex;
    private String statusMessage;
    private boolean roundOver;

    public BlackjackGameState(String username) {
        this.humanPlayer = new HumanPlayer(username, 1000);
        this.dealer = new Dealer();
        this.deck = new Deck();
        this.participants = new ArrayList<>();

        // Turn order required by the assignment: human, bot 1, bot 2, dealer.
        participants.add(humanPlayer);
        participants.add(new ComputerPlayer("Bot 1", 1000, 16));
        participants.add(new ComputerPlayer("Bot 2", 1000, 17));
        participants.add(dealer);

        this.activeIndex = 0;
        this.statusMessage = "Choose a bet and start a new round.";
        this.roundOver = true;
    }

    public HumanPlayer getHumanPlayer() {
        return humanPlayer;
    }

    public List<BlackjackParticipant> getParticipants() {
        return participants;
    }

    public Dealer getDealer() {
        return dealer;
    }

    public Deck getDeck() {
        return deck;
    }

    public int getActiveIndex() {
        return activeIndex;
    }

    public void setActiveIndex(int activeIndex) {
        if (activeIndex < 0) {
            this.activeIndex = 0;
        } else if (activeIndex >= participants.size()) {
            this.activeIndex = participants.size() - 1;
        } else {
            this.activeIndex = activeIndex;
        }
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    public boolean isRoundOver() {
        return roundOver;
    }

    public void setRoundOver(boolean roundOver) {
        this.roundOver = roundOver;
    }

    public boolean isHumanOutOfMoney() {
        return humanPlayer.getBalance() <= 0;
    }

    public void startRound(int humanBet) {
        if (isHumanOutOfMoney()) {
            roundOver = true;
            statusMessage = humanPlayer.getName() + " is out of money. Game over.";
            return;
        }

        deck.reset();
        roundOver = false;
        activeIndex = 0;

        // Clear the old hands and place a new bet for every non-dealer player.
        for (BlackjackParticipant participant : participants) {
            participant.resetTurn();
            if (!(participant instanceof Dealer)) {
                int bet = participant == humanPlayer ? humanBet : 50;
                participant.placeBet(bet);
            }
        }

        // Deal two cards to each participant.
        for (int i = 0; i < 2; i++) {
            for (BlackjackParticipant participant : participants) {
                participant.getHand().addCard(deck.drawCard());
            }
        }

        statusMessage = "Cards dealt. " + humanPlayer.getName() + " acts first.";
    }

    public BlackjackParticipant getActiveParticipant() {
        if (activeIndex >= participants.size()) {
            return participants.get(participants.size() - 1);
        }
        return participants.get(activeIndex);
    }

    public void hitActivePlayer() {
        if (roundOver) {
            statusMessage = "Round is over. Start the next round when ready.";
            return;
        }

        BlackjackParticipant player = getActiveParticipant();
        player.getHand().addCard(deck.drawCard());

        if (player.getHand().isBust()) {
            player.stand();
            statusMessage = player.getName() + " busts!";
            moveToNextTurn();
        } else {
            statusMessage = player.getName() + " hits.";
        }
    }

    public void standActivePlayer() {
        if (roundOver) {
            statusMessage = "Round is over. Start the next round when ready.";
            return;
        }

        BlackjackParticipant player = getActiveParticipant();
        player.stand();
        statusMessage = player.getName() + " stands.";
        moveToNextTurn();
    }

    public void moveToNextTurn() {
        activeIndex++;
        if (activeIndex >= participants.size()) {
            finishRound();
        }
    }

    public void autoPlayUntilHumanNeeded() {
        while (!roundOver && !(getActiveParticipant() instanceof HumanPlayer)) {
            BlackjackParticipant participant = getActiveParticipant();
            if (participant.shouldHit()) {
                hitActivePlayer();
            } else {
                standActivePlayer();
            }
        }
    }

    public void finishRound() {
        roundOver = true;
        activeIndex = participants.size() - 1;

        int dealerValue = dealer.getHand().getValue();
        boolean dealerBust = dealer.getHand().isBust();

        // Compare each player to the dealer and update money using the current bet.
        for (BlackjackParticipant participant : participants) {
            if (participant instanceof Dealer) {
                continue;
            }

            int value = participant.getHand().getValue();
            if (participant.getHand().isBust()) {
                participant.loseBet();
            } else if (dealerBust || value > dealerValue) {
                participant.winBet();
            } else if (value < dealerValue) {
                participant.loseBet();
            } else {
                participant.push();
            }
        }

        if (humanPlayer.getBalance() <= 0) {
            statusMessage = "Round complete. " + humanPlayer.getName() + " is out of money.";
        } else if (dealerBust) {
            statusMessage = "Dealer busts. Winners are paid.";
        } else {
            statusMessage = "Round complete. Start the next round when ready.";
        }
    }
}
