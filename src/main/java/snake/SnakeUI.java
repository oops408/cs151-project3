package snake;

import model.Direction;
import model.Point;

import javafx.animation.AnimationTimer;
import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;


public class SnakeUI extends Application {

    //Grid 
    private static final int COLS     = 20;
    private static final int ROWS     = 20;
    private static final int CELL     = 28;
    private static final int CANVAS_W = COLS * CELL;
    private static final int CANVAS_H = ROWS * CELL;

    //Palette
    private static final Color BG         = Color.web("#FAFAFA");
    private static final Color GRID_COLOR = Color.web("#F0F0F0");
    private static final Color TEXT_DARK  = Color.web("#1A1A1A");
    private static final Color TEXT_MID   = Color.web("#888888");
    private static final Color TEXT_LIGHT = Color.web("#BBBBBB");
    private static final Color FOOD_RED   = Color.web("#FF3B30");  

    private double snakeHue = 200.0;  

    // ── Particles ─────────────────────────────────────────────────
    private final List<Particle> particles = new ArrayList<>();
    private final Random         rng       = new Random();

    // ── Game objects ──────────────────────────────────────────────
    private SnakeScoreManager scoreManager;
    private SnakeController   controller;

    // ── JavaFX nodes ──────────────────────────────────────────────
    private Canvas canvas;
    private Label  lblScore;
    private Label  lblBest;
    private Label  lblLevel;
    private Button btnPause;

    // ── Loop ──────────────────────────────────────────────────────
    private AnimationTimer gameLoop;
    private long           lastTickNs = 0;

    // ── Food-eat detection: snapshot position before each tick ────
    private Point prevFoodPos = null;

    // ─────────────────────────────────────────────────────────────
    @Override
    public void start(Stage stage) {
        scoreManager = new SnakeScoreManager();
        controller   = new SnakeController(COLS, ROWS, scoreManager);

        controller.setOnUpdate(this::render);
        controller.setOnGameOver(this::onGameOver);
        scoreManager.setOnScoreChanged(this::updateHUD);
        scoreManager.setOnLevelUp(this::onLevelUp);

        BorderPane root = buildLayout();

        Scene scene = new Scene(root, CANVAS_W, CANVAS_H + 52);
        scene.setFill(Color.WHITE);
        scene.setOnKeyPressed(e -> handleKey(e.getCode()));

        stage.setTitle("Snake");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();

        render();
        startGameLoop();
    }

    //Layout
    private BorderPane buildLayout() {
        HBox hud = new HBox();
        hud.setPrefHeight(52);
        hud.setPadding(new Insets(0, 20, 0, 20));
        hud.setAlignment(Pos.CENTER);
        hud.setStyle(
            "-fx-background-color: #FFFFFF;" +
            "-fx-border-color: #E8E8E8; -fx-border-width: 0 0 1 0;"
        );

        lblScore = hudNumber("0");
        lblBest  = hudNumber(String.valueOf(scoreManager.getHighScore()));
        lblLevel = hudNumber("1");

        VBox scoreCell = hudCell("SCORE", lblScore);
        VBox bestCell  = hudCell("BEST",  lblBest);
        VBox levelCell = hudCell("LEVEL", lblLevel);

        // Flexible spacers push score group to centre
        Region left  = new Region(); HBox.setHgrow(left,  Priority.ALWAYS);
        Region right = new Region(); HBox.setHgrow(right, Priority.ALWAYS);

        btnPause = new Button("PAUSE");
        stylePauseBtn(btnPause);
        btnPause.setOnAction(e -> {
            controller.togglePause();
            btnPause.setText(
                controller.getState() == GameState.PAUSED ? "RESUME" : "PAUSE"
            );
        });

        hud.getChildren().addAll(
            left,
            scoreCell, gap(36), bestCell, gap(36), levelCell,
            right,
            btnPause
        );

        canvas = new Canvas(CANVAS_W, CANVAS_H);

        BorderPane root = new BorderPane();
        root.setTop(hud);
        root.setCenter(canvas);
        root.setStyle("-fx-background-color: #FAFAFA;");
        return root;
    }

    /** A fixed-width invisible spacer. */
    private Region gap(double w) {
        Region r = new Region();
        r.setMinWidth(w);
        return r;
    }

    /** Caption + large number stacked vertically. */
    private VBox hudCell(String caption, Label value) {
        Label cap = new Label(caption);
        cap.setFont(Font.font("Helvetica Neue", FontWeight.NORMAL, 9));
        cap.setTextFill(TEXT_LIGHT);

        VBox box = new VBox(1, cap, value);
        box.setAlignment(Pos.CENTER);
        return box;
    }

    private Label hudNumber(String text) {
        Label lbl = new Label(text);
        lbl.setFont(Font.font("Helvetica Neue", FontWeight.BOLD, 20));
        lbl.setTextFill(TEXT_DARK);
        lbl.setMinWidth(50);
        lbl.setAlignment(Pos.CENTER);
        return lbl;
    }

    private void stylePauseBtn(Button btn) {
        String base =
            "-fx-background-color: transparent;" +
            "-fx-border-color: #CCCCCC; -fx-border-radius: 20;" +
            "-fx-background-radius: 20; -fx-text-fill: #999999;" +
            "-fx-font-family: 'Helvetica Neue'; -fx-font-size: 10;" +
            "-fx-font-weight: bold; -fx-padding: 5 14 5 14; -fx-cursor: hand;";
        String hover =
            "-fx-background-color: #F2F2F2;" +
            "-fx-border-color: #AAAAAA; -fx-border-radius: 20;" +
            "-fx-background-radius: 20; -fx-text-fill: #333333;" +
            "-fx-font-family: 'Helvetica Neue'; -fx-font-size: 10;" +
            "-fx-font-weight: bold; -fx-padding: 5 14 5 14; -fx-cursor: hand;";
        btn.setStyle(base);
        btn.setOnMouseEntered(e -> btn.setStyle(hover));
        btn.setOnMouseExited(e  -> btn.setStyle(base));
    }

    //Game loop 
    private void startGameLoop() {
        gameLoop = new AnimationTimer() {
            @Override
            public void handle(long nowNs) {
                // Redraw every frame so particles animate smoothly
                if (!particles.isEmpty()) {
                    tickParticles();
                    render();
                }

                long intervalNs = (long) scoreManager.getTickIntervalMs() * 1_000_000L;
                if (nowNs - lastTickNs >= intervalNs) {
                    lastTickNs = nowNs;

                    // Snapshot food position before tick to detect eating
                    prevFoodPos = controller.getFood().getPosition();

                    snakeHue = (snakeHue + 4) % 360;  // drift hue
                    controller.tick();

                    // If food moved, the snake just ate — spawn particles
                    Point newFoodPos = controller.getFood().getPosition();
                    if (prevFoodPos != null && !prevFoodPos.equals(newFoodPos)) {
                        spawnParticles(prevFoodPos);
                    }
                }
            }
        };
        gameLoop.start();
    }

    //Input
    private void handleKey(KeyCode code) {
        switch (code) {
            case UP,    W -> controller.changeDirection(Direction.UP);
            case DOWN,  S -> controller.changeDirection(Direction.DOWN);
            case LEFT,  A -> controller.changeDirection(Direction.LEFT);
            case RIGHT, D -> controller.changeDirection(Direction.RIGHT);
            case SPACE -> {
                GameState s = controller.getState();
                if (s == GameState.READY || s == GameState.GAME_OVER) {
                    controller.startGame();
                    btnPause.setText("PAUSE");
                } else {
                    controller.togglePause();
                    btnPause.setText(
                        controller.getState() == GameState.PAUSED ? "RESUME" : "PAUSE"
                    );
                }
            }
            case R -> restartGame();
            default -> {}
        }
    }

    //Render 
    private void render() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        drawBackground(gc);
        drawGrid(gc);
        drawFood(gc);
        drawSnake(gc);
        drawParticles(gc);
        drawOverlay(gc);
    }

    private void drawBackground(GraphicsContext gc) {
        gc.setFill(BG);
        gc.fillRect(0, 0, CANVAS_W, CANVAS_H);
    }

    private void drawGrid(GraphicsContext gc) {
        gc.setStroke(GRID_COLOR);
        gc.setLineWidth(0.5);
        for (int x = 0; x <= COLS; x++)
            gc.strokeLine(x * CELL, 0, x * CELL, CANVAS_H);
        for (int y = 0; y <= ROWS; y++)
            gc.strokeLine(0, y * CELL, CANVAS_W, y * CELL);
    }

    private void drawFood(GraphicsContext gc) {
        Point  fp = controller.getFood().getPosition();
        double cx = fp.getX() * CELL + CELL / 2.0;
        double cy = fp.getY() * CELL + CELL / 2.0;
        double r  = CELL / 2.0 - 5;

        // Soft drop shadow
        gc.setFill(Color.color(1.0, 0.23, 0.19, 0.18));
        gc.fillOval(cx - r - 1, cy - r + 2, (r + 1) * 2, (r + 1) * 2);

        // Main circle
        gc.setFill(FOOD_RED);
        gc.fillOval(cx - r, cy - r, r * 2, r * 2);

        // Specular highlight
        gc.setFill(Color.color(1, 1, 1, 0.55));
        gc.fillOval(cx - r * 0.45, cy - r * 0.55, r * 0.5, r * 0.4);
    }

    private void drawSnake(GraphicsContext gc) {
        List<Point> segs = controller.getSnake().getSegments();
        int n = segs.size();

        for (int i = 0; i < n; i++) {
            Point  p   = segs.get(i);
            double px  = p.getX() * CELL;
            double py  = p.getY() * CELL;

            // Gradient: head uses snakeHue, tail shifts +80° and desaturates
            double t    = (double) i / Math.max(n - 1, 1);
            double hue  = (snakeHue + t * 80) % 360;
            double sat  = 0.65 - t * 0.20;
            double bri  = 0.88 - t * 0.12;
            Color  col  = Color.hsb(hue, sat, bri);

            boolean isHead = (i == 0);
            double  pad    = isHead ? 1.5 : 3.0;
            double  size   = CELL - pad * 2;
            double  arc    = isHead ? 10 : 7;

            // Segment body
            gc.setFill(col);
            gc.fillRoundRect(px + pad, py + pad, size, size, arc, arc);

            // Subtle top-edge sheen for depth
            gc.setFill(Color.color(1, 1, 1, 0.22));
            gc.fillRoundRect(px + pad, py + pad, size, size * 0.38, arc, arc);

            if (isHead) drawEyes(gc, p);
        }
    }

    private void drawEyes(GraphicsContext gc, Point head) {
        Direction dir = controller.getSnake().getDirection();
        double bx  = head.getX() * CELL;
        double by  = head.getY() * CELL;
        double mid = CELL / 2.0;
        double er  = 2.2;
        double ex1, ey1, ex2, ey2;

        switch (dir) {
            case RIGHT -> { ex1 = bx+mid+4;   ey1 = by+mid-4; ex2 = bx+mid+4;   ey2 = by+mid+1; }
            case LEFT  -> { ex1 = bx+mid-6.5; ey1 = by+mid-4; ex2 = bx+mid-6.5; ey2 = by+mid+1; }
            case UP    -> { ex1 = bx+mid-4;   ey1 = by+mid-7; ex2 = bx+mid+1;   ey2 = by+mid-7; }
            default    -> { ex1 = bx+mid-4;   ey1 = by+mid+4; ex2 = bx+mid+1;   ey2 = by+mid+4; }
        }

        gc.setFill(Color.WHITE);
        gc.fillOval(ex1, ey1, er * 2, er * 2);
        gc.fillOval(ex2, ey2, er * 2, er * 2);
        gc.setFill(TEXT_DARK);
        gc.fillOval(ex1 + 1, ey1 + 1, er, er);
        gc.fillOval(ex2 + 1, ey2 + 1, er, er);
    }

    //Particles

    private void spawnParticles(Point at) {
        double cx = at.getX() * CELL + CELL / 2.0;
        double cy = at.getY() * CELL + CELL / 2.0;

        for (int i = 0; i < 20; i++) {
            double angle = 2 * Math.PI * i / 20 + rng.nextDouble() * 0.5;
            double speed = 1.6 + rng.nextDouble() * 2.8;
            double r     = 2.0 + rng.nextDouble() * 2.5;
            // Alternate between food red and current snake head hue
            Color col = (i % 2 == 0)
                ? FOOD_RED
                : Color.hsb(snakeHue, 0.7, 0.85);
            particles.add(new Particle(
                cx, cy,
                Math.cos(angle) * speed,
                Math.sin(angle) * speed,
                r, col
            ));
        }
    }

    private void tickParticles() {
        Iterator<Particle> it = particles.iterator();
        while (it.hasNext()) {
            Particle p = it.next();
            p.update();
            if (p.isDead()) it.remove();
        }
    }

    private void drawParticles(GraphicsContext gc) {
        for (Particle p : particles) {
            gc.setGlobalAlpha(p.alpha);
            gc.setFill(p.color);
            gc.fillOval(p.x - p.radius, p.y - p.radius, p.radius * 2, p.radius * 2);
        }
        gc.setGlobalAlpha(1.0);
    }

    //Overlay
    private void drawOverlay(GraphicsContext gc) {
        GameState state = controller.getState();
        if (state == GameState.RUNNING) return;

        // Frosted-glass wash
        gc.setFill(Color.color(0.98, 0.98, 0.98, 0.84));
        gc.fillRect(0, 0, CANVAS_W, CANVAS_H);

        double cx = CANVAS_W / 2.0;
        double cy = CANVAS_H / 2.0;
        gc.setTextAlign(TextAlignment.CENTER);

        switch (state) {
            case READY -> {
                overlayTitle(gc, "Snake", cx, cy - 30);
                overlayBody(gc, "Press Space to begin", cx, cy + 8);
                overlayCaption(gc, "WASD  ·  Arrows   ·   Space to pause   ·   R to restart", cx, cy + 34);
            }
            case PAUSED -> {
                overlayTitle(gc, "Paused", cx, cy - 14);
                overlayBody(gc, "Press Space to resume", cx, cy + 20);
            }
            case GAME_OVER -> {
                overlayTitle(gc, "Game Over", cx, cy - 50);

                // Big score number
                gc.setFont(Font.font("Helvetica Neue", FontWeight.BOLD, 44));
                gc.setFill(TEXT_DARK);
                gc.fillText(String.valueOf(scoreManager.getCurrentScore()), cx, cy + 14);
                overlayCaption(gc, "SCORE", cx, cy + 32);

                boolean isNewBest = scoreManager.getCurrentScore() >= scoreManager.getHighScore()
                                    && scoreManager.getCurrentScore() > 0;
                if (isNewBest) {
                    overlayAccent(gc, "✦ New best!", cx, cy + 58);
                } else {
                    overlayBody(gc, "Best  " + scoreManager.getHighScore(), cx, cy + 58);
                }
                overlayCaption(gc, "R to restart   ·   Space to play again", cx, cy + 84);
            }
        }
    }

    private void overlayTitle(GraphicsContext gc, String t, double x, double y) {
        gc.setFont(Font.font("Helvetica Neue", FontWeight.BOLD, 28));
        gc.setFill(TEXT_DARK);
        gc.fillText(t, x, y);
    }

    private void overlayBody(GraphicsContext gc, String t, double x, double y) {
        gc.setFont(Font.font("Helvetica Neue", FontWeight.NORMAL, 14));
        gc.setFill(TEXT_MID);
        gc.fillText(t, x, y);
    }

    private void overlayCaption(GraphicsContext gc, String t, double x, double y) {
        gc.setFont(Font.font("Helvetica Neue", FontWeight.NORMAL, 10));
        gc.setFill(TEXT_LIGHT);
        gc.fillText(t, x, y);
    }

    private void overlayAccent(GraphicsContext gc, String t, double x, double y) {
        gc.setFont(Font.font("Helvetica Neue", FontWeight.BOLD, 13));
        gc.setFill(FOOD_RED);
        gc.fillText(t, x, y);
    }

    //Event callbacks 
    private void updateHUD() {
        lblScore.setText(String.valueOf(scoreManager.getCurrentScore()));
        lblBest .setText(String.valueOf(scoreManager.getHighScore()));
        lblLevel.setText(String.valueOf(scoreManager.getLevel()));

        // Quick pop animation on score label
        lblScore.setScaleX(1.3);
        lblScore.setScaleY(1.3);
        PauseTransition pt = new PauseTransition(Duration.millis(120));
        pt.setOnFinished(e -> { lblScore.setScaleX(1); lblScore.setScaleY(1); });
        pt.play();
    }

    private void onLevelUp() {
        // Flash level label with current snake hue colour
        lblLevel.setTextFill(Color.hsb(snakeHue, 0.8, 0.65));
        PauseTransition pt = new PauseTransition(Duration.millis(700));
        pt.setOnFinished(e -> lblLevel.setTextFill(TEXT_DARK));
        pt.play();
    }

    private void onGameOver() {
        btnPause.setText("PAUSE");
        updateHUD();
        render();
    }

    private void restartGame() {
        particles.clear();
        controller.startGame();
        btnPause.setText("PAUSE");
        render();
    }

    // ── Entry point ───────────────────────────────────────────────
    public static void main(String[] args) {
        launch(args);
    }

    // ── Particle ──────────────────────────────────────────────────
    private static class Particle {
        double x, y, vx, vy, radius, alpha;
        Color  color;

        Particle(double x, double y, double vx, double vy, double radius, Color color) {
            this.x = x; this.y = y;
            this.vx = vx; this.vy = vy;
            this.radius = radius;
            this.alpha  = 1.0;
            this.color  = color;
        }

        void update() {
            x      += vx;
            y      += vy;
            vx     *= 0.87;   // horizontal friction
            vy     *= 0.87;   // vertical friction
            vy     += 0.10;   // gentle gravity pulls particles down
            alpha  -= 0.034;
            radius *= 0.97;
        }

        boolean isDead() { return alpha <= 0 || radius < 0.3; }
    }
}