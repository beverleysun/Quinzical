package quinzical;

import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public abstract class ButtonHandler implements EventHandler<MouseEvent> {

    protected final Stage _stage;
    protected final Scene _scene;
    protected final SceneGenerator _sceneGenerator = SceneGenerator.getInstance();
    protected final Controller _controller = Controller.getInstance();

    public ButtonHandler(Stage stage, Scene scene) {
        _stage = stage;
        _scene = scene;
    }

}
