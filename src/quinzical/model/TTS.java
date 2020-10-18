package quinzical.model;

import java.io.FileWriter;
import java.io.IOException;

public class TTS {

    private static TTS _tts;
    private double _speedMultiplier;
    private String _accent;

    /**
     * Cannot be instantiated outside of the scope of this class
     */
    private TTS() {}

    /**
     * Singleton class so only ever returns one object of TTS
     * @return an object of type TTS
     */
    public static TTS getInstance() {
        if (_tts == null) {
            _tts = new TTS();
        }
        return _tts;
    }

    /**
     * Use a bash process the speak a given string using festival and its settings
     * @param str the string the be spoken
     */
    public void speak(String str) {
        // Update voice settings
        strToText(str);

        // Load in the voice settings
        String command = "festival -b ./.save/voice-settings/settings.scm \n wait ";
        ProcessBuilder pb = new ProcessBuilder("bash", "-c", command);

        // Speak
        try {
            Process process = pb.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Set the voice speed multiplier
     * @param multiplier the value for it to be set to
     */
    public void setMultiplier(double multiplier) {
        _speedMultiplier = multiplier;

        // Save the speed in the save folder
        Database.getInstance().updateSpeed(multiplier);
    }

    /**
     * Initiate the multiplier when the game first starts up.
     * @param multiplier the value for it to be set to
     */
    public void initMultiplier(double multiplier){
        _speedMultiplier = multiplier;
    }

    /**
     * Gets the voice speed multiplier
     * @return the voice speed multiplier of type double
     */
    public double getMultiplier() {
        return _speedMultiplier;
    }

    /**
     * Generates the scm file for festival
     * @param input the text that festival will speak
     */
    private void strToText(String input) {
        try {
            FileWriter writer = new FileWriter("./.save/voice-settings/settings.scm");
            writer.write(_accent + "\n");
            writer.write("(Parameter.set 'Duration_Stretch " + (1 / _speedMultiplier) + ")" + "\n");
            writer.write("(SayText \"" + input + "\")");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sets the accent that the user selects
     * @param accent either us or nz (kal_diphone or jdt_diphone)
     */
    public void setAccent(String accent){
        _accent = accent;
        strToText("Updated voice");
    }

    /**
     * Gets the user specified accent
     * @return the accent name
     */
    public String getAccent() {
        return _accent;
    }

}
