package quinzical.controllers.play;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import quinzical.Database;
import quinzical.SceneChanger;
import quinzical.controllers.VoiceSettingsChangeable;

import java.io.IOException;

public abstract class ConfirmController extends VoiceSettingsChangeable {
    @FXML
    private Label winnings;

    /**
     *  Initializes the scene
     */
    @FXML
    public void initialize() {
        super.initialize();
        winnings.setText("$" + Database.getInstance().getWinnings());
    }

    /**
     * This method is invoked when user click the next button in the incorrect/correct scene.
     * It will switch to the question selection interface.
     * @param e the source of the click
     */
    public void toQuestionBoard(MouseEvent e){
        try {
            if (Database.getInstance().gameCompleted()) {
                Parent gameCompleted = FXMLLoader.load(getClass().getResource("/quinzical/scenes/play/GameCompleted.fxml"));
                SceneChanger.changeScene(e, gameCompleted);
            } else {
                Parent play = FXMLLoader.load(getClass().getResource("/quinzical/scenes/play/Play.fxml"));
                SceneChanger.changeScene(e, play);
            }
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

}