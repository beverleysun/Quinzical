package quinzical.controllers.practice;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import quinzical.SceneChanger;
import quinzical.controllers.play.PlayController;

import java.io.IOException;

public class AnswerController extends PlayController {


    public AnswerController() {

    }
    @FXML
    private Button next;
    @FXML
    private Label clueLabel;
    @FXML
    private Label TopLabel;
    @FXML
    private Label BottomLabel;

    @FXML
    public void initialize() {
    if(AnswerQuestionController.getResult()){
        clueLabel.setText("Correct!");
        TopLabel.setText(null);
        BottomLabel.setText(null);

    }
    else{
        if(PracticeController.getAttempted() <3){
            clueLabel.setText("Sorry, you are incorrect!");
            TopLabel.setText(null);
            BottomLabel.setText(null);
        }
        else {
            TopLabel.setText("Oops, the answer to");
            clueLabel.setText(PracticeController.getClue() + " Was");
            BottomLabel.setText(PracticeController.getAnswer());
        }
    }

    }

    @FXML
    private void backToQuestion(MouseEvent e) throws IOException {

        if(AnswerQuestionController.get_unattemptedTime() > 1 && !AnswerQuestionController.getResult()) {
            Parent answer = FXMLLoader.load(getClass().getResource("../../scenes/practice/AnswerQuestion.fxml"));
            SceneChanger.changeScene(e, answer);
        }
        else {
        AnswerQuestionController.skipQuestion(e);
        }
    }

}