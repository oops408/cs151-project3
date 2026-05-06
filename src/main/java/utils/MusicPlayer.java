package utils;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.net.URL;

public class MusicPlayer {
    private MediaPlayer mediaPlayer;

    public void playLoop(String resourcePath) {
        stop();
        URL url = getClass().getResource(resourcePath);
        if (url == null) {
            return;
        }
        Media media = new Media(url.toExternalForm());
        mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        mediaPlayer.play();
    }

    public void stop() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
    }
}
