import java.util.ArrayList;
import java.util.Collections;

public class Deck {

    private ArrayList<Card> cards;

    public Deck() {
        cards = new ArrayList<Card>();
        makeNewDeck();

    }

    // Here is my function to make a new deck, it three forloops
    public void makeNewDeck() {
        cards.clear();

        String[] suits = {"H", "D", "C", "S"};
        String[] ranks = {"2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K", "A"};

        for (int deckNum = 0; deckNum < 4; deckNum++) {
            for (int s = 0; s < suits.length; s++) {
                for (int r = 0; r < ranks.length; r++) {
                    cards.add(new Card(ranks[r], suits[s]));
                }
            }
        }
        
        Collections.shuffle(cards);

    }

    // Hwew ia my function to draw a card
    public Card drawCard() {
        if (cards.size() == 0) {
            makeNewDeck();
        }

        return cards.remove(0);
    }

    // Similar to my other classes, the save text have the same purpose
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


    // Thiw will loqd the text
    public void loadText(String text) {
        cards.clear();

        if (text.length() == 0) {
            return;
        }

        String[] cardTexts = text.split("\\|");

        for (int i = 0; i < cardTexts.length; i++) {
            cards.add(Card.fromSaveText(cardTexts[i]));
        }
    }

}
