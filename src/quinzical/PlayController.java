package quinzical;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.util.List;

public class PlayController {

    @FXML
    private Label cat1Label;

    @FXML
    private Label cat2Label;

    @FXML
    private Label cat3Label;

    @FXML
    private Label cat4Label;

    @FXML
    private Label cat5Label;

    private Database _database = Database.getInstance();
    private List<Category> _questionData = _database.getQuestionData();

    @FXML
    public void initialize() {
        setLabels();
    }

    public void setLabels() {
        cat1Label.setText(_questionData.get(0).getCategoryName());
        cat2Label.setText(_questionData.get(1).getCategoryName());
        cat3Label.setText(_questionData.get(2).getCategoryName());
        cat4Label.setText(_questionData.get(3).getCategoryName());
        cat5Label.setText(_questionData.get(4).getCategoryName());
    }

    public void back(MouseEvent e) {
        try {
            // Load start page scene
            Parent startPage = FXMLLoader.load(getClass().getResource("StartPage.fxml"));
            SceneChanger.changeScene(e, startPage);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
}
