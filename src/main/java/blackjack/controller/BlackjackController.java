package blackjack.controller;

import blackjack.model.BlackjackGame;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import manager.GameManagerController;

public class BlackjackController {
    private final GameManagerController managerController;
    private final BlackjackGame game;

    public BlackjackController(GameManagerController managerController) {
        this.managerController = managerController;
        this.game = new BlackjackGame();
    }

    public Parent createView() {
        VBox root = new VBox(12);
        root.setPadding(new Insets(20));

        Label title = new Label("Blackjack");
        Label status = new Label(game.getMessage());
        TextField betField = new TextField("50");
        betField.setPromptText("Bet amount");

        Button startButton = new Button("Start New Round");
        Button hitButton = new Button("Hit");
        Button standButton = new Button("Stand");
        Button backButton = new Button("Back to Main Menu");

        startButton.setOnAction(event -> {
            try {
                int bet = Integer.parseInt(betField.getText().trim());
                game.startNewRound(bet);
            } catch (NumberFormatException ex) {
                game.startNewRound(0);
            }
            status.setText(game.getTurnName() + " - " + game.getMessage());
        });

        hitButton.setOnAction(event -> {
            game.humanHit();
            status.setText(game.getTurnName() + " - " + game.getMessage());
        });

        standButton.setOnAction(event -> {
            game.humanStand();
            while (game.isRoundGoing() && !game.isHumanTurn()) {
                game.playOneComputerTurn();
            }
            status.setText(game.getTurnName() + " - " + game.getMessage());
        });

        backButton.setOnAction(event -> managerController.showMainMenu());

        root.getChildren().addAll(title, status, betField, startButton, hitButton, standButton, backButton);
        return root;
    }
}
