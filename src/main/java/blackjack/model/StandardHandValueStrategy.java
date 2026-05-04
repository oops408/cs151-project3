package blackjack.model;

public class StandardHandValueStrategy implements HandValueStrategy {
    @Override
    public int calculate(BlackjackHand hand) {
        int total = 0;
        int aces = 0;
        for (Card card : hand.getCards()) {
            total += card.getRank().getValue();
            if (card.getRank() == Rank.ACE) {
                aces++;
            }
        }
        while (total > 21 && aces > 0) {
            total -= 10;
            aces--;
        }
        return total;
    }
}
