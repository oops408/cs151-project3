package snake.model;

public abstract class GameEntity {
    private int x;
    private int y;
 
    public GameEntity(int x, int y) {
        this.x = x;
        this.y = y;
    }
 
    public int getX() { return x; }
    public int getY() { return y; }
 
    protected void setX(int x) { this.x = x; }
    protected void setY(int y) { this.y = y; }
 
    public Point getPosition() {
        return new Point(x, y);
    }
}