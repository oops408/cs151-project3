package snake.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SnakeGameStateTest {
    @Test
    void pauseStopsMovement() {
        SnakeGameState state = new SnakeGameState();
        GridPosition before = state.getSnake().getPosition();
        state.togglePause();
        state.tick();
        assertEquals(before, state.getSnake().getPosition());
    }
}
