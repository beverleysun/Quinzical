package quinzical;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class StartPageController {

    public void quit(ActionEvent e) throws IOException {
        // Go to quit scene
        Parent quit = FXMLLoader.load(getClass().getResource("QuitPrompt.fxml"));

        // Set scene width and height to the previous scene width and height
        Scene prevScene = ((Node)e.getSource()).getScene();
        Stage window = (Stage) ((Node)e.getSource()).getScene().getWindow();
        double prevSceneWidth = prevScene.getWidth();
        double prevSceneHeight = prevScene.getHeight();
        Scene quitScene = new Scene(quit, prevSceneWidth, prevSceneHeight);

        // Show scene
        window.setScene(quitScene);
    }

    public void play(ActionEvent e) throws IOException {
        // Go to quit scene
        Parent quit = FXMLLoader.load(getClass().getResource("Play.fxml"));

        // Set scene width and height to the previous scene width and height
        Scene prevScene = ((Node)e.getSource()).getScene();
        Stage window = (Stage) ((Node)e.getSource()).getScene().getWindow();
        double prevSceneWidth = prevScene.getWidth();
        double prevSceneHeight = prevScene.getHeight();
        Scene quitScene = new Scene(quit, prevSceneWidth, prevSceneHeight);

        // Show scene
        window.setScene(quitScene);
    }
}
