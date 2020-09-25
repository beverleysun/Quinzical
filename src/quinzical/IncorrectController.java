package quinzical;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

public class IncorrectController extends ConfirmController {

    @FXML
    private Label answerLabel;

    @FXML
    private Button nextButton;

    private String _answer;

    public IncorrectController(String answer) {
        super();
        _answer =  answer;
    }

    @FXML
    public void initialize() {
        super.initialize();
        answerLabel.setText("The correct answer was " + _answer);
        TTS.getInstance().speak("Oops. The correct answer was " + _answer);
        nextButton.setOnMouseClicked(this::toQuestionBoard);
    }

    public void toQuestionBoard(MouseEvent e) {
        super.toQuestionBoard(e);
    }
}
