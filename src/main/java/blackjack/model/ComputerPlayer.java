package blackjack.model;

public class ComputerPlayer extends Player implements AutoPlayer {
    
    private int standNumber;

    public ComputerPlayer(String name, int startingMoney, int standNumber) {
        super(name, startingMoney);
        this.standNumber = standNumber;
    }

    // This function will help determine wheather our computer player wants to keep hitting ot not
    public boolean wantsToHit() {
        return getHand().getBestValue() < standNumber;
        
    }



}

