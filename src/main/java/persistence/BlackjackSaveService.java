package persistence;

import blackjack.model.*;
import utils.CryptoUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

public class BlackjackSaveService {
    public String save(BlackjackGameState state) {
        StringJoiner joiner = new StringJoiner(";");
        joiner.add(String.valueOf(state.getActiveIndex()));
        joiner.add(Boolean.toString(state.isRoundOver()));
        joiner.add(cardsToText(state.getDeck().getRemainingCards()));
        for (BlackjackParticipant participant : state.getParticipants()) {
            joiner.add(participant.getName() + "|" + participant.getBalance() + "|"
                    + participant.getCurrentBet() + "|" + cardsToText(participant.getHand().getCards()));
        }
        return CryptoUtils.encrypt(joiner.toString());
    }

    public BlackjackGameState load(String saveStateString, String username) {
        String raw = CryptoUtils.decrypt(saveStateString);
        String[] sections = raw.split(";");
        BlackjackGameState state = new BlackjackGameState(username);
        List<BlackjackParticipant> participants = state.getParticipants();
        state.setActiveIndex(Integer.parseInt(sections[0]));
        state.setRoundOver(Boolean.parseBoolean(sections[1]));
        state.getDeck().replaceRemainingCards(textToCards(sections[2]));

        for (int i = 0; i < participants.size(); i++) {
            String[] parts = sections[i + 3].split("\\|", 4);
            BlackjackParticipant participant = participants.get(i);
            participant.resetTurn();
            participant.setBalance(Integer.parseInt(parts[1]));
            participant.setCurrentBet(Integer.parseInt(parts[2]));
            for (Card card : textToCards(parts[3])) {
                participant.getHand().addCard(card);
            }
        }
        state.setStatusMessage("Game loaded from save state string.");
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
}
