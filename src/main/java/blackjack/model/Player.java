public class Player {

    private String name;
    private Hand hand;
    private int money;
    private int bet;

    public Player(String name, int startingMoney) {
        this.name = name;
        this.money = startingMoney;
        this.bet = 0;
        this.hand = new Hand();
    }
    
    public String getName() {
        return name;
    }

    public Hand getHand() {
        return hand;
    }

    public int getMoney() {
        return money;
    }

    public int getBet() {
        return bet;
    }


}
