package quinzical.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import quinzical.model.SceneChanger;

import java.io.IOException;

/**
 * Controls the scene where the user is prompted if they actually want to quit
 *
 * @author Beverley Sun, Jinkai Zhang
 */
public class QuitPromptController {

    /**
     * When the user presses no when prompted if they want to quit or not. Goes back the the start page
     * @param e the event that was triggered
     */
    @FXML
    public void noQuit(MouseEvent e) {
        // Go back to start page
        try {
            Parent quit = FXMLLoader.load(getClass().getResource("/quinzical/scenes/StartPage.fxml"));
            SceneChanger.changeScene(e, quit);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    /**
     * Triggered when the user clicks yes to quit. Quits the game
     * @param e the event that was triggered.
     */
    @FXML
    public void yesQuit(MouseEvent e) {
        // Close the game
        Stage window = (Stage) ((Node)e.getSource()).getScene().getWindow();
        window.close();
    }
}
