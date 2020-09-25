package quinzical;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

import java.io.IOException;

public class AskQuestionController {

    private String _questionStr;
    private String _answerStr;
    private String _categoryStr;
    private int _value;

    @FXML
    private Label questionInfo, winnings;

    @FXML
    private Slider voiceSlider;

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
        questionInfo.setText("Playing " + _categoryStr + " for $" + _value);
        voiceSlider.setValue(TTS.getInstance().getMultiplier());
        winnings.setText("$" + Database.getInstance().getWinnings());
        TTS.getInstance().speak(_questionStr);

        confirm.setOnMouseClicked(this::confirm);
        giveUp.setOnMouseClicked(this::loadIncorrectScene);

    }

    public void replay() {
        TTS.getInstance().speak(_questionStr);
    }

    public void confirm(MouseEvent e) {
        String userAnswer = textField.getText();
        if (_answerStr.equals(userAnswer)) {
            Database.getInstance().addWinnings(_value);
            loadCorrectScene(e);
        } else {
            loadIncorrectScene(e);
        }
    }

    public void loadIncorrectScene(MouseEvent e) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Incorrect.fxml"));
        loader.setController(new IncorrectController(_answerStr));
        try {
            Parent incorrect = loader.load();
            SceneChanger.changeScene(e, incorrect);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    public void loadCorrectScene(MouseEvent e){
        try {
            Parent correct = FXMLLoader.load(getClass().getResource("Correct.fxml"));
            SceneChanger.changeScene(e, correct);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    public void sliderChanged() {
        TTS.getInstance().setMultiplier(voiceSlider.getValue());
    }

}
