package snake.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SnakeMovementTest {
    @Test
    public void snakeMovesOneCellInCurrentDirection() {
        Snake snake = new Snake(new GridPosition(5, 5), Direction.RIGHT);
        snake.move();

        assertEquals(new GridPosition(5, 6), snake.getPosition());
    }

    @Test
    public void snakeGrowsAfterGrowIsCalled() {
        Snake snake = new Snake(new GridPosition(5, 5), Direction.RIGHT);
        snake.grow();
        snake.move();

        assertEquals(2, snake.getBody().size());
        assertTrue(snake.occupies(new GridPosition(5, 5)));
        assertTrue(snake.occupies(new GridPosition(5, 6)));
    }

    @Test
    public void hitSelfDetectsCollisionWithBody() {
        Snake snake = new Snake(new GridPosition(5, 5), Direction.RIGHT);

        snake.grow();
        snake.move();
        snake.grow();
        snake.move();
        snake.setDirection(Direction.DOWN);
        snake.grow();
        snake.move();
        snake.setDirection(Direction.LEFT);
        snake.grow();
        snake.move();
        snake.setDirection(Direction.UP);
        snake.move();

        assertTrue(snake.hitSelf());
        assertFalse(snake.getBody().isEmpty());
    }
}
