package quinzical.controllers.play;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import quinzical.Database;
import quinzical.SceneChanger;
import quinzical.TTS;
import quinzical.controllers.VoiceSpeedChangeable;

import java.io.IOException;

public class AskQuestionController extends VoiceSpeedChangeable {

    private final String _questionStr;
    private final String _answerStr;
    private final String _categoryStr;
    private final int _value;

    @FXML
    private Label questionInfo, winnings;

    @FXML
    private Button confirm, giveUp;

    @FXML
    private TextField textField;

    public AskQuestionController(String questionStr, String answerStr, String categoryStr, int value) {
        _questionStr = questionStr;
        _answerStr = answerStr;
        _categoryStr = categoryStr;
        _value = value;

    }

    @FXML
    public void initialize() {
        super.initialize();
        questionInfo.setText("Playing " + _categoryStr + " for $" + _value);
        winnings.setText("$" + Database.getInstance().getWinnings());
        TTS.getInstance().speak(_questionStr);

        confirm.setOnMouseClicked(this::confirm);
        giveUp.setOnMouseClicked(this::loadIncorrectScene);
    }

    public void replay() {
        TTS.getInstance().speak(_questionStr);
    }

    public void confirm(MouseEvent e) {
        // Validate the user answer
        String userAnswer = textField.getText();
        if (_answerStr.equals(userAnswer)) {
            Database.getInstance().addWinnings(_value);
            loadCorrectScene(e);
        } else {
            loadIncorrectScene(e);
        }
    }

    public void loadIncorrectScene(MouseEvent e) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../../scenes/play/Incorrect.fxml"));
        loader.setController(new IncorrectController(_answerStr));
        try {
            // Load the "incorrect" scene
            Parent incorrect = loader.load();
            SceneChanger.changeScene(e, incorrect);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    public void loadCorrectScene(MouseEvent e){
        try {
            // Load the "correct" scene
            Parent correct = FXMLLoader.load(getClass().getResource("../../scenes/play/Correct.fxml"));
            SceneChanger.changeScene(e, correct);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
}
