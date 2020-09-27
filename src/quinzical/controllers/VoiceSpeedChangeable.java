package quinzical.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import quinzical.TTS;

public abstract class VoiceSpeedChangeable {
    @FXML
    private Slider voiceSlider;

    @FXML
    private Label speedDisplay;

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
