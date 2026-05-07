package snake;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class SnakeScoreTest {

    private SnakeScoreManager manager;

    @Before
    public void setUp() {
        manager = new SnakeScoreManager();

        // reset state for clean testing
        manager.clearHighScore();
        manager.reset();
    }

    @Test
    public void testInitialState() {
        assertEquals(0, manager.getCurrentScore());
        assertEquals(1, manager.getLevel());
    }

    @Test
    public void testAddScore() {
        manager.addScore(10);
        assertEquals(10, manager.getCurrentScore());
    }

    @Test
    public void testHighScoreUpdate() {
        manager.addScore(20);
        manager.updateHighScore();
        assertTrue(manager.getHighScore() >= 20);
    }

    @Test
    public void testLevelIncrease() {
        manager.incrementLevel(20);
        assertTrue(manager.getLevel() >= 1);
    }
}