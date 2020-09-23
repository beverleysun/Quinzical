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


    private final Database _database = Database.getInstance();
    private final List<Category> _questionData = _database.getQuestionData();
    private final List<Button> _buttons = new ArrayList<Button>();

    @FXML
    public void initialize() {
        setLabels();
        initButtons();
    }

    private void setLabels() {
        cat1Label.setText(_questionData.get(0).getCategoryName());
        cat2Label.setText(_questionData.get(1).getCategoryName());
        cat3Label.setText(_questionData.get(2).getCategoryName());
        cat4Label.setText(_questionData.get(3).getCategoryName());
        cat5Label.setText(_questionData.get(4).getCategoryName());
    }

    private void initButtons() {
        Collections.addAll(_buttons, cat1value100Button, cat1value200Button, cat1value300Button, cat1value400Button, cat1value500Button,
                cat2value100Button, cat2value200Button, cat2value300Button, cat2value400Button, cat2value500Button,
                cat3value100Button, cat3value200Button, cat3value300Button, cat3value400Button, cat3value500Button,
                cat4value100Button, cat4value200Button, cat4value300Button, cat4value400Button, cat4value500Button,
                cat5value100Button, cat5value200Button, cat5value300Button, cat5value400Button, cat5value500Button);
        for (Button button: _buttons) {
            int[] parsed = parseButtonID(button);

            Question questionToAsk = _questionData.get(parsed[0]-1).findQuestionByValue(parsed[1]);

            if (!questionToAsk.isAvailable()) {
                button.setDisable(true);
            } else {
                button.setDisable(false);
            }
        }
    }

    public void askQuestion(MouseEvent e) {
        Button buttonSource = (Button) e.getSource();
        int[] parsed = parseButtonID(buttonSource);

        Question question = _questionData.get(parsed[0]-1).findQuestionByValue(parsed[1]);
        String questionStr = question.getQuestion();
        String answerStr = question.getAnswer();

        buttonSource.setDisable(true);

        // Set next button available. Disable clicked button
        if (parsed[1] != 500) {
            int nextValue = parsed[1]+100;
            String nextID = parsed[0] + "-" + nextValue;
            for (Button button : _buttons) {
                if (button.getId().equals(nextID)){
                    button.setDisable(false);
                    break;
                }
            }
        }

        try {
            // Initialise controller with specific question and answer fields
            AskQuestionController controller = new AskQuestionController(questionStr, answerStr);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("AskQuestion.fxml"));
            loader.setController(controller);

            // Change scene
            Parent askQuestion = loader.load();
            SceneChanger.changeScene(e, askQuestion);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    // Get the category number and question value from the ID of a button
    private int[] parseButtonID(Button button) {
        int category = Integer.parseInt(button.getId().split("-")[0]);
        int value = Integer.parseInt(button.getId().split("-")[1]);
        return new int[]{category, value};
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
