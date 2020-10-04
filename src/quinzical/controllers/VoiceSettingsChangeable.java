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

    /** This method is invoked when the user click the Radio buttons(NZ or US).
     *  @param e the source of the click
     */
    @FXML
    public void setAccent(MouseEvent e) {
        if (nzAccent.isSelected()) {
            TTS.getInstance().setAccent("(voice_akl_nz_jdt_diphone)");
        } else {
            TTS.getInstance().setAccent("(voice_kal_diphone)");
        }
    }
    /**
     * This method will initialize the Accent selection radio buttons.
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
     * This method will set the new speed when the speed slider changes.
     */
    public void sliderChanged() {
        TTS.getInstance().setMultiplier(voiceSlider.getValue());
        displaySpeed();
    }
    /**
     * This method will display the speed value when user put the mouse on the slider.
     */
    public void displaySpeed() {
        speedDisplay.setText(TTS.getInstance().getMultiplier() + "x");
    }
    /**
     * This method will remove the speed value when user take the mouse off the slider.
     */
    public void removeSpeed() {
        speedDisplay.setText("");
    }
}
