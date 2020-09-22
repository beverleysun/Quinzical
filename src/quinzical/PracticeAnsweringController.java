package quinzical;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PracticeAnsweringController extends PlayController{

    private final Database _database = Database.getInstance();
    private final List<Category> _practiceQuestionData = _database.getPracticeQuestionData();


    @FXML
    private FlowPane categoryFlowPane;
    Button categoryButton;
    List<Button> categorizations;

    public void initialize() {

        categorizations = new ArrayList<>();
        for (Category category : _practiceQuestionData ) {

            categoryButton = new Button(category.getCategoryName());
            categoryButton.getStyleClass().add("purple-button");
            categoryButton.getStyleClass().add("white-text-fill");

            categorizations.add(categoryButton);
            categoryFlowPane.getChildren().add(categoryButton);
        }
    }

}
