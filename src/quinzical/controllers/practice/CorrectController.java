package quinzical.controllers.practice;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import quinzical.TTS;

import java.io.IOException;

public class CorrectController extends ConfirmController {

    @FXML
    private Button next;
    @FXML
    private Label correctLabel;

    @FXML
    public void initialize() throws IOException {
        super.initialize();
        TTS.getInstance().speak("Correct!");
    }

    @FXML
    private void backToQuestion(MouseEvent e) {
        backToPractice(e);
    }
}