package quinzical.controllers.play;

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
import quinzical.Database;
import quinzical.Question;
import quinzical.SceneChanger;
import quinzical.TTS;
import quinzical.controllers.VoiceSettingsChangeable;

import java.io.IOException;
import java.util.Arrays;

public class AskQuestionController extends VoiceSettingsChangeable {

    private final String _categoryStr;
    private final Question _question;

    @FXML
    private Label categoryLabel, valueLabel, winnings;

    @FXML
    private TextField textField;

    public AskQuestionController(Question question, String categoryStr) {
        _categoryStr = categoryStr;
        _question = question;
    }

    /* This method will control the initial AskQuestion scene. It will control the display of slider bar, the current winnings,
     * play the clue by festival and the attempt times. */
    @FXML
    public void initialize() {
        super.initialize();
        categoryLabel.setText(_categoryStr);
        valueLabel.setText("$" + _question.getValue());
        winnings.setText("$" + Database.getInstance().getWinnings());

        TTS.getInstance().speak(_question.getQuestion());
    }

    /* This method is invoked when the user press the replay button.
     * It will read the question again by festival.  */
    @FXML
    public void replay() {
        TTS.getInstance().speak(_question.getQuestion());
    }

    /* This method is invoked when the user click the confirm arrow, it will invoke the check answer method. */
    @FXML
    public void confirm(MouseEvent e) {
        validateAnswer(e);
    }

    /* This method is invoked when the text field is active. If the user press enter key, it will invoke
     * the check answer method.  */
    @FXML
    public void enter(KeyEvent e) {
        if (e.getCode() == KeyCode.ENTER) {
            validateAnswer(e);
        }
    }

    /* This method compare the user's input with the actual answer in the database.*/
    private void validateAnswer(Event e) {
        String userAnswer = textField.getText();
        if (_question.compareAnswers(userAnswer)) {
            Database.getInstance().addWinnings(_question.getValue());
            loadCorrectScene(e);
        } else {
            loadIncorrectScene(e);
        }
    }

    /* This method is invoked when the user press the macron letter button */
    @FXML
    public void addMacron(MouseEvent e) {
        String macron = ((Button) e.getSource()).getText();
        textField.appendText(macron);
    }

    /* This method is invoked when the user's input is incorrect.
     * It will switch to the incorrect scene and display the answer of the question.*/
    @FXML
    public void loadIncorrectScene(Event e) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../../scenes/play/Incorrect.fxml"));
        String answerTemp = Arrays.toString(_question.getAnswer());
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
    /* This method is invoked when the user's input is correct.
     * It will switch to the correct scene. */
    public void loadCorrectScene(Event e){
        try {
            // Load the "correct" scene
            Parent correct = FXMLLoader.load(getClass().getResource("../../scenes/play/Correct.fxml"));
            SceneChanger.changeScene(e, correct);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
}
