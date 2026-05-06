package model;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
 
public class Snake extends GameEntity implements Movable {
 
    private LinkedList<Point> segments = new LinkedList<>();
    private Direction direction;
    private Direction nextDirection;
    private boolean growing = false;
 
    public Snake(int startX, int startY, Direction startDirection) {
        super(startX, startY);
        this.direction = startDirection;
        this.nextDirection = startDirection;
 
        segments.add(new Point(startX, startY));
        segments.add(new Point(startX - startDirection.getDx(), startY - startDirection.getDy()));
        segments.add(new Point(startX - startDirection.getDx() * 2, startY - startDirection.getDy() * 2));
    }
 
    @Override
    public void move() {
        direction = nextDirection;
 
        Point head = segments.getFirst();
        Point newHead = head.translate(direction.getDx(), direction.getDy());
        segments.addFirst(newHead);
        setX(newHead.getX());
        setY(newHead.getY());
 
        if (growing) {
            growing = false;
        } else {
            segments.removeLast();
        }
    }
 
    @Override
    public void changeDirection(Direction newDir) {
        if (newDir != null && !newDir.isOpposite(direction)) {
            nextDirection = newDir;
        }
    }
 
    public void grow() {
        growing = true;
    }
 
    public Point getHead() {
        return segments.getFirst();
    }
 
    public List<Point> getSegments() {
        return new ArrayList<>(segments);
    }
 
    public boolean checkSelfCollision() {
        Point head = segments.getFirst();
        for (int i = 1; i < segments.size(); i++) {
            if (segments.get(i).equals(head)) {
                return true;
            }
        }
        return false;
    }
 
    public int getLength() {
        return segments.size();
    }
 
    public Direction getDirection() {
        return direction;
    }
}