package quinzical.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import quinzical.TTS;

public abstract class VoiceSettingsChangeable {

    @FXML
    private Slider voiceSlider;

    @FXML
    private Label speedDisplay;

    @FXML
    private RadioButton nzAccent;

    @FXML
    private RadioButton usAccent;

    private ToggleGroup accents;


    @FXML
    public void setAccent(MouseEvent e) {
        accents = new ToggleGroup();
        nzAccent.setToggleGroup(accents);
        usAccent.setToggleGroup(accents);

        if (nzAccent.isSelected()) {
            TTS.getInstance().setAccent("(voice_akl_nz_jdt_diphone)");
        }
        else  {
            TTS.getInstance().setAccent("(voice_kal_diphone)");
        }
    }

    public void initialize() {
        voiceSlider.setValue(TTS.getInstance().getMultiplier());
    }

    public void sliderChanged() {
        TTS.getInstance().setMultiplier(voiceSlider.getValue());
        displaySpeed();
    }

    public void displaySpeed() {
        speedDisplay.setText(TTS.getInstance().getMultiplier() + "x");
    }

    public void removeSpeed() {
        speedDisplay.setText("");
    }

}
