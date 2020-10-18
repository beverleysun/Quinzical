package quinzical.controllers.play;

import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.robot.Robot;
import javafx.scene.shape.Polyline;
import javafx.util.Duration;
import quinzical.*;
import quinzical.controllers.VoiceSettingsChangeable;

import java.io.IOException;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CountDownLatch;

import static javafx.scene.input.MouseButton.PRIMARY;

public class AskQuestionController extends VoiceSettingsChangeable {
    private final String _categoryStr;
    private final Question _question;

    @FXML
    private Label categoryLabel, valueLabel, winnings;
    @FXML
    private TextField textField;

    @FXML
    private Label timeLeft;

    @FXML
    private ProgressBar timeLeftBar;

    @FXML
    private Polyline confirm;


    private MouseEvent _e;





    /**
     * Takes in the question and category that is being asked
     *
     * @param question    the question
     * @param categoryStr the name of the category
     */
    public AskQuestionController(Question question, String categoryStr) {
        _categoryStr = categoryStr;
        _question = question;

    }

    CountdownTimer timer = new CountdownTimer();

    /**
     * This method will initialize the AskQuestion scene. It will control the display of slider bar, the current winnings,
     * play the clue by festival and the attempt times.
     */
    @FXML
    public void initialize() {
        super.initialize();
        // Display contents after 1.5 second
        PauseTransition pauseTransition = new PauseTransition(Duration.seconds(10));
        pauseTransition.setOnFinished( event -> {
            pressEnter();
        });
        pauseTransition.play();

            categoryLabel.setText(_categoryStr);
            valueLabel.setText("$" + _question.getValue());
            winnings.setText("$" + Database.getInstance().getWinnings());

            TTS.getInstance().speak(_question.getQuestionStr());

            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    textField.requestFocus();
                }
            });



            timeLeftBar.progressProperty().bind(timer.progressProperty());
            timeLeft.textProperty().bind(timer.messageProperty());
            new Thread(timer).start();






    }


    public  void pressEnter(){
        Robot robot = new Robot();
        robot.keyPress(KeyCode.ENTER);
        robot.keyRelease(KeyCode.ENTER);
    }




    /**
     * This method is invoked when the user press the replay button.
     * It will read the question again by festival.
     */
    @FXML
    public void replay() {
        TTS.getInstance().speak(_question.getQuestionStr());
    }

    /**
     * Validates the answer when the confirm arrow is clicked.
     * @param e the source of the click
     */
    @FXML
    public void confirm(MouseEvent e) {

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
        String userAnswer = textField.getText();
        if (_question.compareAnswers(userAnswer)) {
            Database.getInstance().addWinnings(_question.getValue());
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
     * This method is invoked when the user's input is incorrect.
     * It will switch to the incorrect scene and display the answer of the question.
     * @param e the event source
     */
    @FXML
    public void loadIncorrectScene(Event e) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/quinzical/scenes/play/Incorrect.fxml"));
        String answerTemp = Arrays.toString(_question.getAnswers());
        String answer = answerTemp.substring(1,answerTemp.length()-1);
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
