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


}
