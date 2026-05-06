package persistence;

import blackjack.model.BlackjackGameState;
import blackjack.model.BlackjackParticipant;
import blackjack.model.Card;
import utils.CryptoUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

public class BlackjackSaveService {
    public String save(BlackjackGameState state) {
        StringJoiner joiner = new StringJoiner(";");

        // Store round-level information first.
        joiner.add(String.valueOf(state.getActiveIndex()));
        joiner.add(Boolean.toString(state.isRoundOver()));
        joiner.add(escape(state.getStatusMessage()));
        joiner.add(cardsToText(state.getDeck().getRemainingCards()));

        // Store each participant's money, bet, standing state, and cards.
        for (BlackjackParticipant participant : state.getParticipants()) {
            joiner.add(escape(participant.getName()) + "|"
                    + participant.getBalance() + "|"
                    + participant.getCurrentBet() + "|"
                    + participant.isStanding() + "|"
                    + cardsToText(participant.getHand().getCards()));
        }

        // Encrypt the readable save data so cards and money are not obvious in the output.
        return CryptoUtils.encrypt(joiner.toString());
    }

    public BlackjackGameState load(String saveStateString, String username) {
        String raw = CryptoUtils.decrypt(saveStateString);
        String[] sections = raw.split(";", -1);

        BlackjackGameState state = new BlackjackGameState(username);
        List<BlackjackParticipant> participants = state.getParticipants();

        state.setActiveIndex(Integer.parseInt(sections[0]));
        state.setRoundOver(Boolean.parseBoolean(sections[1]));
        state.setStatusMessage(unescape(sections[2]));
        state.getDeck().replaceRemainingCards(textToCards(sections[3]));

        for (int i = 0; i < participants.size(); i++) {
            String[] parts = sections[i + 4].split("\\|", -1);
            BlackjackParticipant participant = participants.get(i);

            participant.resetTurn();
            participant.setBalance(Integer.parseInt(parts[1]));
            participant.setCurrentBet(Integer.parseInt(parts[2]));
            participant.setStanding(Boolean.parseBoolean(parts[3]));

            for (Card card : textToCards(parts[4])) {
                participant.getHand().addCard(card);
            }
        }
        return state;
    }

    private String cardsToText(List<Card> cards) {
        StringJoiner cardJoiner = new StringJoiner(",");
        for (Card card : cards) {
            cardJoiner.add(card.toString());
        }
        return cardJoiner.toString();
    }

    private List<Card> textToCards(String text) {
        List<Card> cards = new ArrayList<>();
        if (text == null || text.isBlank()) {
            return cards;
        }
        for (String cardText : text.split(",")) {
            cards.add(Card.fromString(cardText));
        }
        return cards;
    }

    // Keep delimiter characters out of the save string fields.
    private String escape(String text) {
        return text.replace("%", "%25").replace(";", "%3B").replace("|", "%7C");
    }

    private String unescape(String text) {
        return text.replace("%7C", "|").replace("%3B", ";").replace("%25", "%");
    }
}
