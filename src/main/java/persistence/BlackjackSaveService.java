package persistence;

import blackjack.model.BlackjackGame;

public class BlackjackSaveService {
    public String save(BlackjackGame game) {
        if (game == null) {
            return "";
        }
        return game.makeSaveString();
    }

    public boolean load(BlackjackGame game, String saveStateString) {
        if (game == null) {
            return false;
        }
        return game.loadFromString(saveStateString);
    }

    public String save(String saveStateString) {
        return saveStateString == null ? "" : saveStateString;
    }

    public String load(String saveStateString) {
        return saveStateString == null ? "" : saveStateString;
    }
}
