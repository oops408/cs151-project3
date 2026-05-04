package blackjack.model;

public class Card {
    private final Rank rank;
    private final Suit suit;

    public Card(Rank rank, Suit suit) {
        this.rank = rank;
        this.suit = suit;
    }

    public Rank getRank() {
        return rank;
    }

    public Suit getSuit() {
        return suit;
    }

    @Override
    public String toString() {
        return rank.name() + "-" + suit.name();
    }

    public static Card fromString(String text) {
        String[] parts = text.split("-");
        return new Card(Rank.valueOf(parts[0]), Suit.valueOf(parts[1]));
    }
}
