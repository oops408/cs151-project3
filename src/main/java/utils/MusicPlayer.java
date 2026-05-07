package utils;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.net.URL;

public class MusicPlayer {
    private static MediaPlayer currentPlayer;

    public void playLoop(String resourcePath) {
        stopAll();

        URL url = getClass().getResource(resourcePath);
        if (url == null) {
            return;
        }

        Media media = new Media(url.toExternalForm());
        currentPlayer = new MediaPlayer(media);
        currentPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        currentPlayer.play();
    }

    public void stop() {
        stopAll();
    }

    public static void stopAll() {
        if (currentPlayer != null) {
            currentPlayer.stop();
            currentPlayer.dispose();
            currentPlayer = null;
        }
    }
}