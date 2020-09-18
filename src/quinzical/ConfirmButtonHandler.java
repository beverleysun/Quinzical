package quinzical;

import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class ConfirmButtonHandler extends ButtonHandler {

    private final String _realAnswer;
    private final String _givenAnswer;
    private final int _value;

    public ConfirmButtonHandler(Stage stage, Scene scene, String realAnswer, String givenAnswer, int value) {
        super(stage, scene);
        _realAnswer = realAnswer;
        _givenAnswer = givenAnswer;
        _value = value;
    }

    @Override
    public void handle(MouseEvent event) {
        validateAnswer();
    }

    public void validateAnswer() {
        Scene rightWrongScene;
        if (_realAnswer.toLowerCase().trim().equals(_givenAnswer.toLowerCase().trim())) {
            _controller.addWinnings(_value);
            rightWrongScene = _sceneGenerator.getRightWrongScene(_stage, _scene, _realAnswer, true);
            _controller.speak("Correct");
        } else {
            _controller.addWinnings(_value*-1);
            rightWrongScene = _sceneGenerator.getRightWrongScene(_stage, _scene, _realAnswer, false);
            _controller.speak("The correct answer was " + _realAnswer);
        }
        _controller.showScene(_stage, rightWrongScene);
    }
}
