import java.util.ArrayList;

public class Hand {

    private ArrayList<Card> cards;

    public Hand() {
        cards = new ArrayList<Card>();
    }

    // First let's create some simple getter methods
    public Card getCard(int index) {
        return cards.get(index);
    }

    public int getSize() {
        return cards.size();
    }

    //Time for the more complex getter, this function will calculate the best value by treating our aces as 11s, and if it could bust. then it changes it to a 1
    public int getBestValue() {
        int total = 0;
        int aceCount = 0;

        for (int i = 0; i < cards.size(); i++) {
            Card card = cards.get(i);
            total = total + card.getValue();

            if (card.getRank().equals("A")) {
                aceCount++;
            }
        }

        while (total > 21 && aceCount > 0) {
            total = total - 10;
            aceCount--;
        }

        return total;
    }

    // This command will check whether the Ace is used as an 11 or as a 1
    public boolean isSoft() {
        int total = 0;
        int aceCount = 0;

        for (int i = 0; i < cards.size(); i++) {
            total = total + cards.get(i).getValue();
            if (cards.get(i).getRank().equals("A")) {
                aceCount++;
            }
        }

        while (total > 21 && aceCount > 0) {
            total = total - 10;
            aceCount--;
        }

        return aceCount > 0 && total <= 21;
    }

    // This function I created will check if the hand has busted
    public boolean isBust() {
        return getBestValue() > 21;
    }

    // I created this function to check the player got a Black jack, this occurs if the first two cards 
    public boolean isBlackjack() {
        return cards.size() == 2 && getBestValue() == 21;
    }

    // This function will save the text
    public String saveText() {
        String text = "";

        for (int i = 0; i < cards.size(); i++) {
            text = text + cards.get(i).saveText();

            if (i < cards.size() - 1) {
                text = text + "|";
            }
        }

        return text;
    }

    // Thia funxtion will load our text
    public void loadText(String text) {
        cards.clear();

        if (text == null || text.length() == 0) {
            return;
        }

        String[] cardTexts = text.split("\\|");

        for (int i = 0; i < cardTexts.length; i++) {
            if (cardTexts[i].length() > 0) {
                cards.add(Card.fromSaveText(cardTexts[i]));
            }
        }
    }
    

}
