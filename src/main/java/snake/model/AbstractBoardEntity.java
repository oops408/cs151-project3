package snake.model;

public abstract class AbstractBoardEntity {
    private GridPosition position;

    protected AbstractBoardEntity(GridPosition position) {
        this.position = position;
    }

    public GridPosition getPosition() {
        return position;
    }

    public void setPosition(GridPosition position) {
        this.position = position;
    }
}
