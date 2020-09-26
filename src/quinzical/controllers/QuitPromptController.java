package quinzical.controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import quinzical.SceneChanger;

import java.io.IOException;

public class QuitPromptController {

    public void noQuit(MouseEvent e) throws IOException {
        // Go back to start page
        Parent quit = FXMLLoader.load(getClass().getResource("../scenes/StartPage.fxml"));
        SceneChanger.changeScene(e, quit);
    }

    public void yesQuit(MouseEvent e) {
        // Close the game
        Stage window = (Stage) ((Node)e.getSource()).getScene().getWindow();
        window.close();
    }
}
