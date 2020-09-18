package quinzical;

import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.scene.control.Button;

public class ResetButtonHandler extends ButtonHandler {

    public ResetButtonHandler(Stage stage, Scene scene) {
        super(stage, scene);
    }

    @Override
    public void handle(MouseEvent event) {
        Button button = (Button) event.getSource();
        if (button.getStyleClass().contains("reset-button")) {
            Scene resetScene = _sceneGenerator.getResetScene(_stage, _scene);
            _controller.showScene(_stage, resetScene);
        } else if (button.getStyleClass().contains("yes-button")) {
            Controller.getInstance().reset();
            Scene startScene = _sceneGenerator.getStartScene(_stage, _scene);
            _controller.showScene(_stage, startScene);
        } else {
            Scene startScene = _sceneGenerator.getStartScene(_stage, _scene);
            _controller.showScene(_stage, startScene);
        }
    }
}