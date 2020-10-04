package quinzical.controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.input.MouseEvent;
import quinzical.Database;
import quinzical.SceneChanger;

import java.io.IOException;

public class StartPageController {

    /**
     * Loads the quit prompt when the quit button is clicked
     * @param e the event that was triggered
     */
    public void quit(MouseEvent e) {
        // Go to quit prompt
        try {
            Parent quit = FXMLLoader.load(getClass().getResource("../scenes/QuitPrompt.fxml"));
            SceneChanger.changeScene(e, quit);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    /**
     * Goes to the question board when the user clicks "play"
     * @param e the event that was triggered
     */
    public void play(MouseEvent e) {
        // Go to game module
        try {
            // Display game completed scene if all questions have been answered
            if (Database.getInstance().gameCompleted()) {
                    Parent gameCompleted = FXMLLoader.load(getClass().getResource("../scenes/play/GameCompleted.fxml"));
                    SceneChanger.changeScene(e, gameCompleted);
            } else {
                Parent play = FXMLLoader.load(getClass().getResource("../scenes/play/Play.fxml"));
                SceneChanger.changeScene(e, play);
            }
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    /**
     * Goes to the practice module when the user clicks "practice"
     * @param e the event that was triggered
     */
    public void practice(MouseEvent e) {
        // Go to practice module
        try {
            Parent play = FXMLLoader.load(getClass().getResource("../scenes/practice/Practice.fxml"));
            SceneChanger.changeScene(e, play);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
}

