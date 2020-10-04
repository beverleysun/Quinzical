package quinzical.controllers.play;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import quinzical.Database;
import quinzical.SceneChanger;

import java.io.IOException;

public class GameCompletedController {

    @FXML
    private Label finalWinningsLabel;

    /* This method control the initial display of the GameCompleted scene. */
    @FXML
    public void initialize() {
        int winnings = Database.getInstance().getWinnings();
        if (winnings > 0) {
            finalWinningsLabel.setText("You won $" + winnings + "!");
        } else if (winnings < 0) {
            finalWinningsLabel.setText("But, you lost $" + winnings + " :(");
        } else {
            finalWinningsLabel.setText("But, you didn't win anything :(");
        }
    }

    /* This method is invoked when the user click the Reset To Play Again button.
     * It will reset the game and switch to the StartPage scene. */
    public void reset(MouseEvent e) {
        Database.getInstance().reset();
        try {
            Parent start = FXMLLoader.load(getClass().getResource("../../scenes/StartPage.fxml"));
            SceneChanger.changeScene(e, start);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
}
