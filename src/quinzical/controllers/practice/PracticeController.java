package quinzical.controllers.practice;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import quinzical.Category;
import quinzical.Database;
import quinzical.Question;
import quinzical.SceneChanger;
import quinzical.controllers.VoiceSpeedChangeable;

import java.io.IOException;
import java.util.List;

public class PracticeController extends VoiceSpeedChangeable {

    private Database _database = Database.getInstance();
    private List<Category> _practiceQuestionData = _database.getPracticeQuestionData();

    @FXML
    private FlowPane categoryFlowPane;

    public void initialize() {
        super.initialize();

        // Calculate button height to fill up the height of the FlowPane
        double numRows = Math.ceil((double) _practiceQuestionData.size() / 4);
        double buttonHeight = (180 - 10 * (numRows - 1)) / numRows;

        // Load in the category buttons
        for (Category category : _practiceQuestionData) {
            Button categoryButton = new Button(category.getCategoryName());
            categoryButton.setId(category.getCategoryName());
            categoryButton.setOnMouseClicked(this::categoryClicked);
            categoryButton.getStyleClass().add("purple-button");
            categoryButton.getStyleClass().add("white-text-fill");
            categoryButton.setPrefSize(120, buttonHeight);
            categoryFlowPane.getChildren().add(categoryButton);
        }
    }

    public void categoryClicked(MouseEvent e) {
        try {
            Button categoryButton = (Button) e.getSource();

            String categoryStr = categoryButton.getId();
            Question question = _database.findRandomPracticeQuestionByCategory(categoryStr);

            // Change scene
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../../scenes/practice/AnswerQuestion.fxml"));
            loader.setController(new AnswerQuestionController(question));
            Parent answer = loader.load();
            SceneChanger.changeScene(e, answer);

        } catch (IOException event) {
            event.printStackTrace();
        }
    }


    public void back(MouseEvent e) {
        try {
            // Load start page scene
            Parent startPage = FXMLLoader.load(getClass().getResource("../../scenes/StartPage.fxml"));
            SceneChanger.changeScene(e, startPage);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
}