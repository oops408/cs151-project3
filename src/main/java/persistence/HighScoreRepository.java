package persistence;

import common.GameType;
import manager.ScoreRecord;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class HighScoreRepository {
    public static final String FILE_NAME = "high_scores.txt";

    public HighScoreRepository() {
        initializeDefaults();
    }

    private void initializeDefaults() {
        List<String> lines = FileStorage.readAllLines(FILE_NAME);
        if (lines.isEmpty()) {
            lines = new ArrayList<>();
            lines.add("BLACKJACK|Default|1000");
            lines.add("SNAKE|Default|1000");
            FileStorage.writeAllLines(FILE_NAME, lines);
        }
    }

    public List<ScoreRecord> loadScores() {
        List<ScoreRecord> scores = new ArrayList<>();
        for (String line : FileStorage.readAllLines(FILE_NAME)) {
            if (line.isBlank()) {
                continue;
            }
            String[] parts = line.split("\\|");
            if (parts.length == 3) {
                scores.add(new ScoreRecord(parts[1], Integer.parseInt(parts[2]), GameType.valueOf(parts[0])));
            }
        }
        return scores;
    }

    public void recordScore(GameType gameType, String username, int score) {
        List<String> lines = new ArrayList<>(FileStorage.readAllLines(FILE_NAME));
        lines.add(gameType.name() + "|" + username + "|" + score);
        FileStorage.writeAllLines(FILE_NAME, lines);
    }

    public List<ScoreRecord> getTopFive(GameType gameType) {
        return loadScores().stream()
                .filter(score -> score.getGameType() == gameType)
                .sorted(Comparator.comparingInt(ScoreRecord::getScore).reversed())
                .limit(5)
                .collect(Collectors.toList());
    }
}
