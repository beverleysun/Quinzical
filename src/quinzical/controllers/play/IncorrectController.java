package quinzical.controllers.play;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import quinzical.model.TTS;

/**
 * Controls the scene when the user gets the answer correct
 *
 * @author Beverley Sun, Jinkai Zhang
 */
public class IncorrectController extends ConfirmController {

    @FXML private Label answerLabel;
    private final String answer;

    /**
     * Takes in the question answer so it can speak it
     * @param answer the answer to the question
     */
    public IncorrectController(String answer) {
        super();
        this.answer =  answer;
    }

    /**
     * Initialize the "incorrect" scene
     */
    @FXML
    public void initialize() {
        super.initialize();
        answerLabel.setText("The correct answer was " + answer);
        TTS.getInstance().speak("Oops. The correct answer was " + answer);
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
