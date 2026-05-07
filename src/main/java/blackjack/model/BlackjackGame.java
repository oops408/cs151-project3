public class BlackjackGame {

private Deck deck;
    private HumanPlayer humanPlayer;
    private ComputerPlayer computerOne;
    private ComputerPlayer computerTwo;
    private Dealer dealer;
    private int currentTurn;
    private boolean roundGoing;
    private boolean dealerCardHidden;
    private String message;

    public BlackjackGame() {
        deck = new Deck();
        humanPlayer = new HumanPlayer("Human Player", 1000);
        computerOne = new ComputerPlayer("Computer Player 1", 1000, 16);
        computerTwo = new ComputerPlayer("Computer Player 2", 1000, 17);
        dealer = new Dealer("Dealer", 1000);
        currentTurn = 4;
        roundGoing = false;
        dealerCardHidden = false;
        message = "Choose Start New Game or Load Game.";
    }

    // This is the function which will handle starting a new round, it wull use if statements to check for certain conditions
    public void startNewRound(int humanBet) {
        if (roundGoing) {
            message = "The round is already going.";
            return;
        }

        if (humanPlayer.getMoney() <= 0) {
            message = "You are out of money. Game over.";
            return;
        }

        if (humanBet <= 0 || humanBet > humanPlayer.getMoney()) {
            message = "Bet must be more than 0 and not more than your money.";
            return;
        }

        deck = new Deck();
        humanPlayer.clearHand();
        computerOne.clearHand();
        computerTwo.clearHand();
        dealer.clearHand();

        humanPlayer.placeBet(humanBet);
        computerOne.placeBet(makeComputerBet(computerOne));
        computerTwo.placeBet(makeComputerBet(computerTwo));
        dealer.placeBet(0);

        dealStartingCards();

        currentTurn = 0;
        roundGoing = true;
        dealerCardHidden = true;
        message = "New round started. It is your turn.";

        checkForStartingBlackjacks();
    }


    
    // This function will handle the computer player's bet
    private int makeComputerBet(Player player) {
        if (player.getMoney() >= 50) {
            return 50;
        }
        return player.getMoney();
    }

    // This function deals the starting cards for the human player, the two computer players, and the bot
    private void dealStartingCards() {
        for (int i = 0; i < 2; i++) {
            humanPlayer.getHand().addCard(deck.drawCard());
            computerOne.getHand().addCard(deck.drawCard());
            computerTwo.getHand().addCard(deck.drawCard());
            dealer.getHand().addCard(deck.drawCard());
        }
    }

    // I made this function to check whether we will have a blackjack or not
    private void checkForStartingBlackjacks() {
        if (humanPlayer.getHand().isBlackjack()) {
            humanStand();
        }
    }

    // This function will handle the human hits, it will use an if else statement to ensure we can keep hitting based on whether we busted or not
    public void humanHit() {
        if (!roundGoing || currentTurn != 0) {
            return;
        }

        humanPlayer.getHand().addCard(deck.drawCard());

        if (humanPlayer.getHand().isBust()) {
            message = "Human Player busts.";
            nextTurn();
        } else {
            message = "Human Player hits.";
        }
    }

    // This will handle whether the player stands, meaning they no longer hit and will keep their current hand to compare with the dealer
    public void humanStand() {
        if (!roundGoing || currentTurn != 0) {
            return;
        }

        message = "Human Player stands.";
        nextTurn();
    }

    // I made this function to help run each computer players turn, including the dealer.
    public void playOneComputerTurn() {
        if (!roundGoing) {
            return;
        }

        if (currentTurn == 1) {
            playComputer(computerOne);
        } else if (currentTurn == 2) {
            playComputer(computerTwo);
        } else if (currentTurn == 3) {
            playDealer();
        }
    }

    // I made this function to help run the player's turn
    private void playComputer(ComputerPlayer player) {
        if (player.getMoney() <= 0 && player.getBet() <= 0) {
            message = player.getName() + " has no money left and skips.";
            nextTurn();
            return;
        }

        if (player.wantsToHit()) {
            player.getHand().addCard(deck.drawCard());
            message = player.getName() + " hits.";

            if (player.getHand().isBust()) {
                message = player.getName() + " busts.";
                nextTurn();
            }
        } else {
            message = player.getName() + " stands.";
            nextTurn();
        }
    }

    // I made this function to help run the dealer's turn
    private void playDealer() {
        dealerCardHidden = false;

        if (dealer.wantsToHit()) {
            dealer.getHand().addCard(deck.drawCard());
            message = "Dealer hits.";

            if (dealer.getHand().isBust()) {
                message = "Dealer busts.";
                finishRound();
            }
        } else {
            message = "Dealer stands.";
            finishRound();
        }
    }

    // I created this function to handle moving onto the next turn
    private void nextTurn() {
        currentTurn++;

        if (currentTurn >= 3) {
            currentTurn = 3;
            dealerCardHidden = false;
        }
    }

    // This function will handle whenever a round has finished
    private void finishRound() {
        roundGoing = false;
        currentTurn = 4;
        dealerCardHidden = false;

        payPlayer(humanPlayer);
        payPlayer(computerOne);
        payPlayer(computerTwo);

        message = message + " Round is over. Start a new round when ready.";
    }

    // This function will handle/deal with the payments, so it determines whether the player pushes, busts, or gets paid
    private void payPlayer(Player player) {
        int dealerValue = dealer.getHand().getBestValue();
        int playerValue = player.getHand().getBestValue();

        if (player.getBet() == 0) {
            return;
        }

        if (player.getHand().isBust()) {
            player.loseBet();
            return;
        }

        if (dealer.getHand().isBust()) {
            player.winBet();
            return;
        }

        if (playerValue > dealerValue) {
            player.winBet();
        } else if (playerValue == dealerValue) {
            player.pushBet();
        } else {
            player.loseBet();
        }
    }

    // This function will create a string the user can save to reload the game at a future date/time
    public String makeSaveString() {
        String text = "";
        text += currentTurn + ";";
        text += roundGoing + ";";
        text += dealerCardHidden + ";";
        text += humanPlayer.saveText() + ";";
        text += computerOne.saveText() + ";";
        text += computerTwo.saveText() + ";";
        text += dealer.saveText() + ";";
        text += deck.saveText();
        return text;
    }

    // The user can then upload sed string into this function and they can return where they once left off
    public boolean loadFromString(String text) {
        if (text == null || text.trim().length() == 0) {
            message = "Paste a save string first.";
            return false;
        }

        try {
            String[] parts = text.trim().split(";", -1);

            if (parts.length != 8) {
                message = "Could not load save string. It should have 8 parts.";
                return false;
            }

            int loadedTurn = Integer.parseInt(parts[0]);
            boolean loadedRoundGoing = Boolean.parseBoolean(parts[1]);
            boolean loadedDealerHidden = Boolean.parseBoolean(parts[2]);

            if (loadedTurn < 0 || loadedTurn > 4) {
                message = "Could not load save string. The turn number is not valid.";
                return false;
            }

            currentTurn = loadedTurn;
            roundGoing = loadedRoundGoing;
            dealerCardHidden = loadedDealerHidden;

            humanPlayer.loadText(parts[3]);
            computerOne.loadText(parts[4]);
            computerTwo.loadText(parts[5]);
            dealer.loadText(parts[6]);
            deck.loadText(parts[7]);

            if (!roundGoing) {
                currentTurn = 4;
                dealerCardHidden = false;
            }

            message = "Game loaded from save string.";
            return true;
        } catch (Exception ex) {
            message = "Could not load save string. Make sure it was copied exactly.";
            return false;
        }
    }

    public boolean isHumanTurn() {
        return roundGoing && currentTurn == 0;
    }

    public boolean isRoundGoing() {
        return roundGoing;
    }

    public boolean shouldHideDealerCard() {
        return dealerCardHidden && roundGoing;
    }

    // Here are my getter methods
    public String getTurnName() {
        if (!roundGoing) {
            return "No active round";
        }
        if (currentTurn == 0) {
            return "Human Player";
        }
        if (currentTurn == 1) {
            return "Computer Player 1";
        }
        if (currentTurn == 2) {
            return "Computer Player 2";
        }
        if (currentTurn == 3) {
            return "Dealer";
        }
        return "Round over";
    }

    public int getCurrentTurn() {
        return currentTurn;
    }

    public String getMessage() {
        return message;
    }

    public HumanPlayer getHumanPlayer() {
        return humanPlayer;
    }

    public ComputerPlayer getComputerOne() {
        return computerOne;
    }

    public ComputerPlayer getComputerTwo() {
        return computerTwo;
    }

    public Dealer getDealer() {
        return dealer;
    }
}