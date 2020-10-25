package quinzical.controllers.play;

import javafx.fxml.FXML;
import quinzical.model.TTS;

/**
 * Controls the scene when the user gets the answer right
 *
 * @author Beverley Sun, Jinkai Zhang
 */
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
