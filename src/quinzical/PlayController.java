package quinzical;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PlayController {

    @FXML
    private Label cat1Label, cat2Label, cat3Label, cat4Label, cat5Label;

    @FXML
    private Button cat1value100Button, cat1value200Button, cat1value300Button, cat1value400Button, cat1value500Button,
                    cat2value100Button, cat2value200Button, cat2value300Button, cat2value400Button, cat2value500Button,
                    cat3value100Button, cat3value200Button, cat3value300Button, cat3value400Button, cat3value500Button,
                    cat4value100Button, cat4value200Button, cat4value300Button, cat4value400Button, cat4value500Button,
                    cat5value100Button, cat5value200Button, cat5value300Button, cat5value400Button, cat5value500Button ;



    private Database _database = Database.getInstance();
    private List<Category> _questionData = _database.getQuestionData();
    private List<Button> _buttons = new ArrayList<Button>();

    @FXML
    public void initialize() {
        setLabels();
        initButtons();
    }

    public void setLabels() {
        cat1Label.setText(_questionData.get(0).getCategoryName());
        cat2Label.setText(_questionData.get(1).getCategoryName());
        cat3Label.setText(_questionData.get(2).getCategoryName());
        cat4Label.setText(_questionData.get(3).getCategoryName());
        cat5Label.setText(_questionData.get(4).getCategoryName());
    }

    public void initButtons() {
        Collections.addAll(_buttons, cat1value100Button, cat1value200Button, cat1value300Button, cat1value400Button, cat1value500Button,
                cat2value100Button, cat2value200Button, cat2value300Button, cat2value400Button, cat2value500Button,
                cat3value100Button, cat3value200Button, cat3value300Button, cat3value400Button, cat3value500Button,
                cat4value100Button, cat4value200Button, cat4value300Button, cat4value400Button, cat4value500Button,
                cat5value100Button, cat5value200Button, cat5value300Button, cat5value400Button, cat5value500Button);
        for (Button button: _buttons) {
            int category = Integer.parseInt(button.getId().split("-")[0]);
            int value = Integer.parseInt(button.getId().split("-")[1]);

            Question questionToAsk = _questionData.get(category-1).findQuestionByValue(value);

            if (!questionToAsk.isAvailable()) {
                button.setDisable(true);
            } else {
                button.setDisable(false);
            }
        }
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
