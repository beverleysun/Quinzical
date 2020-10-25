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


public class SelectCategoriesController extends VoiceSettingsChangeable {

    private final List<Button> _buttons = new ArrayList<Button>();
    private ArrayList<Category> _categories = new ArrayList<Category>();
    private Category _category;
    private int _selectedNumber = 0;


    private final Database _database = Database.getInstance();
    private List<Category> _practiceQuestionData = _database.getPracticeQuestionData();



    @FXML
    private FlowPane categoryFlowPane;
    @FXML
    private Button confirm;

    /**
     * Initializes the practice scene and all of it's buttons.
     */
    public void initialize() {
        super.initialize();
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

            //for initial buttons.
            _buttons.add(categoryButton);
        }
        initButtons();
    }

    // change the colors when the button is clicked.
    private void initButtons() {

        confirm.setDisable(true);
        if(_selectedNumber == 5){
            confirm.setDisable(false);
        }

        for (Button button : _buttons) {
            _category = _database.getCategory(button.getId());
            if (_category.getSelectStatus()) {
                button.setStyle("-fx-background-color: orange");
            } else {
                button.setStyle(null);
            }
        }

    }

    public void categoryClicked(MouseEvent e) {

        Button categoryButton = (Button) e.getSource();
        String categoryStr = categoryButton.getId();
        //Add a new field in Category class, it indicates the button is been selected or not.
        _category = _database.getCategory(categoryStr);

        if (_category.getSelectStatus()) {
            _categories.remove(_category);
            _category.setSelected(false);
            _selectedNumber--;
        } else {
            _categories.add(_category);
            _category.setSelected(true);
            _selectedNumber++;
        }
        initButtons();
    }

    public void setConfirm(MouseEvent e) {
        //get a int[] that contains the 5 index of user selected category.
        int[] catIndex = _database.getCategorieIndex(_categories);
        //Create the file that store the array.
        _database.loadCategoryIndex(catIndex);


        try {
            Parent play = FXMLLoader.load(getClass().getResource("/quinzical/scenes/play/Play.fxml"));
            SceneChanger.changeScene(e, play);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }


    }


    /**
     * This method is invoked when the user click the back button in Practice scene.
     * It will switch to the StartPage interface.
     *
     * @param e the source of the click
     */
    public void back(MouseEvent e) {
        try {

            // If go back here, then reset the game. (or maybe just delete the Catgory Index file.)
            _database.reset();
            // Load start page scene
            Parent startPage = FXMLLoader.load(getClass().getResource("/quinzical/scenes/StartPage.fxml"));
            SceneChanger.changeScene(e, startPage);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }


}