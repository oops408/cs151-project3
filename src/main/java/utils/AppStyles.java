package utils;

import javafx.scene.Scene;

public final class AppStyles {
    private AppStyles() {
    }

    public static void apply(Scene scene) {
        String stylesheet = AppStyles.class.getResource("/styles/app.css").toExternalForm();
        scene.getStylesheets().add(stylesheet);
    }
}
