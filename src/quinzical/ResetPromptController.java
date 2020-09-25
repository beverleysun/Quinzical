package quinzical;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

import java.io.IOException;

public class ResetPromptController {


    @FXML
    private Label finalScoreLabel;


    @FXML
    public void initialize() {
        finalScoreLabel.setText("You're final score was $" + Database.getInstance().getWinnings());
    }

    public void yesReset(MouseEvent e) {
        Database.getInstance().reset();
        goToQuestionBoard(e);
    }

    public void goToQuestionBoard(MouseEvent e) {
        try {
            Parent questionBoard = FXMLLoader.load(getClass().getResource("Play.fxml"));
            SceneChanger.changeScene(e, questionBoard);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
}
