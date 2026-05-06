package manager;

import common.GameType;

public class ScoreRecord {
    private final String username;
    private final int score;
    private final GameType gameType;

    public ScoreRecord(String username, int score, GameType gameType) {
        this.username = username;
        this.score = score;
        this.gameType = gameType;
    }

    public String getUsername() {
        return username;
    }

    public int getScore() {
        return score;
    }

    public GameType getGameType() {
        return gameType;
    }
}
