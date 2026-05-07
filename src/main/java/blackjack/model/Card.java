public class Card {

  private String rank;
  private String suit;
  
  public Card(String rank, String suit) {
    this.rank = rank;
    this.suit = suit;
  }

  // I created rhis function to get the value of our face cards
  public int getValue() {
        if (rank.equals("A")) {
            return 11;
        } else if (rank.equals("K") || rank.equals("Q") || rank.equals("J")) {
            return 10;
        } else {
            return Integer.parseInt(rank);
        }
  }

  // Here are my getter methods
  public String getRank() {
        return rank;
    }
  public String getSuit() {
        return suit;
    }
  public String saveText() {
        return rank + suit;
    }

  // This function will get our saved text and return certain information, that being our rank and suit
  public static Card fromSaveText(String text) {
        String suit = text.substring(text.length() - 1);
        String rank = text.substring(0, text.length() - 1);
        return new Card(rank, suit);
    }

  // 
    public String toString() {
        String symbol = suit;

        if (suit.equals("H")) {
            symbol = "♥";
        } else if (suit.equals("D")) {
            symbol = "♦";
        } else if (suit.equals("C")) {
            symbol = "♣";
        } else if (suit.equals("S")) {
            symbol = "♠";
        }

        return rank + symbol;
    }

  // Here is my overriden toString
  @Override
  public String toString() {
        String symbol = suit;

        if (suit.equals("H")) {
            symbol = "♥";
        } else if (suit.equals("D")) {
            symbol = "♦";
        } else if (suit.equals("C")) {
            symbol = "♣";
        } else if (suit.equals("S")) {
            symbol = "♠";
        }

        return rank + symbol;
    }



}
