package quinzical;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PracticeController extends PlayController implements EventHandler<MouseEvent> {

    private final Database _database = Database.getInstance();
    private final List<Category> _practiceQuestionData = _database.getPracticeQuestionData();
    private String _category;
    private Question questionToAsk;
    private static String clue;

    @FXML
    private FlowPane categoryFlowPane;
    Button categoryButton;
    List<Button> categorizations;

    public PracticeController(){

    }

    public void initialize() throws IOException {

        categorizations = new ArrayList<>();
        for (Category category : _practiceQuestionData ) {

            categoryButton = new Button(category.getCategoryName());
            categoryButton.setId(category.getCategoryName());
            categoryButton.setOnMouseClicked(new PracticeController());
            categoryButton.getStyleClass().add("purple-button");
            categoryButton.getStyleClass().add("white-text-fill");
            categorizations.add(categoryButton);
            categoryFlowPane.getChildren().add(categoryButton);
        }
    }

    @Override
    public void handle(MouseEvent e) {
        try {

            Button categoryButton = (Button) e.getSource();
            _category = categoryButton.getId();
            getQuestionInfo();
            clue = questionToAsk.getQuestion();
            Parent answer = FXMLLoader.load(getClass().getResource("PracticeAnswering.fxml"));
            SceneChanger.changeScene(e, answer);

        } catch (IOException event) {
            event.printStackTrace();
        }
    }


    private void getQuestionInfo() {

        questionToAsk = _database.findQuestion(_category, "0", _practiceQuestionData);
        // System.out.print(category);

        try {
            _database.isAttempted(_category);
            int attempted = _database.getAttemptedTimes(_category);
            questionToAsk.set_answeredTimes(attempted);

            int unattempted = 3 - attempted;

            if (unattempted < 1) {
                new File("./.save/PracticeQuestions/" + _category).delete();
                _database.getPracticeQuestionData().clear();
                _database.loadPracticeQuestions();
            }

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static String getClue(){
        return clue;
    }

}
