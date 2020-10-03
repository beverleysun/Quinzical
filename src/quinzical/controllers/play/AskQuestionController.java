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

    @FXML
    public void initialize() {
        super.initialize();
        categoryLabel.setText(_categoryStr);
        valueLabel.setText("$" + _question.getValue());
        winnings.setText("$" + Database.getInstance().getWinnings());

        TTS.getInstance().speak(_question.getQuestion());
    }

    @FXML
    public void replay() {
        TTS.getInstance().speak(_question.getQuestion());
    }

    @FXML
    public void confirm(MouseEvent e) {
        // Validate the user answer
        String userAnswer = textField.getText();
        if (_question.compareAnswers(userAnswer)) {
            Database.getInstance().addWinnings(_question.getValue());
            loadCorrectScene(e);
        } else {
            loadIncorrectScene(e);
        }
    }

    @FXML
    public void addMacron(MouseEvent e) {
        String macron = ((Button) e.getSource()).getText();
        textField.appendText(macron);
    }

    @FXML
    public void loadIncorrectScene(MouseEvent e) {
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
