package quinzical.controllers.practice;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import quinzical.SceneChanger;
import quinzical.TTS;
import quinzical.controllers.VoiceSpeedChangeable;

import java.io.IOException;

public class AnswerController extends VoiceSpeedChangeable {

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
        super.initialize();
        if(AnswerQuestionController.getResult()){
            clueLabel.setText("Correct!");
            TopLabel.setText(null);
            BottomLabel.setText(null);
            TTS.getInstance().speak("Correct!");
        }
        else{
            if(PracticeController.getAttempted() < 3){
                clueLabel.setText(null);
                TopLabel.setText(null);
                BottomLabel.setText("Sorry, you are incorrect!");
                TTS.getInstance().speak("Sorry, you are incorrect!");
            }
            else {
                TopLabel.setText("Oops, the answer to");
                clueLabel.setText("\"" + PracticeController.getClue() + "\"" + " was");
                BottomLabel.setText(PracticeController.getAnswer());
                TTS.getInstance().speak("Oops, the answer to " +PracticeController.getClue() + " was" + PracticeController.getAnswer());
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