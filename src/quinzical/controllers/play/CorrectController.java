package quinzical.controllers.play;

import javafx.fxml.FXML;
import quinzical.TTS;

public class CorrectController extends ConfirmController {

    @FXML
    public void initialize() {
        super.initialize();
        TTS.getInstance().speak("Correct");
    }

}
