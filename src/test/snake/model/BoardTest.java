package snake.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BoardTest {
    @Test
    public void boardCanBeCreated() {
        Board board = new Board(10, 10);

        assertNotNull(board);
    }
}
