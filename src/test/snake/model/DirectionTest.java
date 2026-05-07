package snake.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DirectionTest {
    @Test
    public void directionsExist() {
        assertNotNull(Direction.UP);
        assertNotNull(Direction.DOWN);
        assertNotNull(Direction.LEFT);
        assertNotNull(Direction.RIGHT);
    }
}
