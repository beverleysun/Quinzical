package quinzical.controllers.play;

import javafx.fxml.FXML;
import quinzical.TTS;

import java.io.IOException;

public class CorrectController extends ConfirmController {

    @FXML
    public void initialize() throws IOException {
        super.initialize();
        TTS.getInstance().speak("Correct");
    }
}
