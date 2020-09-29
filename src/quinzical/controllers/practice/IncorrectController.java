package quinzical.controllers.practice;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import quinzical.SceneChanger;
import quinzical.TTS;
import quinzical.controllers.VoiceSpeedChangeable;

import java.io.IOException;
import java.util.Arrays;

public class IncorrectController extends VoiceSpeedChangeable {

    @FXML
    private Button next;
    @FXML
    private Label clueLabel;
    @FXML
    private Label TopLabel;
    @FXML
    private Label BottomLabel;

    @FXML
    public void initialize() {
        super.initialize();
        TopLabel.setText("Oops, the answer to");
        clueLabel.setText("\"" + PracticeController.getClue() + "\"" + " was");
        String answerTemp = Arrays.toString(PracticeController.getAnswer());
        String answer = answerTemp.substring(1,answerTemp.length()-1);
        BottomLabel.setText(answer);
        TTS.getInstance().speak("Oops, the answer to " +PracticeController.getClue() + " was" + answer);
    }

    @FXML
    private void backToQuestion(MouseEvent e) throws IOException {

        if(AnswerQuestionController.get_unattemptedTime() > 1 && !AnswerQuestionController.getResult()) {
            Parent answer = FXMLLoader.load(getClass().getResource("../../scenes/practice/AnswerQuestion.fxml"));
            SceneChanger.changeScene(e, answer);
        }
        else {
            AnswerQuestionController.skipQuestion(e);
        }
    }
}