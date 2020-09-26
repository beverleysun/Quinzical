package quinzical;

import javafx.fxml.FXML;

public class CorrectController extends ConfirmController {

    @FXML
    public void initialize() {
        super.initialize();
        TTS.getInstance().speak("Correct");
    }

}
