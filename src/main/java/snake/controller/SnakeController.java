package snake.controller;

import common.BaseGameController;
import common.GameType;
import manager.GameManagerController;
import manager.ToolbarFactory;
import snake.model.Direction;
import snake.model.GridPosition;
import snake.model.SnakeGameState;
import utils.MusicPlayer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.util.Duration;

public class SnakeController extends BaseGameController {
    private static final int CELL_SIZE = 25;
    private SnakeGameState gameState;
    private Timeline timeline;
    private Canvas canvas;
    private Label infoLabel;
    private BorderPane root;
    private final MusicPlayer musicPlayer;
    private boolean scoreRecorded;

    public SnakeController(GameManagerController manager) {
        super(manager);
        this.gameState = new SnakeGameState();
        this.musicPlayer = new MusicPlayer();
        this.scoreRecorded = false;
    }

    @Override
    public String getTitle() {
        return "Snake";
    }

    @Override
    public void resetGame() {
        gameState.reset();
        scoreRecorded = false;
        draw();
        requestGameFocus();
    }

    @Override
    public Parent createView() {
        musicPlayer.playLoop("/audio/snake.mp3");
        root = new BorderPane();
        root.setTop(ToolbarFactory.create(manager));
        root.setPadding(new Insets(15));

        canvas = new Canvas(gameState.getSize() * CELL_SIZE, gameState.getSize() * CELL_SIZE);
        canvas.setFocusTraversable(true);
        infoLabel = new Label("Score: 0");
        infoLabel.getStyleClass().add("section-title");

        Button restartButton = new Button("Restart");
        restartButton.setOnAction(e -> resetGame());

        Label help = new Label("Arrow keys move. Escape pauses/resumes. Click the board if the keys do not respond.");
        help.setWrapText(true);

        HBox topInfo = new HBox(20, infoLabel, restartButton);
        topInfo.setAlignment(Pos.CENTER_LEFT);
        VBox center = new VBox(10, topInfo, canvas, help);
        center.getStyleClass().add("card");
        center.setMaxWidth(700);
        center.setOnMouseClicked(e -> requestGameFocus());
        canvas.setOnMouseClicked(e -> requestGameFocus());
        root.setCenter(center);

        setupLoop();
        draw();
        setupKeyboardControls();
        requestGameFocus();
        return root;
    }

    private void setupKeyboardControls() {
        // JavaFX key events only go to the node that currently has focus.
        // Listening on the Scene after it exists makes arrow keys reliable even if a button had focus before.
        root.sceneProperty().addListener((observable, oldScene, newScene) -> {
            if (newScene != null) {
                newScene.setOnKeyPressed(event -> {
                    handleKeyPress(event.getCode());
                    event.consume();
                });
                requestGameFocus();
            }
        });

        root.setOnKeyPressed(event -> {
            handleKeyPress(event.getCode());
            event.consume();
        });
        root.setFocusTraversable(true);
    }

    private void requestGameFocus() {
        if (canvas != null) {
            canvas.requestFocus();
        }
    }

    private void setupLoop() {
        if (timeline != null) {
            timeline.stop();
        }
        timeline = new Timeline(new KeyFrame(Duration.millis(220), e -> {
            gameState.tick();
            if (gameState.isGameOver() && !scoreRecorded) {
                manager.getHighScoreRepository().recordScore(GameType.SNAKE, manager.getCurrentUser(), gameState.getScore());
                scoreRecorded = true;
            }
            draw();
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    private void handleKeyPress(KeyCode code) {
        if (code == KeyCode.UP) {
            gameState.setDirection(Direction.UP);
        } else if (code == KeyCode.DOWN) {
            gameState.setDirection(Direction.DOWN);
        } else if (code == KeyCode.LEFT) {
            gameState.setDirection(Direction.LEFT);
        } else if (code == KeyCode.RIGHT) {
            gameState.setDirection(Direction.RIGHT);
        } else if (code == KeyCode.ESCAPE) {
            gameState.togglePause();
        }
        draw();
    }

    private void draw() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        gc.strokeRect(0, 0, canvas.getWidth(), canvas.getHeight());

        for (GridPosition segment : gameState.getSnake().getBody()) {
            gc.fillRect(segment.col() * CELL_SIZE, segment.row() * CELL_SIZE, CELL_SIZE, CELL_SIZE);
        }

        GridPosition food = gameState.getFood().getPosition();
        gc.fillOval(food.col() * CELL_SIZE, food.row() * CELL_SIZE, CELL_SIZE, CELL_SIZE);

        infoLabel.setText("Score: " + gameState.getScore() + (gameState.isPaused() ? " | Paused" : ""));

        if (gameState.isGameOver()) {
            drawOverlay(gc, "Game Over", "Final Score: " + gameState.getScore() + "   Click Restart to play again");
        } else if (gameState.isPaused()) {
            drawOverlay(gc, "Paused", "Press Escape to resume");
        }
    }

    private void drawOverlay(GraphicsContext gc, String mainText, String subText) {
        double width = canvas.getWidth();
        gc.fillRect(70, 170, width - 140, 120);
        gc.clearRect(75, 175, width - 150, 110);
        gc.setFont(Font.font(28));
        gc.fillText(mainText, 110, 220);
        gc.setFont(Font.font(16));
        gc.fillText(subText, 110, 250);
    }
}
