package quinzical.controllers.practice;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.input.MouseEvent;
import quinzical.model.SceneChanger;
import quinzical.controllers.VoiceSettingsChangeable;
import quinzical.model.TTS;

import java.io.IOException;

/**
 * Controls the scenes after the user answers (both incorrectly and correctly)
 *
 * @author Beverley Sun, Jinkai Zhang
 */
public abstract class ConfirmController extends VoiceSettingsChangeable {

    /**
     * Initializes the scene.
     */
    @FXML
    public void initialize() {
        super.initialize();
        TTS.getInstance().killCurrentProcess();
    }

    /** This method is invoked in correct/incorrect controller.
     * It will switch to the question selection interface.
     * @param e the source of the click
     */
    public void backToPractice(MouseEvent e) {
        try {
            Parent practice = FXMLLoader.load(AnswerQuestionController.class.getResource("/quinzical/scenes/practice/Practice.fxml"));
            SceneChanger.changeScene(e, practice);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
}