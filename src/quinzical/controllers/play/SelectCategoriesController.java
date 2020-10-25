package quinzical.controllers.play;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import quinzical.controllers.VoiceSettingsChangeable;
import quinzical.model.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * This controls the scene where the user is able to select the 5 categories that they want to play.
 *
 * @author Beverley Sun, Jinkai Zhang
 */
public class SelectCategoriesController extends VoiceSettingsChangeable {

    private final List<Button> buttons = new ArrayList<>();
    private final ArrayList<Category> categories = new ArrayList<>();
    private final Database database = Database.getInstance();
    private final List<Category> allQuestionsData = database.getPracticeQuestionData();
    private int selectedNumber = 0;
    private Category category;

    @FXML private FlowPane categoryFlowPane;
    @FXML private Button confirm;

    /**
     * Initializes the category selection scene and all of it's buttons.
     */
    @FXML
    public void initialize() {
        super.initialize();

        // Calculate button height for the button area
        double numRows = Math.ceil((double) allQuestionsData.size() / 3);
        double buttonHeight = (180 - 10 * (numRows - 1)) / numRows;

        // Load in the category buttons
        for (Category category : allQuestionsData) {
            // Init button attributes
            Button categoryButton = new Button(category.getCategoryName());
            categoryButton.setId(category.getCategoryName());
            categoryButton.setOnMouseClicked(this::categoryClicked);

            // Set styles on the buttons
            categoryButton.getStyleClass().add("purple-button");
            categoryButton.getStyleClass().add("white-text-fill");
            categoryButton.setPrefSize(163, buttonHeight);

            categoryFlowPane.getChildren().add(categoryButton);

            // For initial buttons.
            buttons.add(categoryButton);
        }
        initButtons();
    }

    /**
     * Set colours for each button depending on if they are selected or not. Enable the confirm button if
     * 5 categories are selected
     */
    private void initButtons() {

        confirm.setDisable(true);

        // Allow user to confirm when all 5 categories selected
        if(selectedNumber == 5){
            confirm.setDisable(false);

            // Disable all unselected categories
            for (Button button : buttons) {
                category = database.getCategoryByName(button.getId());
                if (!category.getSelectStatus()) {
                    button.setDisable(true);
                }
            }
        } else {
            // Enable all categories
            for (Button button : buttons) {
                button.setDisable(false);
            }
        }

        // Set buttons to orange if they are selected
        for (Button button : buttons) {
            category = database.getCategoryByName(button.getId());
            if (category.getSelectStatus()) {
                button.setStyle("-fx-background-color: #D9932A");
            } else {
                button.setStyle(null);
            }
        }
    }

    /**
     * Triggered when a category has been selected
     * @param e the event that happened
     */
    @FXML
    public void categoryClicked(MouseEvent e) {

        Button categoryButton = (Button) e.getSource();
        String categoryStr = categoryButton.getId();

        //Add a new field in Category class, it indicates if the button has been selected or not.
        category = database.getCategoryByName(categoryStr);

        if (category.getSelectStatus()) { // When the user deselects a category
            categories.remove(category);
            category.setSelected(false);
            selectedNumber--;
        } else { // When the user selected a category
            categories.add(category);
            category.setSelected(true);
            selectedNumber++;
        }
        initButtons(); // Re-initiate the buttons
    }

    /**
     * Triggered when the user clicks the confirm button (all categories have been selected)
     * @param e the event that happened
     */
    @FXML
    public void setConfirm(MouseEvent e) {
        // Get an int[] that contains the 5 indices of the user selected categories.
        int[] catIndex = database.getCategoryIndices(categories);
        // Create the file that stores the array.
        database.loadCategoryIndex(catIndex);

        // Go to the question board scene
        try {
            Parent play = FXMLLoader.load(getClass().getResource("/quinzical/scenes/play/Play.fxml"));
            SceneChanger.changeScene(e, play);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    /**
     * This method is invoked when the user click the back button.
     * It will switch to the StartPage interface.
     *
     * @param e the source of the click
     */
    @FXML
    public void back(MouseEvent e) {
        try {
            // If go back here, then reset the game.
            database.reset();
            // Load start page scene
            Parent startPage = FXMLLoader.load(getClass().getResource("/quinzical/scenes/StartPage.fxml"));
            SceneChanger.changeScene(e, startPage);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
}