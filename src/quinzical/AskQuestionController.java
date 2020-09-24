package quinzical;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class AskQuestionController {

    private String _questionStr;
    private String _answerStr;
    private String _categoryStr;
    private int _value;

    @FXML
    private Label questionInfo;

    public AskQuestionController(String questionStr, String answerStr, String categoryStr, int value) {
        _questionStr = questionStr;
        _answerStr = answerStr;
        _categoryStr = categoryStr;
        _value = value;
    }

    @FXML
    public void initialize() {
        questionInfo.setText("Playing " + _categoryStr + " for $" + _value);
    }


}
