package persistence;

import common.GameType;
import manager.ScoreRecord;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class HighScoreRepositoryTest {
    @Test
    public void topFiveScoresAreSortedHighestToLowest() throws Exception {
        Files.deleteIfExists(Path.of(HighScoreRepository.FILE_NAME));

        HighScoreRepository repository = new HighScoreRepository();
        repository.recordScore(GameType.SNAKE, "low", 10);
        repository.recordScore(GameType.SNAKE, "high", 90);
        repository.recordScore(GameType.SNAKE, "middle", 50);

        List<ScoreRecord> scores = repository.getTopFive(GameType.SNAKE);

        assertEquals("Default", scores.get(0).getUsername());
        assertEquals(1000, scores.get(0).getScore());
        assertEquals("high", scores.get(1).getUsername());
        assertEquals(90, scores.get(1).getScore());

        Files.deleteIfExists(Path.of(HighScoreRepository.FILE_NAME));
    }

    @Test
    public void defaultScoresAreCreatedForBothGames() throws Exception {
        Files.deleteIfExists(Path.of(HighScoreRepository.FILE_NAME));

        HighScoreRepository repository = new HighScoreRepository();

        assertTrue(repository.getTopFive(GameType.BLACKJACK).size() >= 1);
        assertTrue(repository.getTopFive(GameType.SNAKE).size() >= 1);

        Files.deleteIfExists(Path.of(HighScoreRepository.FILE_NAME));
    }
}
