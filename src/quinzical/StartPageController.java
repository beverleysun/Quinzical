package quinzical;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.IOException;

public class StartPageController {

    public void quit(ActionEvent e) {
        // Go to quit prompt
        try {
            Parent quit = FXMLLoader.load(getClass().getResource("QuitPrompt.fxml"));
            SceneChanger.changeScene(e, quit);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    public void play(ActionEvent e) {
        // Go to game module
        try {
            Parent play = FXMLLoader.load(getClass().getResource("Play.fxml"));
            SceneChanger.changeScene(e, play);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
}
