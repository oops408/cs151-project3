package snake.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class FoodTest {
    @Test
    public void foodCanBeCreatedWithBoard() {
        Board board = new Board(10, 10);
        Food food = new Food(board);

        assertNotNull(food);
    }
}
