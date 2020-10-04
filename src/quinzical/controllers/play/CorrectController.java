package quinzical.controllers.play;

import javafx.fxml.FXML;
import quinzical.TTS;

public class CorrectController extends ConfirmController {

    /**
     * Initialize the "correct" scene. It will display and read the word "Correct"
     */
    @FXML
    public void initialize() {
        super.initialize();
        TTS.getInstance().speak("Correct");
    }
}
