
//Since the dealer is a player, they extend from the player class, however they implement from the auto player since they are a computer
// However, since they are the dealer, they have special functions thus are unique from a regular computer player
public class Dealer extends Player implements AutoPlayer {

  public Dealer(String name, int startingMoney) {
        super(name, startingMoney);
  }

  // Since a dealer must only hit when their hand is less than 17, this function causes them to keep hitting until that reach that point
  public boolean wantsToHit() {
        if (getHand().getBestValue() < 17) {
            return true;
        }

        if (getHand().getBestValue() == 17 && getHand().isSoft()) {
            return true;
        }

        return false;
  }


}
