package quinzical.controllers.play;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import quinzical.model.Database;
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

    @FXML private Label winnings;
    private static int prevNumCategoriesCompleted;

    /**
     *  Initializes the scene
     */
    @FXML
    public void initialize() {
        super.initialize();
        TTS.getInstance().killCurrentProcess();
        winnings.setText("$" + Database.getInstance().getWinnings());
    }

    /**
     * This method is invoked when user click the next button in the incorrect/correct scene.
     * It will switch to the question selection interface.
     * @param e the source of the click
     */
    @FXML
    public void toQuestionBoard(MouseEvent e){
        try {
            if (Database.getInstance().gameCompleted()) {
                Parent gameCompleted = FXMLLoader.load(getClass().getResource("/quinzical/scenes/play/GameCompleted.fxml"));
                SceneChanger.changeScene(e, gameCompleted);
            } else if (Database.getInstance().getNumCategoriesCompleted() == 2 && prevNumCategoriesCompleted == 1) {
                Parent gameCompleted = FXMLLoader.load(getClass().getResource("/quinzical/scenes/play/UnlockedInternational.fxml"));
                SceneChanger.changeScene(e, gameCompleted);
            } else {
                Parent play = FXMLLoader.load(getClass().getResource("/quinzical/scenes/play/Play.fxml"));
                SceneChanger.changeScene(e, play);
            }
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    /**
     * Sets the number of categories that were completed before this current question was answered
     * @param i the number of completed categories
     */
    public static void setPrevNumCategoriesCompleted(int i) {
        prevNumCategoriesCompleted = i;
    }
}