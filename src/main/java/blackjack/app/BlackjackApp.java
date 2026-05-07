package blackjack.app;

import blackjack.controller.BlackjackController;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import manager.GameManagerController;

public class BlackjackApp extends Application {
    @Override
    public void start(Stage stage) {
        GameManagerController managerController = new GameManagerController(stage);
        BlackjackController controller = new BlackjackController(managerController);

        stage.setTitle("Blackjack");
        stage.setScene(new Scene(controller.createView(), 900, 700));
        stage.show();
    }
}
