package common;

import javafx.scene.Parent;

public interface RenderableGame {
    Parent createView();
    String getTitle();
}
