package quinzical;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.stage.Stage;

import java.io.IOException;

public class QuitPromptController {

    public void noQuit(ActionEvent e) throws IOException {
        // Go back to start page
        Parent quit = FXMLLoader.load(getClass().getResource("StartPage.fxml"));
        SceneChanger.changeScene(e, quit);
    }

    public void yesQuit(ActionEvent e) {
        // Close the game
        Stage window = (Stage) ((Node)e.getSource()).getScene().getWindow();
        window.close();
    }
}
