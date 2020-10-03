package quinzical.controllers.play;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import quinzical.Database;
import quinzical.Question;
import quinzical.SceneChanger;
import quinzical.TTS;
import quinzical.controllers.VoiceSettingsChangeable;

import java.io.IOException;
import java.util.Arrays;

public class AskQuestionController extends VoiceSettingsChangeable {

    private final String _questionStr;
    private final String[] _answerStr;
    private final String _categoryStr;
    private final int _value;

    private Question _question;

    @FXML
    private Label questionInfo, winnings;

    @FXML
    private Button confirm, giveUp;

    @FXML
    private TextField textField;

    public AskQuestionController(Question question, String questionStr, String[] answerStr, String categoryStr, int value) {
        _questionStr = questionStr;
        _answerStr = answerStr;
        _categoryStr = categoryStr;
        _value = value;
        _question = question;
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

    public void replay() throws IOException {
        TTS.getInstance().speak(_questionStr);
    }

    public void confirm(MouseEvent e) {
        // Validate the user answer
        String userAnswer = textField.getText();
        if (_question.compareAnswers(userAnswer)) {
            Database.getInstance().addWinnings(_value);
            loadCorrectScene(e);
        } else {
            loadIncorrectScene(e);
        }
    }

    public void loadIncorrectScene(MouseEvent e) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../../scenes/play/Incorrect.fxml"));
        String answerTemp = Arrays.toString(_answerStr);
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
