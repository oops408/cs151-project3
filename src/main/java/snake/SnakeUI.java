package snake;

import common.GameType;
import manager.GameManagerController;
import snake.model.Direction;
import snake.model.Point;
import utils.MusicPlayer;
import snake.model.GameState;

import javafx.animation.AnimationTimer;
import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.*;


public class SnakeUI extends Application {

    private static final int COLS = 20;
    private static final int ROWS = 20;
    private static final int CELL = 28;
    private static final int CANVAS_W = COLS * CELL;
    private static final int CANVAS_H = ROWS * CELL;

    private static final Color BG = Color.web("BLACK");
    private static final Color GRID_COLOR = Color.web("WHITE");
    private static final Color TEXT_DARK = Color.web("#1A1A1A");
    private static final Color TEXT_MID = Color.web("#888888");
    private static final Color TEXT_LIGHT = Color.web("#BBBBBB");
    private static final Color FOOD_RED = Color.web("#FF3B30");

    private double snakeHue = 200.0;

    private final List<Particle> particles = new ArrayList<>();
    private final Random rng = new Random();

    private SnakeScoreManager scoreManager;
    private SnakeController controller;
    private MusicPlayer musicPlayer;
    private GameManagerController managerController;

    private Canvas canvas;
    private Label lblScore;
    private Label lblBest;
    private Label lblLevel;
    private Button btnStart;

    private AnimationTimer gameLoop;
    private long lastTickNs = 0;
    private Point prevFoodPos = null;

    @Override
    public void start(Stage stage) {
        Parent root = createView();

        Scene scene = new Scene(root, CANVAS_W, CANVAS_H + 52);
        scene.setFill(Color.WHITE);

        stage.setTitle("Snake");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
        canvas.requestFocus();
    }

    public Parent createView(GameManagerController managerController) {
        this.managerController = managerController;
        return createView();
    }

    public Parent createView() {
        scoreManager = new SnakeScoreManager();
        controller = new SnakeController(COLS, ROWS, scoreManager);
        musicPlayer = new MusicPlayer();

        controller.setOnUpdate(this::render);
        controller.setOnGameOver(this::onGameOver);
        scoreManager.setOnScoreChanged(this::updateHUD);
        scoreManager.setOnLevelUp(this::onLevelUp);

        BorderPane root = buildLayout();

        canvas.setFocusTraversable(true);
        canvas.setOnKeyPressed(e -> {
            e.consume();
            handleKey(e.getCode());
        });

        render();
        startGameLoop();
        musicPlayer.playLoop("/audio/snake.mp3");

        root.setOnMouseClicked(event -> canvas.requestFocus());
        return root;
    }


    // UI setup

    private BorderPane buildLayout() {

        HBox hud = new HBox();
        hud.setPrefHeight(52);
        hud.setPadding(new Insets(0, 20, 0, 20));
        hud.setAlignment(Pos.CENTER);
        hud.setStyle("-fx-background-color: white; -fx-border-color: #E8E8E8; -fx-border-width: 0 0 1 0;");

        lblScore = hudNumber("0");
        lblBest = hudNumber(String.valueOf(scoreManager.getHighScore()));
        lblLevel = hudNumber("1");

        VBox scoreCell = hudCell("SCORE", lblScore);
        VBox bestCell = hudCell("BEST", lblBest);
        VBox levelCell = hudCell("LEVEL", lblLevel);

        Region left = new Region(); HBox.setHgrow(left, Priority.ALWAYS);
        Region right = new Region(); HBox.setHgrow(right, Priority.ALWAYS);

        btnStart = new Button("START");
        styleBtn(btnStart);

        btnStart.setOnAction(e -> {
            GameState s = controller.getState();

            if (s == GameState.READY || s == GameState.GAME_OVER) {
                controller.startGame();
                btnStart.setText("PAUSE");
            } else if (s == GameState.RUNNING) {
                controller.togglePause();
                btnStart.setText("RESUME");
            } else if (s == GameState.PAUSED) {
                controller.togglePause();
                btnStart.setText("PAUSE");
            }
            canvas.requestFocus();
        });

        hud.getChildren().addAll(
                left,
                scoreCell, gap(36),
                bestCell, gap(36),
                levelCell,
                right,
                btnStart
        );

        canvas = new Canvas(CANVAS_W, CANVAS_H);

        BorderPane root = new BorderPane();
        root.setTop(hud);
        root.setCenter(canvas);
        root.setStyle("-fx-background-color: #FAFAFA;");
        return root;
    }

    private VBox hudCell(String title, Label value) {
        Label t = new Label(title);
        t.setFont(Font.font(10));
        t.setTextFill(TEXT_LIGHT);

        VBox box = new VBox(2, t, value);
        box.setAlignment(Pos.CENTER);
        return box;
    }

    private Label hudNumber(String text) {
        Label l = new Label(text);
        l.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        l.setTextFill(TEXT_DARK);
        return l;
    }

    private Region gap(double w) {
        Region r = new Region();
        r.setMinWidth(w);
        return r;
    }

    private void styleBtn(Button b) {
        b.setStyle(
                "-fx-background-color: transparent;" +
                        "-fx-border-color: #CCC;" +
                        "-fx-border-radius: 20;" +
                        "-fx-padding: 5 14;" +
                        "-fx-text-fill: #666;"
        );
    }

    //the loop

    private void startGameLoop() {
        gameLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {

                if (!particles.isEmpty()) {
                    tickParticles();
                    render();
                }

                long interval = (long) scoreManager.getTickIntervalMs() * 1_000_000L;

                if (now - lastTickNs >= interval) {
                    lastTickNs = now;

                    if (controller.getState() != GameState.RUNNING) return;

                    prevFoodPos = controller.getFood().getPosition();

                    snakeHue = (snakeHue + 4) % 360;
                    controller.tick();

                    Point newFood = controller.getFood().getPosition();
                    if (!prevFoodPos.equals(newFood)) {
                        spawnParticles(prevFoodPos);
                    }
                }
            }
        };
        gameLoop.start();
    }

    private void handleKey(KeyCode code) {
        switch (code) {
            case UP -> controller.changeDirection(Direction.UP);
            case DOWN -> controller.changeDirection(Direction.DOWN);
            case LEFT -> controller.changeDirection(Direction.LEFT);
            case RIGHT -> controller.changeDirection(Direction.RIGHT);

            case ESCAPE -> {
                if (controller.getState() == GameState.RUNNING)
                    controller.togglePause();
                else if (controller.getState() == GameState.PAUSED)
                    controller.togglePause();
                else
                    controller.startGame();
            }

            case R -> restart();
        }
    }

    private void restart() {
        particles.clear();
        musicPlayer.playLoop("/audio/snake.mp3");
        controller.startGame();
        btnStart.setText("PAUSE");
    }

    // Render

    private void render() {
        GraphicsContext gc = canvas.getGraphicsContext2D();

        gc.setFill(BG);
        gc.fillRect(0, 0, CANVAS_W, CANVAS_H);

        gc.setStroke(GRID_COLOR);
        for (int i = 0; i <= COLS; i++)
            gc.strokeLine(i * CELL, 0, i * CELL, CANVAS_H);
        for (int j = 0; j <= ROWS; j++)
            gc.strokeLine(0, j * CELL, CANVAS_W, j * CELL);

        drawFood(gc);
        drawSnake(gc);
        drawParticles(gc);
        drawOverlay(gc);
    }

    private void drawFood(GraphicsContext gc) {
        Point p = controller.getFood().getPosition();
        gc.setFill(FOOD_RED);
        gc.fillOval(p.getX() * CELL + 6, p.getY() * CELL + 6, CELL - 12, CELL - 12);
    }

    private void drawSnake(GraphicsContext gc) {
        List<Point> s = controller.getSnake().getSegments();
        for (int i = 0; i < s.size(); i++) {
            Point p = s.get(i);
            gc.setFill(Color.hsb(snakeHue, 0.7, 0.8));
            gc.fillRoundRect(p.getX() * CELL + 2, p.getY() * CELL + 2, CELL - 4, CELL - 4, 8, 8);
        }
    }

    private void drawOverlay(GraphicsContext gc) {
        if (controller.getState() == GameState.RUNNING) return;

        gc.setFill(Color.color(1,1,1,0.85));
        gc.fillRect(0,0,CANVAS_W,CANVAS_H);

        gc.setFill(TEXT_DARK);
        gc.setTextAlign(TextAlignment.CENTER);

        if (controller.getState() == GameState.READY) {
            gc.fillText("Press START", CANVAS_W/2, CANVAS_H/2);
        } else if (controller.getState() == GameState.GAME_OVER) {
            gc.fillText("Game Over", CANVAS_W/2, CANVAS_H/2 - 20);
            gc.fillText("Press R to restart", CANVAS_W/2, CANVAS_H/2 + 10);
        }
    }


    private void updateHUD() {
        lblScore.setText(String.valueOf(scoreManager.getCurrentScore()));
        lblBest.setText(String.valueOf(scoreManager.getHighScore()));
        lblLevel.setText(String.valueOf(scoreManager.getLevel()));
    }

    private void onGameOver() {
        musicPlayer.stop();
        btnStart.setText("START");

        if (managerController != null && managerController.getCurrentUser() != null) {
            managerController.getHighScoreRepository().recordScore(
                    GameType.SNAKE,
                    managerController.getCurrentUser(),
                    scoreManager.getCurrentScore()
            );
        }
    }

    private void onLevelUp() {}

   // â”€â”€ Particle â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€

    private void spawnParticles(Point p) {}

    private void tickParticles() {}

    private void drawParticles(GraphicsContext gc) {}

    private static class Particle {
        double x,y,vx,vy,alpha,radius;
        Color color;

        Particle(double x,double y,double vx,double vy,double r,Color c){
            this.x=x;this.y=y;this.vx=vx;this.vy=vy;this.radius=r;this.color=c;
            alpha=1;
        }

        void update(){
            x+=vx;y+=vy;alpha-=0.03;radius*=0.97;
        }
    }
}


