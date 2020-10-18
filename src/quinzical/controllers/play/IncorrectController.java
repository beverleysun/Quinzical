package quinzical.controllers.play;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import quinzical.model.TTS;

public class IncorrectController extends ConfirmController {

    @FXML
    private Label answerLabel;

    private String _answer;

    /**
     * Takes in the question answer so it can speak it
     * @param answer the answer to the question
     */
    public IncorrectController(String answer) {
        super();
        _answer =  answer;
    }

    /**
     * Initialize the "incorrect" scene
     */
    @FXML
    public void initialize() {
        super.initialize();
        answerLabel.setText("The correct answer was " + _answer);
        TTS.getInstance().speak("Oops. The correct answer was " + _answer);
    }

    /**
     * This method is invoked when the user click the next button in incorrect scene.
     * It will switch to the question selection interface.
     * @param e the source of the click
     */
    @FXML
    public void toQuestionBoard(MouseEvent e) {
        super.toQuestionBoard(e);
    }
}
