package blackjack.controller;

import blackjack.model.BlackjackGameState;
import blackjack.model.BlackjackParticipant;
import blackjack.model.Card;
import blackjack.model.Suit;
import common.BaseGameController;
import common.GameType;
import manager.GameManagerController;
import manager.ToolbarFactory;
import persistence.BlackjackSaveService;
import utils.MusicPlayer;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class BlackjackController extends BaseGameController {
    private BlackjackGameState gameState;
    private final BlackjackSaveService saveService;
    private final MusicPlayer musicPlayer;
    private BorderPane root;
    private VBox centerPane;
    private Label statusLabel;
    private TextArea saveOutput;

    public BlackjackController(GameManagerController manager) {
        super(manager);
        this.gameState = new BlackjackGameState(manager.getCurrentUser());
        this.saveService = new BlackjackSaveService();
        this.musicPlayer = new MusicPlayer();
    }

    @Override
    public String getTitle() {
        return "Blackjack";
    }

    @Override
    public void resetGame() {
        gameState = new BlackjackGameState(manager.getCurrentUser());
        showBlackjackMenu();
    }

    @Override
    public Parent createView() {
        musicPlayer.playLoop("/audio/blackjack.mp3");
        root = new BorderPane();
        root.setTop(ToolbarFactory.create(manager));
        root.setPadding(new Insets(18));

        centerPane = new VBox(15);
        centerPane.setAlignment(Pos.TOP_CENTER);
        statusLabel = new Label();
        statusLabel.getStyleClass().add("game-message");

        showBlackjackMenu();
        root.setCenter(centerPane);
        return root;
    }

    private void showBlackjackMenu() {
        Label title = new Label("Blackjack");
        title.getStyleClass().add("title-label");

        Label instructions = new Label("Start a new game, choose a bet on the table screen, or paste an encrypted saveStateString to continue.");
        instructions.setWrapText(true);

        TextArea loadArea = new TextArea();
        loadArea.setPromptText("Paste saveStateString here");
        loadArea.setPrefRowCount(5);
        loadArea.setMaxWidth(800);

        Button newGameButton = new Button("Start New Game");
        newGameButton.getStyleClass().add("primary-button");
        Button loadGameButton = new Button("Load Game");
        Label loadStatus = new Label();
        loadStatus.getStyleClass().add("warning-text");

        // A new Blackjack game opens the table first. The user chooses the first bet there.
        newGameButton.setOnAction(e -> {
            gameState = new BlackjackGameState(manager.getCurrentUser());
            refreshGameBoard();
        });

        loadGameButton.setOnAction(e -> {
            try {
                gameState = saveService.load(loadArea.getText().trim(), manager.getCurrentUser());
                refreshGameBoard();
            } catch (RuntimeException ex) {
                loadStatus.setText("Could not load that save string. Please check the text and try again.");
            }
        });

        VBox menuCard = new VBox(12, title, instructions, newGameButton, loadArea, loadGameButton, loadStatus);
        menuCard.getStyleClass().add("card");
        menuCard.setMaxWidth(850);
        centerPane.getChildren().setAll(menuCard);
    }

    private void refreshGameBoard() {
        VBox tableArea = new VBox(12);
        tableArea.setAlignment(Pos.CENTER_LEFT);
        tableArea.getStyleClass().add("card");
        tableArea.setMaxWidth(1000);

        Label title = new Label("Blackjack Table");
        title.getStyleClass().add("title-label");
        tableArea.getChildren().add(title);

        Label turnLabel = new Label("Current Turn: " + gameState.getActiveParticipant().getName());
        turnLabel.getStyleClass().add("section-title");
        tableArea.getChildren().add(turnLabel);

        boolean hideDealerCard = !gameState.isRoundOver();
        for (BlackjackParticipant participant : gameState.getParticipants()) {
            boolean isActive = participant == gameState.getActiveParticipant() && !gameState.isRoundOver();
            boolean hide = participant.getName().equals("Dealer") && hideDealerCard;
            tableArea.getChildren().add(createPlayerArea(participant, isActive, hide));
        }

        statusLabel.setText(gameState.getStatusMessage());

        TextField betField = new TextField("100");
        betField.setPromptText("Bet amount");
        betField.setMaxWidth(110);

        Button startRoundButton = new Button(gameState.isRoundOver() ? "Start Round" : "Round In Progress");
        startRoundButton.setOnAction(e -> startNextRound(betField));
        startRoundButton.setDisable(!gameState.isRoundOver() || gameState.isHumanOutOfMoney());

        Button hitButton = new Button("Hit");
        hitButton.getStyleClass().add("primary-button");
        Button standButton = new Button("Stand");
        Button saveButton = new Button("Save State");

        hitButton.setDisable(gameState.isRoundOver());
        standButton.setDisable(gameState.isRoundOver());

        saveOutput = new TextArea();
        saveOutput.setPromptText("Your encrypted saveStateString will appear here.");
        saveOutput.setPrefRowCount(3);
        saveOutput.setMaxWidth(1000);
        saveOutput.setWrapText(true);

        hitButton.setOnAction(e -> humanHit());
        standButton.setOnAction(e -> humanStand());
        saveButton.setOnAction(e -> saveOutput.setText(saveService.save(gameState)));

        HBox actionRow = new HBox(10, hitButton, standButton, saveButton, new Label("Bet:"), betField, startRoundButton);
        actionRow.setAlignment(Pos.CENTER);
        actionRow.getStyleClass().add("card");
        actionRow.setMaxWidth(1000);

        centerPane.getChildren().setAll(tableArea, actionRow, statusLabel, new Label("Save State String:"), saveOutput);
    }

    private void startNextRound(TextField betField) {
        try {
            int bet = Integer.parseInt(betField.getText().trim());
            gameState.startRound(bet);
            gameState.autoPlayUntilHumanNeeded();
            refreshGameBoard();
        } catch (NumberFormatException ex) {
            statusLabel.setText("Please enter a whole number for the bet.");
        }
    }

    private void humanHit() {
        gameState.hitActivePlayer();
        finishComputerTurnsIfNeeded();
    }

    private void humanStand() {
        gameState.standActivePlayer();
        finishComputerTurnsIfNeeded();
    }

    private void finishComputerTurnsIfNeeded() {
        gameState.autoPlayUntilHumanNeeded();
        if (gameState.isRoundOver()) {
            manager.getHighScoreRepository().recordScore(GameType.BLACKJACK, manager.getCurrentUser(), gameState.getHumanPlayer().getBalance());
        }
        refreshGameBoard();
    }

    private VBox createPlayerArea(BlackjackParticipant player, boolean isActive, boolean hideDealerCard) {
        VBox box = new VBox(8);
        box.getStyleClass().add("player-area");

        if (isActive) {
            box.getStyleClass().add("active-player");
        }

        Label nameLabel = new Label(player.getName());
        nameLabel.getStyleClass().add("player-name");

        Label moneyLabel = new Label("Balance: " + player.getBalance() + " | Bet: " + player.getCurrentBet());
        HBox cardsBox = new HBox(10);

        for (int i = 0; i < player.getHand().getCards().size(); i++) {
            Card card = player.getHand().getCards().get(i);
            if (hideDealerCard && i == 1) {
                cardsBox.getChildren().add(createHiddenCard());
            } else {
                cardsBox.getChildren().add(createCardView(card));
            }
        }

        box.getChildren().addAll(nameLabel, moneyLabel, cardsBox);
        return box;
    }

    private VBox createCardView(Card card) {
        VBox cardBox = new VBox(4);
        cardBox.getStyleClass().add("card-view");

        Label rankLabel = new Label(card.getRank().toString());
        Label suitLabel = new Label(getSuitSymbol(card.getSuit()));
        rankLabel.getStyleClass().add("card-rank");
        suitLabel.getStyleClass().add("card-suit");

        cardBox.getChildren().addAll(rankLabel, suitLabel);
        return cardBox;
    }

    private VBox createHiddenCard() {
        VBox cardBox = new VBox();
        cardBox.getStyleClass().add("hidden-card");
        Label label = new Label("?");
        label.getStyleClass().add("hidden-card-text");
        cardBox.getChildren().add(label);
        return cardBox;
    }

    private String getSuitSymbol(Suit suit) {
        return switch (suit) {
            case HEARTS -> "♥";
            case DIAMONDS -> "♦";
            case CLUBS -> "♣";
            case SPADES -> "♠";
        };
    }
}
