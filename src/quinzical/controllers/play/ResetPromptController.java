package quinzical.controllers.play;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import quinzical.Database;
import quinzical.SceneChanger;

import java.io.IOException;

public class ResetPromptController {


    @FXML
    private Label finalScoreLabel;

    /**
     * Initializes the reset prompt scene
     */
    @FXML
    public void initialize() {
        finalScoreLabel.setText("You're final score was $" + Database.getInstance().getWinnings());
    }

    /**
     * This method is invoked when the user click the yes button in ResetPrompt scene.
     * It will call the reset method in Database. The .save file will be deleted.
     * @param e the source of the button click
     */
    public void yesReset(MouseEvent e) {
        // Reset the game
        Database.getInstance().reset();
        goToQuestionBoard(e);
    }

    /**
     * This method is invoked when the user click the yes/no button in ResetPrompt scene.
     * For no button, It will switch to the question selection interface.
     * For yes button(yesRest), It will reset the game first and then switch to the question selection interface.
     * @param e the source of the button click
     */
    public void goToQuestionBoard(MouseEvent e) {
        try {
            // Load question board scene
            Parent questionBoard = FXMLLoader.load(getClass().getResource("/quinzical/scenes/play/Play.fxml"));
            SceneChanger.changeScene(e, questionBoard);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
}
