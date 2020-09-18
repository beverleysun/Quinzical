package quinzical;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class BackButtonHandler extends ButtonHandler {

    public BackButtonHandler(Stage stage, Scene scene) {
        super(stage, scene);
    }

    @Override
    public void handle(MouseEvent event) {
        Button button = (Button) event.getSource();
        Scene scene;

        if (button.getId().equals("to-start")) {
            scene = _sceneGenerator.getStartScene(_stage, _scene);
        } else {
            scene = _sceneGenerator.getQuestionBoardScene(_stage, _scene);
        }
        _controller.showScene(_stage, scene);
    }
}