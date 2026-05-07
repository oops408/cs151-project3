package manager;

import blackjack.controller.BlackjackController;
import common.GameType;
import persistence.AccountRepository;
import persistence.HighScoreRepository;
import snake.SnakeController;
import utils.AppStyles;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.List;

public class GameManagerController {
    private static final int WINDOW_WIDTH = 1200;
    private static final int WINDOW_HEIGHT = 800;

    private final Stage stage;
    private final AccountRepository accountRepository;
    private final HighScoreRepository highScoreRepository;
    private String currentUser;

    public GameManagerController(Stage stage) {
        this.stage = stage;
        this.accountRepository = new AccountRepository();
        this.highScoreRepository = new HighScoreRepository();
    }

    public String getCurrentUser() {
        return currentUser;
    }

    public HighScoreRepository getHighScoreRepository() {
        return highScoreRepository;
    }

    public void showLoginScene() {
        VBox form = new VBox(14);
        form.getStyleClass().add("card");
        form.setMaxWidth(420);
        form.setAlignment(Pos.CENTER);

        Label title = new Label("CS151 Game Manager");
        title.getStyleClass().add("title-label");

        Label directions = new Label("Log in or create an account to play Blackjack and Snake.");
        directions.setWrapText(true);

        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        Label status = new Label();
        status.getStyleClass().add("warning-text");

        Button loginButton = new Button("Log In");
        loginButton.getStyleClass().add("primary-button");
        Button createButton = new Button("Create Account");

        loginButton.setOnAction(event -> {
            String username = usernameField.getText().trim();
            String password = passwordField.getText();
            if (accountRepository.validateLogin(username, password)) {
                currentUser = username;
                showMainMenu();
            } else {
                status.setText("Invalid username or password.");
            }
        });

        createButton.setOnAction(event -> {
            String username = usernameField.getText().trim();
            String password = passwordField.getText();
            if (username.isBlank() || password.isBlank()) {
                status.setText("Username and password cannot be blank.");
                return;
            }
            boolean created = accountRepository.createAccount(username, password);
            status.setText(created ? "Account created. Please log in." : "Username already exists.");
        });

        HBox buttons = new HBox(10, loginButton, createButton);
        buttons.setAlignment(Pos.CENTER);
        form.getChildren().addAll(title, directions, usernameField, passwordField, buttons, status);

        StackPane root = new StackPane(form);
        root.setPadding(new Insets(30));
        setScene(root);
    }

    public void showMainMenu() {
        BorderPane root = new BorderPane();
        root.setTop(ToolbarFactory.create(this));
        root.setPadding(new Insets(20));

        Label title = new Label("Main Menu");
        title.getStyleClass().add("title-label");

        VBox scoresPane = new VBox(14);
        scoresPane.getChildren().addAll(
                createScoreBox("Blackjack Top 5", highScoreRepository.getTopFive(GameType.BLACKJACK)),
                createScoreBox("Snake Top 5", highScoreRepository.getTopFive(GameType.SNAKE))
        );

        VBox launcherPane = new VBox(16);
        launcherPane.getStyleClass().add("card");
        launcherPane.setAlignment(Pos.CENTER);
        launcherPane.setPrefWidth(420);

        Label welcome = new Label("Welcome, " + currentUser + "!");
        welcome.getStyleClass().add("section-title");

        Button blackjackButton = fullWidthButton("Open Blackjack");
        Button snakeButton = fullWidthButton("Open Snake");
        Button futureOne = fullWidthButton("Future Game Slot 1");
        Button futureTwo = fullWidthButton("Future Game Slot 2");
        futureOne.setDisable(true);
        futureTwo.setDisable(true);

        blackjackButton.setOnAction(event -> openBlackjack());
        snakeButton.setOnAction(event -> openSnake());
        launcherPane.getChildren().addAll(welcome, blackjackButton, snakeButton, futureOne, futureTwo);

        root.setLeft(scoresPane);
        root.setCenter(launcherPane);
        root.setTop(new VBox(ToolbarFactory.create(this), title));
        BorderPane.setMargin(scoresPane, new Insets(20, 25, 0, 0));
        BorderPane.setMargin(launcherPane, new Insets(20, 0, 0, 0));
        setScene(root);
    }

    private Button fullWidthButton(String text) {
        Button button = new Button(text);
        button.setMaxWidth(Double.MAX_VALUE);
        return button;
    }

    private VBox createScoreBox(String title, List<ScoreRecord> scores) {
        VBox box = new VBox(8);
        box.getStyleClass().add("card");
        box.setPrefWidth(360);

        Label titleLabel = new Label(title);
        titleLabel.getStyleClass().add("section-title");
        box.getChildren().add(titleLabel);

        if (scores.isEmpty()) {
            box.getChildren().add(new Label("No scores yet."));
        } else {
            for (int i = 0; i < scores.size(); i++) {
                ScoreRecord score = scores.get(i);
                Label row = new Label((i + 1) + ". " + score.getUsername() + " - " + score.getScore());
                row.getStyleClass().add("score-row");
                box.getChildren().add(row);
            }
        }
        return box;
    }

    public void openBlackjack() {
        BlackjackController controller = new BlackjackController(this);
        setSceneWithToolbar(controller.createView());
    }

    public void openSnake() {
        SnakeController controller = new SnakeController(this);
        setSceneWithToolbar(controller.createView());
    }

    private void setSceneWithToolbar(Parent content) {
        BorderPane root = new BorderPane();
        root.setTop(ToolbarFactory.create(this));
        root.setCenter(content);
        root.setPadding(new Insets(20));
        setScene(root);
    }

    private void setScene(Parent root) {
        Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
        AppStyles.apply(scene);
        stage.setScene(scene);
    }
}

