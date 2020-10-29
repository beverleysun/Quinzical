package quinzical.controllers.practice;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import quinzical.model.*;
import quinzical.controllers.VoiceSettingsChangeable;

import java.io.IOException;
import java.util.List;

/**
 * Controls the scene where the user can select a category to practice
 *
 * @author Beverley Sun, Jinkai Zhang
 */
public class PracticeController extends VoiceSettingsChangeable {

    private final Database database = Database.getInstance();
    private final List<Category> practiceQuestionData = database.getPracticeQuestionData();

    @FXML private FlowPane categoryFlowPane;

    /**
     * Initializes the practice scene and all of it's buttons.
     */
    @FXML
    public void initialize() {
        super.initialize();
        TTS.getInstance().killCurrentProcess();

        // Calculate button height to fill up the height of the FlowPane
        double numRows = Math.ceil((double) practiceQuestionData.size() / 3);
        double buttonHeight = (180 - 10 * (numRows - 1)) / numRows;

        // Load in the category buttons
        for (Category category : practiceQuestionData) {
            Button categoryButton = new Button(category.getCategoryName());
            categoryButton.setId(category.getCategoryName());
            categoryButton.setOnMouseClicked(this::categoryClicked);
            categoryButton.getStyleClass().add("purple-button");
            categoryButton.getStyleClass().add("white-text-fill");
            categoryButton.setPrefSize(163, buttonHeight);
            categoryFlowPane.getChildren().add(categoryButton);
        }
    }

    /** This method is invoked when the user click a category button. It will find the random
     * question in that category and switch to AnswerQuestion scene.
     *  @param e the source of the click
     */
    public void categoryClicked(MouseEvent e) {
        try {
            Button categoryButton = (Button) e.getSource();

            String categoryStr = categoryButton.getId();
            Question question = database.findRandomPracticeQuestionByCategory(categoryStr);

            // Change scene
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/quinzical/scenes/practice/AnswerQuestion.fxml"));
            loader.setController(new AnswerQuestionController(question));
            Parent answer = loader.load();
            SceneChanger.changeScene(e, answer);

        } catch (IOException event) {
            event.printStackTrace();
        }
    }

    /** This method is invoked when the user click the back button in Practice scene.
     * It will switch to the StartPage interface.
     *  @param e the source of the click
     */
    public void back(MouseEvent e) {
        try {
            // Load start page scene
            Parent startPage = FXMLLoader.load(getClass().getResource("/quinzical/scenes/StartPage.fxml"));
            SceneChanger.changeScene(e, startPage);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
}