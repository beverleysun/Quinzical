package quinzical;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

import java.io.IOException;

public class GameCompletedController {

    @FXML
    private Label finalWinningsLabel;

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

    public void reset(MouseEvent e) {
        Database.getInstance().reset();
        try {
            Parent start = FXMLLoader.load(getClass().getResource("StartPage.fxml"));
            SceneChanger.changeScene(e, start);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
}
