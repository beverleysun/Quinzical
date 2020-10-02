package quinzical.controllers.practice;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.input.MouseEvent;
import quinzical.SceneChanger;
import quinzical.controllers.VoiceSpeedChangeable;

import java.io.IOException;

public abstract class ConfirmController extends VoiceSpeedChangeable {

    @FXML
    public void initialize() throws IOException {
        super.initialize();
    }

    public void backToPractice(MouseEvent e) {
        try {
            Parent practice = FXMLLoader.load(AnswerQuestionController.class.getResource("../../scenes/practice/Practice.fxml"));
            SceneChanger.changeScene(e, practice);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
}