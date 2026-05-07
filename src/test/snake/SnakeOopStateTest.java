package snake;

import org.junit.jupiter.api.Test;
import snake.model.Direction;
import snake.model.Food;
import snake.model.GameEntity;
import snake.model.GameState;
import snake.model.Movable;
import snake.model.Point;
import snake.model.Snake;

import static org.junit.jupiter.api.Assertions.*;

public class SnakeOopStateTest {
    @Test
    void snakeExtendsGameEntityAndImplementsMovable() {
        Snake snake = new Snake(10, 10, Direction.RIGHT);

        assertTrue(snake instanceof GameEntity);
        assertTrue(snake instanceof Movable);
    }

    @Test
    void foodExtendsGameEntity() {
        SnakeController controller = new SnakeController(20, 20, new SnakeScoreManager());
        Food food = controller.getFood();

        assertTrue(food instanceof GameEntity);
    }

    @Test
    void snakeStartsNearCenterAndUsesValidDirection() {
        SnakeController controller = new SnakeController(20, 20, new SnakeScoreManager());

        Point head = controller.getSnake().getHead();

        assertTrue(head.getX() >= 8 && head.getX() <= 12);
        assertTrue(head.getY() >= 8 && head.getY() <= 12);
        assertNotNull(controller.getSnake().getDirection());
    }

    @Test
    void pauseStopsSnakeMovement() {
        SnakeController controller = new SnakeController(20, 20, new SnakeScoreManager());

        controller.startGame();
        controller.togglePause();

        Point before = controller.getSnake().getHead();
        controller.tick();
        Point after = controller.getSnake().getHead();

        assertEquals(GameState.PAUSED, controller.getState());
        assertEquals(before, after);
    }
}
