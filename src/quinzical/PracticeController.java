package quinzical;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PracticeController {

    private final Database _database = Database.getInstance();
    private final List<Category> _practiceQuestionData = _database.getPracticeQuestionData();


    @FXML
    private FlowPane categoryFlowPane;
    private Button categorybutton;
    private List<Button> categorybuttons;

    public void initialize() {

        categorybuttons = new ArrayList<>();
        for (Category category : _practiceQuestionData ) {

            categorybutton = new Button(category.getCategoryName());
            categorybutton.getStyleClass().add("purple-button");
            categorybutton.getStyleClass().add("white-text-fill");

            categorybuttons.add(categorybutton);
            categoryFlowPane.getChildren().add(categorybutton);
        }
    }

    public void back (MouseEvent e) {
        try {
            // Load start page scene
            Parent startPage = FXMLLoader.load(getClass().getResource("StartPage.fxml"));
            SceneChanger.changeScene(e, startPage);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
}
