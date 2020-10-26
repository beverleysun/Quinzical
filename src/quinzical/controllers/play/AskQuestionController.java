package quinzical.controllers.play;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Polyline;
import javafx.stage.Stage;
import javafx.util.Duration;
import quinzical.model.Database;
import quinzical.model.Question;
import quinzical.model.SceneChanger;
import quinzical.model.TTS;
import quinzical.controllers.VoiceSettingsChangeable;

import java.io.IOException;
import java.util.Arrays;

/**
 * Controls the asking of the question scene
 *
 * @author Beverley Sun, Jinkai Zhang
 */
public class AskQuestionController extends VoiceSettingsChangeable {
    private final String categoryStr;
    private final Question question;
    private Timeline timeline;

    @FXML private Label categoryLabel, valueLabel, winnings;
    @FXML private TextField textField;
    @FXML private Label timeLeft;
    @FXML private Polyline confirm;

    /**
     * Takes in the question and category that is being asked
     * @param question    the question
     * @param categoryStr the name of the category
     */
    public AskQuestionController(Question question, String categoryStr) {
        this.categoryStr = categoryStr;
        this.question = question;
    }

    /**
     * This method will initialize the AskQuestion scene. It will control the display of slider bar, the current winnings,
     * play the clue by festival.
     */
    @FXML
    public void initialize() {
        super.initialize();
        initTimer(30);

        categoryLabel.setText(categoryStr);
        valueLabel.setText("$" + question.getValue());
        winnings.setText("$" + Database.getInstance().getWinnings());

        TTS.getInstance().speak(question.getQuestionStr());
    }

    /**
     * Initialize the timer to 30 seconds to answer the question
     */
    public void initTimer(int timerValue) {
        IntegerProperty i = new SimpleIntegerProperty(timerValue);
        timeLeft.setText(Integer.toString(i.get()));

        timeline = new Timeline(
                new KeyFrame(
                        // Update the timer every second
                        Duration.seconds(1),
                        event -> {
                            i.set(i.get() - 1);
                            timeLeft.setText(Integer.toString(i.get()));

                            // When time is up, compare answer in the text box
                            if(i.get() == 0) {

                                // Initiate the scene
                                String userAnswer = textField.getText();
                                if (question.compareAnswers(userAnswer)) {
                                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/quinzical/scenes/play/Correct.fxml"));
                                    loadScene(loader);
                                } else {
                                    String answerTemp = Arrays.toString(question.getAnswers());
                                    String answer = answerTemp.substring(1, answerTemp.length() - 1); // Remove square brackets as a result of Arrays.toString()

                                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/quinzical/scenes/play/Incorrect.fxml"));
                                    loader.setController(new IncorrectController(answer));
                                    loadScene(loader);
                                }
                            }
                        }
                )
        );
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    /**
     * Loads a scene and preserves scene width and height
     * @param loader the loader
     */
    public void loadScene(FXMLLoader loader){
        // Get scene data
        double width = confirm.getScene().getWidth();
        double height = confirm.getScene().getHeight();
        Stage window = (Stage) confirm.getScene().getWindow();

        // Load the scene
        try {
            window.setScene(new Scene(loader.load(), width, height));
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    /**
     * This method is invoked when the user press the replay button.
     * It will read the question again by festival.
     */
    @FXML
    public void replay() {
        TTS.getInstance().speak(question.getQuestionStr());
    }

    /**
     * Validates the answer when the confirm arrow is clicked.
     * @param e the source of the click
     */
    @FXML
    public void confirm(Event e) {
        validateAnswer(e);
    }

    /**
     * This method is invoked when the text field is active. If the user press enter key, it will invoke
     * the check answer method.
     * @param e the source of the key press
     */
    @FXML
    public void enter(KeyEvent e) {
        if (e.getCode() == KeyCode.ENTER) {
            validateAnswer(e);
        }
    }

    /**
     *  This method compare the user's input with the actual answer in the database.
     *  @param e the source the validation
     */
    public void validateAnswer(Event e) {
        timeline.stop();
        String userAnswer = textField.getText();
        if (question.compareAnswers(userAnswer)) {
            Database.getInstance().addWinnings(question.getValue());
            loadCorrectScene(e);
        } else {
            loadIncorrectScene(e);
        }
    }

    /**
     *  This method is invoked when the user press the macron letter button
     *  @param e the button source
     */
    @FXML
    public void addMacron(MouseEvent e) {
        String macron = ((Button) e.getSource()).getText();
        textField.appendText(macron);
    }

    /**
     * When the user clicks the give up button
     * @param e the event that happened
     */
    @FXML
    public void giveUp(MouseEvent e) {
        timeline.stop();
        loadIncorrectScene(e);
    }

    /**
     * This method is invoked when the user's input is incorrect.
     * It will switch to the incorrect scene and display the answer of the question.
     * @param e the event source
     */
    @FXML
    public void loadIncorrectScene(Event e) {
        String answerTemp = Arrays.toString(question.getAnswers());
        String answer = answerTemp.substring(1,answerTemp.length()-1); // Remove square brackets as a result of Arrays.toString()

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/quinzical/scenes/play/Incorrect.fxml"));
        loader.setController(new IncorrectController(answer));

        try {
            // Load the "incorrect" scene
            Parent incorrect = loader.load();
            SceneChanger.changeScene(e, incorrect);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    /**
     * This method is invoked when the user's input is correct.
     * It will switch to the correct scene.
     * @param e the event source
     */
    public void loadCorrectScene(Event e){
        try {
            // Load the "correct" scene
            Parent correct = FXMLLoader.load(getClass().getResource("/quinzical/scenes/play/Correct.fxml"));
            SceneChanger.changeScene(e, correct);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
}
