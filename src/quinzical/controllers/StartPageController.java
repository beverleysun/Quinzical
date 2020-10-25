package quinzical.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import quinzical.model.Database;
import quinzical.model.SceneChanger;

import java.io.File;
import java.io.IOException;

public class StartPageController {

    @FXML
    private Button play;

    /**
     * Initializes the reset prompt scene
     */
    @FXML
    public void initialize() {
        if (new File("./.save/category-index/category-index").exists()) {
            play.setText("Continue");
        }
        else{
            play.setText("Play");
        }
    }


    /**
     * Loads the quit prompt when the quit button is clicked
     *
     * @param e the event that was triggered
     */
    public void quit(MouseEvent e) {
        // Go to quit prompt
        try {
            Parent quit = FXMLLoader.load(getClass().getResource("/quinzical/scenes/QuitPrompt.fxml"));
            SceneChanger.changeScene(e, quit);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    /**
     * This method is invoked when the user click the play button in StartPage scene.
     *
     * @param e the source of the click
     */
    public void play(MouseEvent e) {
        try {
            //Check the category index file, if it exists then continue the game.
            //if its not exist, then let user choose the category.
            if (new File("./.save/category-index/category-index").exists()) {
                Database.getInstance().getQuestionData();
                if (Database.getInstance().gameCompleted()) {
                    Parent gameCompleted = FXMLLoader.load(getClass().getResource("/quinzical/scenes/play/GameCompleted.fxml"));
                    SceneChanger.changeScene(e, gameCompleted);
                } else {
                    Parent play = FXMLLoader.load(getClass().getResource("/quinzical/scenes/play/Play.fxml"));
                    SceneChanger.changeScene(e, play);
                }
            } else {
                Parent play = FXMLLoader.load(getClass().getResource("/quinzical/scenes/play/SelectCategories.fxml"));
                SceneChanger.changeScene(e, play);
            }
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }

    }

    /**
     * Goes to the practice module when the user clicks "practice"
     *
     * @param e the event that was triggered
     */
    public void practice(MouseEvent e) {
        // Go to practice module
        try {
            Parent play = FXMLLoader.load(getClass().getResource("/quinzical/scenes/practice/Practice.fxml"));
            SceneChanger.changeScene(e, play);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    /**
     * Goes to the scores page
     *
     * @param e the event that was triggered
     */
    public void scores(MouseEvent e) {
        // Go to practice module
        try {
            Parent scores = FXMLLoader.load(getClass().getResource("/quinzical/scenes/Scores.fxml"));
            SceneChanger.changeScene(e, scores);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
}

