package snake.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class FoodTest {
    @Test
    public void foodCanBeCreated() {
        Food food = new Food(new Point(5, 5));

        assertNotNull(food);
    }
}
