package common;

import manager.GameManagerController;
import javafx.scene.Parent;

public abstract class BaseGameController implements RenderableGame {
    protected final GameManagerController manager;

    protected BaseGameController(GameManagerController manager) {
        this.manager = manager;
    }

    public abstract void resetGame();

    public abstract Parent createView();
}
