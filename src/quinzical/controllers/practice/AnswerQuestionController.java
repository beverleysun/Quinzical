package quinzical.controllers.practice;

import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;
import quinzical.Question;
import quinzical.SceneChanger;
import quinzical.TTS;

import java.io.IOException;

public class AnswerQuestionController extends ConfirmController {

    @FXML
    private Label questionClue;
    @FXML
    private Label hintLabel;
    @FXML
    private TextField answerInput;

    private final Question _question;
    private int _attemptsLeft = 3;

    public AnswerQuestionController(Question question) {
        _question = question;
    }

    @FXML
    public void initialize() throws IOException {
        super.initialize();
        questionClue.setText(_question.getQuestion());

        TTS.getInstance().speak(_question.getQuestion());
        hintLabel.setText("3 attempts left");
    }

    @FXML
    private void checkAnswer(MouseEvent e) {
        PauseTransition pauseTransition = new PauseTransition(Duration.seconds(1.5));
        pauseTransition.setOnFinished( event -> {
            if (_attemptsLeft == 1) {
                hintLabel.setText("Last attempt. Hint: The answer starts with " + _question.getAnswer()[0].charAt(0));
            } else {
                hintLabel.setText(_attemptsLeft + " attempts left");
            }
            questionClue.setText(_question.getQuestion());
        });

        String _userAnswer = answerInput.getText();
        _attemptsLeft -= 1;
        try {
            // Validate the answer
            if (_question.compareAnswers(_userAnswer)) {
                Parent answer = FXMLLoader.load(getClass().getResource("../../scenes/practice/Correct.fxml"));
                SceneChanger.changeScene(e, answer);
            }
            else {
                if(_attemptsLeft > 0) {
                    // If the player still has attempts left, flash "that was wrong"
                    questionClue.setText("Oops! That was wrong!");
                    TTS.getInstance().speak("Oops! That was wrong!");
                    pauseTransition.play();
                }
                else {
                    // When all 3 attempts are used, load the incorrect scene
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("../../scenes/practice/Incorrect.fxml"));
                    loader.setController(new IncorrectController(_question));
                    Parent incorrect = loader.load();
                    SceneChanger.changeScene(e, incorrect);
                }
            }
        }
        catch (IOException ioException) {
            ioException.printStackTrace();
        }

    }

    @FXML
    public void giveUp(MouseEvent e) {
        backToPractice(e);
    }

    @FXML
    public void replay() throws IOException {
        TTS.getInstance().speak(_question.getQuestion());
    }
}