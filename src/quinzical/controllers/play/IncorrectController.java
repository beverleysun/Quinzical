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

    /* This method will control the incorrect scene. It will display and read the answer */
    @FXML
    public void initialize() {
        super.initialize();
        answerLabel.setText("The correct answer was " + _answer);
        TTS.getInstance().speak("Oops. The correct answer was " + _answer);
    }

    /* This method is invoked when the user click the next button in incorrect scene.
     * It will switch to the question selection interface. */
    @FXML
    public void toQuestionBoard(MouseEvent e) {
        super.toQuestionBoard(e);
    }
}
