package quinzical.controllers.practice;

import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;
import quinzical.SceneChanger;
import quinzical.TTS;
import quinzical.controllers.VoiceSpeedChangeable;

import java.io.File;
import java.io.IOException;

public class AnswerQuestionController extends VoiceSpeedChangeable {

    private static boolean _result;
    private static int _unattemptedTime;

    @FXML
    private Label questionClue;
    @FXML
    private Label hintLabel;
    @FXML
    private TextField answerInput;


    @FXML
    public void initialize(){
        super.initialize();
        questionClue.setText(PracticeController.getClue());
        TTS.getInstance().speak(PracticeController.getClue());
        _unattemptedTime = 4 - PracticeController.getQuestion().getAnsweredTimes();

        if(_unattemptedTime > 1) {
            hintLabel.setText("You have " + _unattemptedTime + " chances to answer the question from " + PracticeController.getCategory());
        }
        else{
            hintLabel.setText("This is your last chance. Hint, " + PracticeController.getHint() + " " + PracticeController.getAnswerFirstLetter() + "...");
        }
    }

    @FXML
    private void checkAnswer(MouseEvent e) {
        PauseTransition pauseTransition = new PauseTransition(Duration.seconds(1.5));
        String _realAnswer = answerInput.getText();
        try {
            if (PracticeController.getQuestion().compareAnswers(_realAnswer)) {
                Parent answer = FXMLLoader.load(getClass().getResource("../../scenes/practice/Correct.fxml"));
                SceneChanger.changeScene(e, answer);
            }
            else {
                if(_unattemptedTime > 1) {
                    questionClue.setText("Sorry, you are incorrect!");
                    TTS.getInstance().speak("Sorry, you are incorrect!");
                    pauseTransition.setOnFinished( event -> initialize());
                    pauseTransition.play();
                }
                else {
                    Parent answer = FXMLLoader.load(getClass().getResource("../../scenes/practice/Incorrect.fxml"));
                    SceneChanger.changeScene(e, answer);
                }
            }
            PracticeController.getQuestionInfo();
        }
        catch (IOException ioException) {
            ioException.printStackTrace();
        }

    }

    @FXML
    public void giveUp(MouseEvent e) throws IOException {
        skipQuestion(e);
    }

    public static void skipQuestion(MouseEvent e) throws IOException {
        Parent answer = FXMLLoader.load(AnswerQuestionController.class.getResource("../../scenes/practice/Practice.fxml"));
        SceneChanger.changeScene(e, answer);
        PracticeController.getDatabase().getPracticeQuestionData().clear();

        if(new File("./.save/PracticeQuestions/"+ PracticeController.getCategory()).exists()){
            File file = new File("./.save/PracticeQuestions/"+ PracticeController.getCategory());
            file.delete();
            new File("./.save/PracticeQuestionsIndex/"+ PracticeController.getCategory()).delete();
        }

        PracticeController.getDatabase().loadPracticeQuestions();
    }

    @FXML
    public void replay() {
        TTS.getInstance().speak(PracticeController.getClue());
    }



    public static boolean getResult(){
        return _result;
    }
    public static int get_unattemptedTime() {
        return _unattemptedTime;
    }
}