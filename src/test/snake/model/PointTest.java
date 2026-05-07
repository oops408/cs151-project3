package snake.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PointTest {
    @Test
    public void pointCanBeCreated() {
        Point point = new Point(2, 3);

        assertNotNull(point);
    }

    @Test
    public void pointsWithSameCoordinatesAreEqual() {
        Point first = new Point(2, 3);
        Point second = new Point(2, 3);

        assertEquals(first, second);
    }
}
