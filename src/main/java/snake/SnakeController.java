package snake;

import snake.model.*;
import javafx.scene.Parent;
import manager.GameManagerController;

import java.util.List;

public class SnakeController {

    // Core objects
    private final Board board;
    private Snake snake;
    private Food food;
    private final SnakeScoreManager scoreManager;

    // Game state
    private GameState state = GameState.READY;

    // Callbacks from UI layer
    private Runnable onUpdate;    // refresh screen each tick
    private Runnable onGameOver;  // triggered once when game ends
    private GameManagerController managerController;

    // Constructor used by GameManager
    public SnakeController(GameManagerController managerController) {
        this(20, 20, new SnakeScoreManager());
        this.managerController = managerController;
    }

    // Main constructor
    public SnakeController(int cols, int rows, SnakeScoreManager scoreManager) {
        this.board = new Board(cols, rows);
        this.scoreManager = scoreManager;
        initGame();
    }

    // Initialize / reset game
    public void initGame() {
        int midX = board.getCols() / 2;
        int midY = board.getRows() / 2;

        Direction startDirection = board.randomDirection();
        snake = new Snake(midX, midY, startDirection);

        food = new Food(board);
        food.respawn(snake.getSegments());

        scoreManager.reset();
        state = GameState.READY;
    }

    // Start or restart game
    public void startGame() {
        if (state == GameState.READY || state == GameState.GAME_OVER) {
            if (state == GameState.GAME_OVER) initGame();
            state = GameState.RUNNING;
        }
    }

    // Pause / resume toggle
    public void togglePause() {
        if (state == GameState.RUNNING) {
            state = GameState.PAUSED;
        } else if (state == GameState.PAUSED) {
            state = GameState.RUNNING;
        }
    }

    /**
     * Main game loop tick.
     * Only runs when game is active.
     */
    public void tick() {
        if (state != GameState.RUNNING) return;

        snake.move();

        // Wall collision
        if (board.isOutOfBounds(snake.getHead())) {
            endGame();
            return;
        }

        // Self collision
        if (snake.checkSelfCollision()) {
            endGame();
            return;
        }

        // Food eaten
        if (snake.getHead().equals(food.getPosition())) {
            snake.grow();
            scoreManager.addScore(10);
            scoreManager.incrementLevel(snake.getLength());

            List<Point> occupied = snake.getSegments();
            food.respawn(occupied);
        }

        // Update UI
        if (onUpdate != null) onUpdate.run();
    }

    // Change direction from keyboard input
    public void changeDirection(Direction dir) {
        if (state == GameState.RUNNING) {
            snake.changeDirection(dir);
        }
    }

    // End game logic
    private void endGame() {
        state = GameState.GAME_OVER;
        scoreManager.updateHighScore();

        if (onGameOver != null) onGameOver.run();
        if (onUpdate != null) onUpdate.run(); // final render
    }

    // Getters
    public Snake getSnake() { return snake; }
    public Food getFood() { return food; }
    public Board getBoard() { return board; }
    public GameState getState() { return state; }
    public SnakeScoreManager getScoreManager() { return scoreManager; }

    // UI callbacks
    public void setOnUpdate(Runnable cb) { this.onUpdate = cb; }
    public void setOnGameOver(Runnable cb) { this.onGameOver = cb; }

    // Create UI view
    public Parent createView() {
        SnakeUI snakeUI = new SnakeUI();
        return snakeUI.createView(managerController);
    }
}