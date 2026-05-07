package snake.model;

import java.util.List;
import java.util.Random;
 
public class Board {
    private int cols;
    private int rows;
    private Random rand = new Random();
 
    public Board(int cols, int rows) {
        this.cols = cols;
        this.rows = rows;
    }
 
    public int getCols() { return cols; }
    public int getRows() { return rows; }
 
    public boolean isOutOfBounds(Point p) {
        return p.getX() < 0 || p.getY() < 0 || p.getX() >= cols || p.getY() >= rows;
    }

    public Point randomCell(List<Point> occupied) {
        Point p;
        do {
            int x = rand.nextInt(cols);
            int y = rand.nextInt(rows);
            p = new Point(x, y);
        } while (occupied.contains(p));
        return p;
    }
 
    public Direction randomDirection() {
        Direction[] dirs = Direction.values();
        return dirs[rand.nextInt(dirs.length)];
    }
}