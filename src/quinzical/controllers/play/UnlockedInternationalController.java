package quinzical.controllers.play;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.input.MouseEvent;
import quinzical.model.SceneChanger;

import java.io.IOException;

/**
 * Controls the scene where the user has just completed two categories congratulates them for
 * unlocking the international section
 *
 * @author Beverley Sun, Jinkai Zhang
 */
public class UnlockedInternationalController {

    /**
     * Takes the player back to the question board when the user clicks the continue button
     * @param e the event that happened
     */
    @FXML
    public void toQuestionBoard(MouseEvent e) {
        try {
            Parent gameCompleted = FXMLLoader.load(getClass().getResource("/quinzical/scenes/play/Play.fxml"));
            SceneChanger.changeScene(e, gameCompleted);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
}
