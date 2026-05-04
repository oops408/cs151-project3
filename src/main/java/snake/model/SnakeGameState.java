package snake.model;

import java.util.Random;

public class SnakeGameState {
    private static final int SIZE = 20;
    private final Random random;
    private Snake snake;
    private Food food;
    private int score;
    private boolean paused;
    private boolean gameOver;

    public SnakeGameState() {
        this.random = new Random();
        reset();
    }

    public void reset() {
        snake = new Snake(new GridPosition(10, 10), Direction.values()[random.nextInt(Direction.values().length)]);
        food = new Food(randomPosition());
        score = 0;
        paused = false;
        gameOver = false;
    }

    public Snake getSnake() {
        return snake;
    }

    public Food getFood() {
        return food;
    }

    public int getScore() {
        return score;
    }

    public boolean isPaused() {
        return paused;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public int getSize() {
        return SIZE;
    }

    public void togglePause() {
        paused = !paused;
    }

    public void setDirection(Direction direction) {
        snake.setDirection(direction);
    }

    public void tick() {
        if (paused || gameOver) {
            return;
        }
        snake.move();
        GridPosition head = snake.getPosition();
        if (head.row() < 0 || head.col() < 0 || head.row() >= SIZE || head.col() >= SIZE || snake.hitSelf()) {
            gameOver = true;
            return;
        }
        if (head.equals(food.getPosition())) {
            score += 10;
            snake.grow();
            food.setPosition(randomPosition());
        }
    }

    private GridPosition randomPosition() {
        return new GridPosition(random.nextInt(SIZE), random.nextInt(SIZE));
    }
}
