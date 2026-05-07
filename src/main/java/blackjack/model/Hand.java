package blackjack.model;

import java.util.ArrayList;

public class Hand {
    private ArrayList<Card> cards;

    public Hand() {
        cards = new ArrayList<Card>();
    }

    public void addCard(Card card) {
        cards.add(card);
    }

    public void clear() {
        cards.clear();
    }

    public Card getCard(int index) {
        return cards.get(index);
    }

    public int getSize() {
        return cards.size();
    }

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

    public boolean isBust() {
        return getBestValue() > 21;
    }

    public boolean isBlackjack() {
        return cards.size() == 2 && getBestValue() == 21;
    }

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
