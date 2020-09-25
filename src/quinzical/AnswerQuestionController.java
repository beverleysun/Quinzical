package quinzical;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AnswerQuestionController extends PlayController  {

    private  String _realAnswer;
    private  String _givenAnswer;
    private static boolean _result;
    private static int _unattemptedTime;


    @FXML
    private Label questionClue;
    @FXML
    private Label hintLabel;
    @FXML
    private TextField answerInput;
    @FXML
    private Button giveUp;
    @FXML
    private void initialize(){
        questionClue.setText(PracticeController.getClue());
        _unattemptedTime = 4 - PracticeController.getQuestion().getAnsweredTimes();
        System.out.print(_unattemptedTime);
        if(_unattemptedTime > 1) {
            hintLabel.setText("You have " + _unattemptedTime + " chances to answer the question from " + PracticeController.getCategory());
        }
        else{
            hintLabel.setText("This is your last chance to answer the question. Here is the hint, " + PracticeController.getHint() + " " + PracticeController.getAnswerFirstLetter() + "......??");
        }
    }

    @FXML
    private void checkAnswer(MouseEvent e) {
        _realAnswer = answerInput.getText();
        _givenAnswer = PracticeController.getAnswer();
        try {

            if (_realAnswer.toLowerCase().trim().equals(_givenAnswer.toLowerCase().trim())) {
                _result = true;
                Parent answer = FXMLLoader.load(getClass().getResource("Answer.fxml"));
                SceneChanger.changeScene(e, answer);
            }
            else {
                _result = false;
                Parent answer = FXMLLoader.load(getClass().getResource("Answer.fxml"));
                SceneChanger.changeScene(e, answer);
            }
            PracticeController.getQuestionInfo();
        }
        catch (IOException ioException) {
            ioException.printStackTrace();
        }

    }

    public void replay(MouseEvent mouseEvent) {
    }

    @FXML
    public void giveUp(MouseEvent e) throws IOException {
        skipQuestion(e);
    }


    public static void skipQuestion(MouseEvent e) throws IOException {
        
        Parent answer = FXMLLoader.load(AnswerQuestionController.class.getResource("Practice.fxml"));
        SceneChanger.changeScene(e, answer);
        PracticeController.getDatabase().getPracticeQuestionData().clear();

        if(new File("./.save/PracticeQuestions/"+ PracticeController.getCategory()).exists()){
            File file = new File("./.save/PracticeQuestions/"+ PracticeController.getCategory());
            file.delete();
            new File("./.save/PracticeQuestionsIndex/"+ PracticeController.getCategory()).delete();
        }

        PracticeController.getDatabase().loadPracticeQuestions();
    }

    public static boolean getResult(){
        return _result;
    }
    public static int get_unattemptedTime(){return _unattemptedTime;
    }
}