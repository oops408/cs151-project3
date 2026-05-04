package snake.model;

import java.util.LinkedList;
import java.util.List;

public class Snake extends AbstractBoardEntity implements Movable {
    private final LinkedList<GridPosition> body;
    private Direction direction;
    private boolean growNextMove;

    public Snake(GridPosition start, Direction direction) {
        super(start);
        this.body = new LinkedList<>();
        this.body.add(start);
        this.direction = direction;
        this.growNextMove = false;
    }

    public List<GridPosition> getBody() {
        return List.copyOf(body);
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public void grow() {
        growNextMove = true;
    }

    @Override
    public void move() {
        GridPosition head = body.getFirst();
        GridPosition next = switch (direction) {
            case UP -> new GridPosition(head.row() - 1, head.col());
            case DOWN -> new GridPosition(head.row() + 1, head.col());
            case LEFT -> new GridPosition(head.row(), head.col() - 1);
            case RIGHT -> new GridPosition(head.row(), head.col() + 1);
        };
        body.addFirst(next);
        setPosition(next);
        if (!growNextMove) {
            body.removeLast();
        } else {
            growNextMove = false;
        }
    }

    public boolean occupies(GridPosition position) {
        return body.contains(position);
    }

    public boolean hitSelf() {
        GridPosition head = body.getFirst();
        return body.stream().skip(1).anyMatch(head::equals);
    }
}
