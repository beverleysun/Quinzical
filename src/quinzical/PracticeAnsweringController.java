package quinzical;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PracticeAnsweringController extends PlayController  {


    public PracticeAnsweringController(){

    }

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
        int unattemptedTime = 4 - PracticeController.getQuestion().getAnsweredTimes();
        System.out.print(unattemptedTime);
        if(unattemptedTime > 1) {
            hintLabel.setText("You have " + unattemptedTime + " chances to answer the question from " + PracticeController.getCategory());
        }
        else{
            hintLabel.setText("This is your last chance to answer the question. Here is the hint, " + PracticeController.getHint() + " " + PracticeController.getAnswerFirstLetter() + "......??");
        }
    }

    @FXML
    private void checkAnswer(MouseEvent e) {
        try {
            if (answerInput.getText().equals(PracticeController.getAnswer())) {
                Parent answer = FXMLLoader.load(getClass().getResource("Answer.fxml"));
                SceneChanger.changeScene(e, answer);
            }
            else {
                System.out.print("no");


            }
        }
        catch (IOException ioException) {
            ioException.printStackTrace();

        }

    }

    public void replay(MouseEvent mouseEvent) {
    }
}
