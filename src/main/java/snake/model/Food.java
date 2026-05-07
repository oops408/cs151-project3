package snake.model;

import java.util.List;
 
public class Food extends GameEntity {
 
    private Board board;
 
    public Food(Board board) {
        super(0, 0);
        this.board = board;
    }
 
    public void respawn(List<Point> occupied) {
        Point p = board.randomCell(occupied);
        setX(p.getX());
        setY(p.getY());
    }
}