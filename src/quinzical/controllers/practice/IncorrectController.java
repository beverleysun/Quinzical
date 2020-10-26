package quinzical.controllers.practice;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import quinzical.model.Question;
import quinzical.model.TTS;

import java.util.Arrays;

/**
 * Controls the scene when the user gets the answer correct
 *
 * @author Beverley Sun, Jinkai Zhang
 */
public class IncorrectController extends ConfirmController {

    @FXML private Button next;
    @FXML private Label clueLabel;
    @FXML private Label TopLabel;
    @FXML private Label BottomLabel;

    private final Question question;

    /**
     * Takes in the incorrectly answered question so that the correct answer can be displayed
     * @param question the question that was incorrectly answered
     */
    public IncorrectController (Question question) {
        this.question = question;
    }

    /**
     * Initializes the scene and read out the answer
     */
    @FXML
    public void initialize() {
        super.initialize();
        TopLabel.setText("Oops, the answer to");
        clueLabel.setText("\"" + question.getQuestionStr() + "\"" + " was");

        String answerTemp = Arrays.toString(question.getAnswers());
        String answer = answerTemp.substring(1,answerTemp.length()-1);
        BottomLabel.setText(answer);

        TTS.getInstance().speak("Oops, the answer to " + question.getQuestionStr() + " was" + answer);
    }

    /**
     * This method is invoked when the user click the next button in incorrect scene.
     * It will switch to the question selection interface.
     * @param e the source of the click
     */
    @FXML
    private void backToQuestion(MouseEvent e) {
        backToPractice(e);
    }
}