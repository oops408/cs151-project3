package snake.model;

public class Point {
    private int x;
    private int y;
 
    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }
 
    public int getX() { return x; }
    public int getY() { return y; }
 
    public Point translate(int dx, int dy) {
        return new Point(x + dx, y + dy);
    }
    
    @Override
    public boolean equals(Object o) {
        if (o instanceof Point) {
            Point other = (Point) o;
            return this.x == other.x && this.y == other.y;
        }
        return false;
    }
 
    @Override
    public int hashCode() {
        return 31 * x + y;
    }
 
    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}
