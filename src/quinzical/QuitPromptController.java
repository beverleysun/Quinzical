package quinzical;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class QuitPromptController {

    public void noQuit(ActionEvent e) throws IOException {
        // Go back to start page
        Parent quit = FXMLLoader.load(getClass().getResource("StartPage.fxml"));

        // Set scene width and height to the previous scene width and height
        Scene prevScene = ((Node)e.getSource()).getScene();
        Stage window = (Stage) ((Node)e.getSource()).getScene().getWindow();
        double prevSceneWidth = prevScene.getWidth();
        double prevSceneHeight = prevScene.getHeight();
        Scene startPageScene = new Scene(quit, prevSceneWidth, prevSceneHeight);

        // Show scene
        window.setScene(startPageScene);
    }

    public void yesQuit(ActionEvent e) {
        // Close the game
        Stage window = (Stage) ((Node)e.getSource()).getScene().getWindow();
        window.close();
    }
}
