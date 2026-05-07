package persistence;

import common.GameType;
import manager.ScoreRecord;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class HighScoreRepositoryTest {
    private final Path scorePath = Path.of(HighScoreRepository.FILE_NAME);
    private List<String> originalLines;
    private boolean originallyExisted;

    @BeforeEach
    void saveOriginalFile() throws Exception {
        originallyExisted = Files.exists(scorePath);
        if (originallyExisted) {
            originalLines = Files.readAllLines(scorePath);
        }
        Files.deleteIfExists(scorePath);
    }

    @AfterEach
    void restoreOriginalFile() throws Exception {
        if (originallyExisted) {
            Files.write(scorePath, originalLines);
        } else {
            Files.deleteIfExists(scorePath);
        }
    }

    @Test
    void repositoryCreatesDefaultScoresForBothGames() {
        HighScoreRepository repository = new HighScoreRepository();

        List<ScoreRecord> blackjackScores = repository.getTopFive(GameType.BLACKJACK);
        List<ScoreRecord> snakeScores = repository.getTopFive(GameType.SNAKE);

        assertFalse(blackjackScores.isEmpty());
        assertFalse(snakeScores.isEmpty());
        assertEquals(1000, blackjackScores.get(0).getScore());
        assertEquals(1000, snakeScores.get(0).getScore());
    }

    @Test
    void repositoryRecordsAndSortsScoresDescending() {
        HighScoreRepository repository = new HighScoreRepository();

        repository.recordScore(GameType.SNAKE, "low", 10);
        repository.recordScore(GameType.SNAKE, "high", 50);

        List<ScoreRecord> scores = repository.getTopFive(GameType.SNAKE);

        assertEquals("Default", scores.get(0).getUsername());
        assertTrue(scores.stream().anyMatch(score -> score.getUsername().equals("high") && score.getScore() == 50));
        assertTrue(scores.stream().anyMatch(score -> score.getUsername().equals("low") && score.getScore() == 10));
    }
}
