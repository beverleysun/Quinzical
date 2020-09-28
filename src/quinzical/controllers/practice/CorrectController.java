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

public class CorrectController extends VoiceSpeedChangeable {

    @FXML
    private Button next;
    @FXML
    private Label correctLabel;

    @FXML
    public void initialize() {
        super.initialize();
        if (AnswerQuestionController.getResult()) {
            correctLabel.setText("Correct!");
            TTS.getInstance().speak("Correct!");
        }
    }

    @FXML
    private void backToQuestion(MouseEvent e) throws IOException {
        AnswerQuestionController.skipQuestion(e);
    }

}