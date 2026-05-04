package app;

import manager.GameManagerController;
import javafx.application.Application;
import javafx.stage.Stage;

public class GameManagerApp extends Application {
    @Override
    public void start(Stage stage) {
        GameManagerController controller = new GameManagerController(stage);
        controller.showLoginScene();
        stage.setTitle("CS151 Project 3 - Game Manager");
        stage.setWidth(1200);
        stage.setHeight(800);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
