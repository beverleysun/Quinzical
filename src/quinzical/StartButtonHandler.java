package quinzical;

import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class StartButtonHandler extends ButtonHandler {

    public StartButtonHandler(Stage stage, Scene scene) {
        super(stage, scene);
    }

    @Override
    public void handle(MouseEvent event) {
        Scene questionBoardScene = _sceneGenerator.getQuestionBoardScene(_stage, _scene);
        _controller.showScene(_stage, questionBoardScene);
    }
}