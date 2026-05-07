public class Player {

    // Here are my variables
    private String name;
    private Hand hand;
    private int money;
    private int bet;

    // This is my establishing a player
    public Player(String name, int startingMoney) {
        this.name = name;
        this.money = startingMoney;
        this.bet = 0;
        this.hand = new Hand();
    }

    // Here is my function which I made to place bets
    public void placeBet(int amount) {
		
        if (amount < 0) {
			amount = 0;
		}

		if (amount > money) {
			amount = money;
		}

		bet = amount;
		money = money - amount;
	}

    // Here is the function which deals whenever you win
	public void winBet() {
		money = money + bet + bet;
		bet = 0;
	}
    

    // Here is the function which deals whenever you lose
	public void loseBet() {
		bet = 0;
	}

    // Here is the function which pushes your bet, this only occurs whenever you tie with the dealer
	public void pushBet() {
		money = money + bet;
		bet = 0;
	}

    // Here is my function to handle clearing the hand
	public void clearHand() {
		hand = new Hand();
		bet = 0;
	}

    // This function's purpose is to save the text
	public String saveText() {
		return name + "," + money + "," + bet + "," + hand.saveText();
	}

	// Now here is my function to deal with loading text
	public void loadText(String text) {
		String[] parts = text.split(",", 4);
		name = parts[0];
		money = Integer.parseInt(parts[1]);
		bet = Integer.parseInt(parts[2]);
		hand = new Hand();
		hand.loadText(parts[3]);
	}



    // I created the following getter methods for name, hand, money, and bet
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
