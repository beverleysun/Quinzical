package quinzical.controllers.practice;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import quinzical.Question;
import quinzical.TTS;

import java.io.IOException;
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

    public IncorrectController (Question question) {
        _question = question;
    }

    /* This method will control the incorrect scene. It will display and read the clue and the answer */
    @FXML
    public void initialize() {
        super.initialize();
        TopLabel.setText("Oops, the answer to");
        clueLabel.setText("\"" + _question.getQuestion() + "\"" + " was");

        String answerTemp = Arrays.toString(_question.getAnswer());
        String answer = answerTemp.substring(1,answerTemp.length()-1);
        BottomLabel.setText(answer);

        TTS.getInstance().speak("Oops, the answer to " + _question.getAnswer() + " was" + answer);
    }

    /* This method is invoked when the user click the next button in incorrect scene.
     * It will switch to the question selection interface. */
    @FXML
    private void backToQuestion(MouseEvent e) {
        backToPractice(e);
    }
}