package quinzical.controllers.practice;

import javafx.event.EventHandler;
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
import quinzical.TTS;
import quinzical.controllers.VoiceSpeedChangeable;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PracticeController extends VoiceSpeedChangeable implements EventHandler<MouseEvent> {

    private static final Database _database = Database.getInstance();
    private static final List<Category> _practiceQuestionData = _database.getPracticeQuestionData();
    private static String _category;
    private static Question questionToAsk;
    private static String clue;
    private static String[] answers;
    private static String answerFirstLetter;
    private static String hint;
    private static int attempted;

    @FXML
    private FlowPane categoryFlowPane;
    private Button categoryButton;
    private List<Button> categorizations;

    public void initialize() {
    	super.initialize();
        categorizations = new ArrayList<>();
        for (Category category : _practiceQuestionData ) {

            categoryButton = new Button(category.getCategoryName());
            categoryButton.setId(category.getCategoryName());
            categoryButton.setOnMouseClicked(new PracticeController());
            categoryButton.getStyleClass().add("purple-button");
            categoryButton.getStyleClass().add("white-text-fill");
            categoryButton.setPrefSize(120,40);
            categorizations.add(categoryButton);
            categoryFlowPane.getChildren().add(categoryButton);
        }
    }

    @FXML
    public void replay() {
        TTS.getInstance().speak(clue);
    }

    @Override
    public void handle(MouseEvent e) {
        try {
            Button categoryButton = (Button) e.getSource();

            _category = categoryButton.getId();
            getQuestionInfo();
            clue = questionToAsk.getQuestion();
            answers = questionToAsk.getAnswer();
            answerFirstLetter = Character.toString(answers[0].charAt(0));
            hint = questionToAsk.getHint();
            Parent answer = FXMLLoader.load(getClass().getResource("../../scenes/practice/AnswerQuestion.fxml"));
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
            e.printStackTrace();
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

    public static String getCategory(){
        return _category;
    }
    public static String getClue(){
        return clue;
    }
    public static String[] getAnswer(){
        return answers;
    }
    public static String getHint(){
        return hint;
    }
    public static Question getQuestion(){
        return questionToAsk;
    }
    public static String getAnswerFirstLetter(){
        return  answerFirstLetter;
    }
    public static Database getDatabase(){return _database;}
    public static int getAttempted(){return attempted;}
}