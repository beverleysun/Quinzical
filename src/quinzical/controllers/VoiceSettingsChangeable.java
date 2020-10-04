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

    /**
     * Sets the accent of the voice that is speaking (US or NZ)
     */
    @FXML
    public void setAccent() {
        if (nzAccent.isSelected()) {
            TTS.getInstance().setAccent("(voice_akl_nz_jdt_diphone)");
        } else {
            TTS.getInstance().setAccent("(voice_kal_diphone)");
        }
    }

    /**
     * Initialize voice speed slider and accent radio buttons for the scenes where voice settings are changeable
     */
    public void initialize() {
        voiceSlider.setValue(TTS.getInstance().getMultiplier());

        ToggleGroup accents = new ToggleGroup();
        nzAccent.setToggleGroup(accents);
        usAccent.setToggleGroup(accents);
        if (TTS.getInstance().getAccent().equals("(voice_akl_nz_jdt_diphone)")) {
            nzAccent.setSelected(true);
        } else {
            usAccent.setSelected(true);
        }
    }

    /**
     * Triggered when the voice slider is changed. Updates the speed to be spoken at and displays the speed to the user
     */
    @FXML
    public void sliderChanged() {
        TTS.getInstance().setMultiplier(voiceSlider.getValue());
        displaySpeed();
    }

    /**
     * Displays the voice speed to the user
     */
    public void displaySpeed() {
        speedDisplay.setText(TTS.getInstance().getMultiplier() + "x");
    }

    /**
     * Removes the voice speed label
     */
    public void removeSpeed() {
        speedDisplay.setText("");
    }
}
