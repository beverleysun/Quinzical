package quinzical;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PracticeController extends PlayController implements EventHandler<MouseEvent> {

    private static final Database _database = Database.getInstance();
    private static final List<Category> _practiceQuestionData = _database.getPracticeQuestionData();
    private static String _category;
    private static Question questionToAsk;
    private static String clue;
    private static String answer;
    private static String answerFisrLetter;
    private static String hint;
    private static int attempted;

    @FXML
    private FlowPane categoryFlowPane;
    Button categoryButton;
    List<Button> categorizations;

    public PracticeController(){

    }

    public void initialize() {

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
            answer = questionToAsk.getAnswer();
            answerFisrLetter = Character.toString(answer.charAt(0));
            hint = questionToAsk.getHint();
            Parent answer = FXMLLoader.load(getClass().getResource("AnswerQuestion.fxml"));
            SceneChanger.changeScene(e, answer);

        } catch (IOException event) {
            event.printStackTrace();
        }
    }


    public static void getQuestionInfo() {

        questionToAsk = _database.findQuestion(_category);


        try {
            _database.isAttempted(_category);
            attempted = _database.getAttemptedTimes(_category);
            questionToAsk.set_answeredTimes(attempted);

            int unattempted = 4 - attempted;

            if (unattempted < 1) {
                new File("./.save/PracticeQuestions/" + _category).delete();
                new File("./.save/PracticeQuestionsIndex/"+ _category).delete();
                _database.getPracticeQuestionData().clear();
                _database.loadPracticeQuestions();
            }

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static String getCategory(){
        return _category;
    }
    public static String getClue(){
        return clue;
    }
    public static String getAnswer(){
        return answer;
    }
    public static String getHint(){
        return hint;
    }
    public static Question getQuestion(){
        return questionToAsk;
    }
    public static String getAnswerFirstLetter(){
        return  answerFisrLetter;
    }
    public static Database getDatabase(){return _database;}
    public static int getAttempted(){return attempted;}


}