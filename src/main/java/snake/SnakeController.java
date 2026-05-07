package snake;

import model.*;

import java.util.List;

public class SnakeController {

    // ── Game state ────────────────────────────────────────────────
    public enum GameState { READY, RUNNING, PAUSED, GAME_OVER }

    // ── Core objects ──────────────────────────────────────────────
    private final Board board;
    private Snake snake;
    private Food  food;
    private final SnakeScoreManager scoreManager;

    // ── State ─────────────────────────────────────────────────────
    private GameState state = GameState.READY;

    // ── Callbacks registered by the UI layer ──────────────────────
    private Runnable onUpdate;    // called every tick to refresh the screen
    private Runnable onGameOver;  // called once when the game ends

    // ─────────────────────────────────────────────────────────────
    public SnakeController(int cols, int rows, SnakeScoreManager scoreManager) {
        this.board        = new Board(cols, rows);
        this.scoreManager = scoreManager;
        initGame();
    }

    // ── Initialisation / reset ────────────────────────────────────
    public void initGame() {
        int midX = board.getCols() / 2;
        int midY = board.getRows() / 2;

        snake = new Snake(midX, midY, Direction.RIGHT);

        food  = new Food(board);
        food.respawn(snake.getSegments());

        scoreManager.reset();
        state = GameState.READY;
    }

    // ── Public actions ────────────────────────────────────────────

    /** Starts (or restarts) the game from READY or GAME_OVER state. */
    public void startGame() {
        if (state == GameState.READY || state == GameState.GAME_OVER) {
            if (state == GameState.GAME_OVER) initGame();
            state = GameState.RUNNING;
        }
    }

    /** Toggles between RUNNING and PAUSED. */
    public void togglePause() {
        if (state == GameState.RUNNING) {
            state = GameState.PAUSED;
        } else if (state == GameState.PAUSED) {
            state = GameState.RUNNING;
        }
    }

    /**
     * Main game tick — called periodically by the UI layer's AnimationTimer.
     * Logic only runs while the game is in the RUNNING state.
     */
    public void tick() {
        if (state != GameState.RUNNING) return;

        snake.move();

        // 1. Wall collision
        if (board.isOutOfBounds(snake.getHead())) {
            endGame();
            return;
        }

        // 2. Self collision
        if (snake.checkSelfCollision()) {
            endGame();
            return;
        }

        // 3. Food eaten
        if (snake.getHead().equals(food.getPosition())) {
            snake.grow();
            scoreManager.addScore(10);
            scoreManager.incrementLevel(snake.getLength());
            List<Point> occupied = snake.getSegments();
            food.respawn(occupied);
        }

        // 4. Notify the UI to redraw
        if (onUpdate != null) onUpdate.run();
    }

    /** Forwards a direction change to the snake (called from keyboard events). */
    public void changeDirection(Direction dir) {
        if (state == GameState.RUNNING) {
            snake.changeDirection(dir);
        }
    }

    // ── Private helpers ───────────────────────────────────────────
    private void endGame() {
        state = GameState.GAME_OVER;
        scoreManager.updateHighScore();
        if (onGameOver != null) onGameOver.run();
        if (onUpdate   != null) onUpdate.run();   // render the final frame
    }

    // ── Getters ───────────────────────────────────────────────────
    public Snake             getSnake()        { return snake; }
    public Food              getFood()         { return food;  }
    public Board             getBoard()        { return board; }
    public GameState         getState()        { return state; }
    public SnakeScoreManager getScoreManager() { return scoreManager; }

    // ── Callback registration ─────────────────────────────────────
    public void setOnUpdate(Runnable cb)   { this.onUpdate   = cb; }
    public void setOnGameOver(Runnable cb) { this.onGameOver = cb; }
}