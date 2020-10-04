package quinzical.controllers.practice;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import quinzical.Question;
import quinzical.TTS;

import java.util.Arrays;

public class IncorrectController extends ConfirmController {

    @FXML
    private Button next;
    @FXML
    private Label clueLabel;
    @FXML
    private Label TopLabel;
    @FXML
    private Label BottomLabel;

    private Question _question;

    /**
     * Takes in the incorrectly answered question so that the correct answer can be displayed
     * @param question the question that was incorrectly answered
     */
    public IncorrectController (Question question) {
        _question = question;
    }

    /**
     * Initializes the scene and read out the answer
     */
    @FXML
    public void initialize() {
        super.initialize();
        TopLabel.setText("Oops, the answer to");
        clueLabel.setText("\"" + _question.getQuestionStr() + "\"" + " was");

        String answerTemp = Arrays.toString(_question.getAnswers());
        String answer = answerTemp.substring(1,answerTemp.length()-1);
        BottomLabel.setText(answer);

        TTS.getInstance().speak("Oops, the answer to " + _question.getAnswers() + " was" + answer);
    }

    /**
     * This method is invoked when the user click the next button in incorrect scene.
     * It will switch to the question selection interface.
     * @param e the event that was triggered
     */
    @FXML
    private void backToQuestion(MouseEvent e) {
        backToPractice(e);
    }
}