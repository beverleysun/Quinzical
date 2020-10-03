package quinzical.controllers.play;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import quinzical.TTS;

public class IncorrectController extends ConfirmController {

    @FXML
    private Label answerLabel;

    private String _answer;

    public IncorrectController(String answer) {
        super();
        _answer =  answer;
    }

    @FXML
    public void initialize() {
        super.initialize();
        answerLabel.setText("The correct answer was " + _answer);
        TTS.getInstance().speak("Oops. The correct answer was " + _answer);
    }

    @FXML
    public void toQuestionBoard(MouseEvent e) {
        super.toQuestionBoard(e);
    }
}
