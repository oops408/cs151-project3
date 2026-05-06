package manager;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;

public final class ToolbarFactory {
    private ToolbarFactory() {
    }

    public static HBox create(GameManagerController manager) {
        Button menuButton = new Button("Main Menu");
        menuButton.setOnAction(event -> manager.showMainMenu());

        Label userLabel = new Label("Logged in as: " + manager.getCurrentUser());
        userLabel.setStyle("-fx-text-fill: white; -fx-font-weight: bold;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox toolbar = new HBox(10, menuButton, spacer, userLabel);
        toolbar.getStyleClass().add("toolbar");
        toolbar.setPadding(new Insets(10));
        return toolbar;
    }
}
