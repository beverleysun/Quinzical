package quinzical.controllers.practice;

import javafx.animation.PauseTransition;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;
import quinzical.model.Question;
import quinzical.model.SceneChanger;
import quinzical.model.TTS;
import quinzical.controllers.VoiceSettingsChangeable;

import java.io.IOException;

/**
 * Controls the scene where the user can answer the question
 *
 * @author Beverley Sun, Jinkai Zhang
 */
public class AnswerQuestionController extends VoiceSettingsChangeable {

    @FXML private Label questionClue;
    @FXML private Label hintLabel;
    @FXML private TextField answerInput;

    private final Question question;
    private int attemptsLeft = 3;

    /**
     * Takes in the question that needs to be asked so that the scene can display it
     * @param question the question to be displayed
     */
    public AnswerQuestionController(Question question) {
        this.question = question;
    }

    /**
     * Initializes the scene and its contents
     */
    @FXML
    public void initialize() {
        super.initialize();
        questionClue.setText(question.getQuestionStr());
        TTS.getInstance().speak(question.getQuestionStr());
        hintLabel.setText("3 attempts left");
    }

    /**
     * This method is invoked when the user click the confirm arrow, it will invoke the check answer method.
     */
    @FXML
    public void confirm(MouseEvent e) {
        checkAnswer(e);
    }

    /**
     * This method is invoked when the text field is active. If the user press enter key, it will invoke
     * the check answer method.
     * @param e the event that was triggered
     */
    @FXML
    public void enter(KeyEvent e) {
        if (e.getCode() == KeyCode.ENTER) {
            checkAnswer(e);
        }
    }

    /**
     * This method compare the user's input with the actual answer in the database. Different content of
     * the clue label will be displayed after the pause. It depends on the attempted times of the question.
     * @param e the event that was triggered
     */
    @FXML
    private void checkAnswer(Event e) {

        // Display contents after 1.5 second
        PauseTransition pauseTransition = new PauseTransition(Duration.seconds(1.5));
        pauseTransition.setOnFinished( event -> {
            if (attemptsLeft == 1) {
                hintLabel.setText("Last attempt. Hint: The answer starts with " + question.getAnswers()[0].charAt(0));
            } else {
                hintLabel.setText(attemptsLeft + " attempts left");
            }
            questionClue.setText(question.getQuestionStr());
        });

        String _userAnswer = answerInput.getText();
        attemptsLeft -= 1;
        try {
            // Validate the answer
            if (question.compareAnswers(_userAnswer)) {
                Parent answer = FXMLLoader.load(getClass().getResource("/quinzical/scenes/practice/Correct.fxml"));
                SceneChanger.changeScene(e, answer);
            }
            else {
                if(attemptsLeft > 0) {
                    // If the player still has attempts left, flash "that was wrong"
                    questionClue.setText("Oops! That was wrong!");
                    TTS.getInstance().speak("Oops! That was wrong!");
                    pauseTransition.play();
                }
                else {
                    // When all 3 attempts are used, load the incorrect scene
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/quinzical/scenes/practice/Incorrect.fxml"));
                    loader.setController(new IncorrectController(question));
                    Parent incorrect = loader.load();
                    SceneChanger.changeScene(e, incorrect);
                }
            }
        }
        catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    /** This method is invoked when the user press the macron letter button
     * @param e the source of the click
     */
    @FXML
    public void addMacron(MouseEvent e) {
        String macron = ((Button) e.getSource()).getText();
        answerInput.appendText(macron);
    }

    /** This method is invoked when the user press the give-up button. It will switch to the
     * incorrect answer scene and display the actual answer.
     * @param e the source of the click
     */
    @FXML
    public void giveUp(MouseEvent e) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/quinzical/scenes/practice/Incorrect.fxml"));
        loader.setController(new IncorrectController(question));
        try {
            Parent incorrect = loader.load();
            SceneChanger.changeScene(e, incorrect);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    /** This method is invoked when the user press the replay button.
     * It will read the question again by festival.
     */
    @FXML
    public void replay() {
        TTS.getInstance().speak(question.getQuestionStr());
    }
}