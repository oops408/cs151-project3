package snake.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SnakeGameOverTest {
    @Test
    public void hittingWallEndsGame() {
        SnakeGameState state = new SnakeGameState();
        state.setDirection(Direction.UP);

        // The board is 20 cells tall, so repeatedly moving up must eventually hit the wall.
        for (int i = 0; i < 25; i++) {
            state.tick();
        }

        assertTrue(state.isGameOver());
    }

    @Test
    public void resetStartsNewPlayableGame() {
        SnakeGameState state = new SnakeGameState();
        state.setDirection(Direction.UP);

        for (int i = 0; i < 25; i++) {
            state.tick();
        }
        assertTrue(state.isGameOver());

        state.reset();
        assertFalse(state.isGameOver());
        assertFalse(state.isPaused());
    }
}
