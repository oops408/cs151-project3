package blackjack.controller;

import blackjack.model.BlackjackGame;
import blackjack.model.Player;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import manager.GameManagerController;
import utils.CryptoUtils;

public class BlackjackController {
    private final BlackjackGame game;

    private Label turnLabel;
    private Label statusLabel;
    private Label resultLabel;
    private VBox playerArea;
    private TextField betField;
    private TextField loadField;
    private TextArea saveStateArea;
    private Button hitButton;
    private Button standButton;

    public BlackjackController(GameManagerController managerController) {
        this.game = new BlackjackGame();
    }

    public Parent createView() {
        BorderPane screen = new BorderPane();
        screen.setPadding(new Insets(20));

        VBox root = new VBox(14);
        root.setAlignment(Pos.TOP_CENTER);

        Label title = new Label("Blackjack Main Menu");
        title.getStyleClass().add("title-label");

        Label directions = new Label("Start a new game by entering a bet, or load a saved game using a saveStateString.");
        directions.setWrapText(true);

        turnLabel = new Label();
        turnLabel.getStyleClass().add("section-title");

        statusLabel = new Label();
        statusLabel.setWrapText(true);

        resultLabel = new Label();
        resultLabel.setWrapText(true);
        resultLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-padding: 8px;");

        betField = new TextField("50");
        betField.setPromptText("Bet amount");
        betField.setMaxWidth(160);

        Button startButton = new Button("Start New Game / Start Round");
        hitButton = new Button("Hit");
        standButton = new Button("Stand");
        Button saveButton = new Button("Save State");

        loadField = new TextField();
        loadField.setPromptText("Paste saveStateString here");
        loadField.setPrefWidth(520);

        Button loadButton = new Button("Load Game");

        saveStateArea = new TextArea();
        saveStateArea.setPromptText("Your encrypted saveStateString will appear here after clicking Save State.");
        saveStateArea.setWrapText(true);
        saveStateArea.setPrefRowCount(3);
        saveStateArea.setEditable(true);

        HBox actionRow = new HBox(10, new Label("Bet:"), betField, startButton, hitButton, standButton, saveButton);
        actionRow.setAlignment(Pos.CENTER);

        HBox loadRow = new HBox(10, loadField, loadButton);
        loadRow.setAlignment(Pos.CENTER);

        playerArea = new VBox(10);
        playerArea.setFillWidth(true);

        startButton.setOnAction(event -> {
            startRound();
            refresh();
        });

        hitButton.setOnAction(event -> {
            game.humanHit();
            playAutomatedTurnsIfNeeded();
            refresh();
        });

        standButton.setOnAction(event -> {
            game.humanStand();
            playAutomatedTurnsIfNeeded();
            refresh();
        });

        saveButton.setOnAction(event -> {
            String rawSaveState = game.makeSaveString();
            String encryptedSaveState = CryptoUtils.encrypt(rawSaveState);
            saveStateArea.setText(encryptedSaveState);
            statusLabel.setText("Encrypted saveStateString generated. Copy it to reload this exact Blackjack state later.");
            refresh();
        });

        loadButton.setOnAction(event -> {
            loadGameFromField();
            refresh();
        });

        root.getChildren().addAll(
                title,
                directions,
                turnLabel,
                statusLabel,
                resultLabel,
                actionRow,
                loadRow,
                saveStateArea,
                playerArea
        );

        screen.setCenter(root);
        refresh();
        return screen;
    }

    private void startRound() {
        try {
            int bet = Integer.parseInt(betField.getText().trim());
            game.startNewRound(bet);
        } catch (NumberFormatException ex) {
            game.startNewRound(0);
        }
        playAutomatedTurnsIfNeeded();
    }

    private void loadGameFromField() {
        String input = loadField.getText();

        if (input == null || input.trim().isEmpty()) {
            statusLabel.setText("Paste a saveStateString before loading.");
            return;
        }

        String saveText = input.trim();

        try {
            saveText = CryptoUtils.decrypt(saveText);
        } catch (RuntimeException ex) {
            // Allow loading older/plain save strings too, but encrypted strings are preferred.
        }

        boolean loaded = game.loadFromString(saveText);
        statusLabel.setText(loaded ? "Game loaded from saveStateString." : game.getMessage());
    }

    private void playAutomatedTurnsIfNeeded() {
        while (game.isRoundGoing() && !game.isHumanTurn()) {
            game.playOneComputerTurn();
        }
    }

    private void refresh() {
        if (turnLabel == null || playerArea == null) {
            return;
        }

        turnLabel.setText("Current Turn: " + game.getTurnName());
        statusLabel.setText(game.getMessage());
        updateResultBanner();

        hitButton.setDisable(!game.isHumanTurn());
        standButton.setDisable(!game.isHumanTurn());

        playerArea.getChildren().clear();
        playerArea.getChildren().add(createPlayerBox("Human Player", game.getHumanPlayer(), false));
        playerArea.getChildren().add(createPlayerBox("Computer Player 1", game.getComputerOne(), false));
        playerArea.getChildren().add(createPlayerBox("Computer Player 2", game.getComputerTwo(), false));
        playerArea.getChildren().add(createPlayerBox("Dealer", game.getDealer(), game.shouldHideDealerCard()));
    }

    private void updateResultBanner() {
        if (resultLabel == null) {
            return;
        }

        if (game.isRoundGoing()) {
            resultLabel.setText("");
            resultLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-padding: 8px;");
            return;
        }

        int humanValue = game.getHumanPlayer().getHand().getBestValue();
        int dealerValue = game.getDealer().getHand().getBestValue();

        if (game.getHumanPlayer().getHand().getSize() == 0 || game.getDealer().getHand().getSize() == 0) {
            resultLabel.setText("");
            resultLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-padding: 8px;");
            return;
        }

        if (game.getHumanPlayer().getHand().isBust()) {
            resultLabel.setText("YOU LOST: You busted with " + humanValue + ".");
            resultLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-padding: 8px; -fx-text-fill: darkred;");
        } else if (game.getDealer().getHand().isBust()) {
            resultLabel.setText("YOU WON: Dealer busted. Your hand: " + humanValue + ".");
            resultLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-padding: 8px; -fx-text-fill: darkgreen;");
        } else if (humanValue > dealerValue) {
            resultLabel.setText("YOU WON: " + humanValue + " beats dealer's " + dealerValue + ".");
            resultLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-padding: 8px; -fx-text-fill: darkgreen;");
        } else if (humanValue == dealerValue) {
            resultLabel.setText("PUSH: You tied the dealer at " + humanValue + ".");
            resultLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-padding: 8px; -fx-text-fill: goldenrod;");
        } else {
            resultLabel.setText("YOU LOST: Dealer's " + dealerValue + " beats your " + humanValue + ".");
            resultLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-padding: 8px; -fx-text-fill: darkred;");
        }
    }
    private GridPane createPlayerBox(String title, Player player, boolean hideDealerCard) {
        GridPane box = new GridPane();
        box.setHgap(12);
        box.setVgap(6);
        box.setPadding(new Insets(12));
        box.getStyleClass().add("card");

        Label name = new Label(title);
        name.getStyleClass().add("section-title");

        Label cards = new Label(formatHand(player, hideDealerCard));
        cards.setWrapText(true);

        Label value = new Label(hideDealerCard ? "Value: ?" : "Value: " + player.getHand().getBestValue());
        Label money = new Label("Money: " + player.getMoney());
        Label bet = new Label("Current Bet: " + player.getBet());

        box.add(name, 0, 0);
        box.add(cards, 0, 1);
        box.add(value, 1, 1);
        box.add(money, 0, 2);
        box.add(bet, 1, 2);

        return box;
    }

    private String formatHand(Player player, boolean hideDealerCard) {
        int size = player.getHand().getSize();

        if (size == 0) {
            return "Cards: none";
        }

        StringBuilder text = new StringBuilder("Cards: ");

        for (int i = 0; i < size; i++) {
            if (hideDealerCard && i == 1) {
                text.append("[Hidden Card]");
            } else {
                text.append(player.getHand().getCard(i));
            }

            if (i < size - 1) {
                text.append(", ");
            }
        }

        return text.toString();
    }
}



