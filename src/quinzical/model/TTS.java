package quinzical.model;

import java.io.FileWriter;
import java.io.IOException;

/**
 * Class where all text-to-speech occurs
 *
 * @author Beverley Sun, Jinkai Zhang
 */
public class TTS {

    private static TTS tts;
    private double speedMultiplier;
    private String accent;
    private Process process;

    /**
     * Cannot be instantiated outside of the scope of this class
     */
    private TTS() {}

    /**
     * Singleton class so only ever returns one object of TTS
     * @return an object of type TTS
     */
    public static TTS getInstance() {
        if (tts == null) {
            tts = new TTS();
        }
        return tts;
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
            // End old speaking process so that the new one can be played
            if (process != null) {
                process.descendants().forEach(ProcessHandle::destroy);
                process.destroy();
            }
            process = pb.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Set the voice speed multiplier
     * @param multiplier the value for it to be set to
     */
    public void setMultiplier(double multiplier) {
        speedMultiplier = multiplier;

        // Save the speed in the save folder
        Database.getInstance().updateSpeed(multiplier);
    }

    /**
     * Initiate the multiplier when the game first starts up.
     * @param multiplier the value for it to be set to
     */
    public void initMultiplier(double multiplier){
        speedMultiplier = multiplier;
    }

    /**
     * Gets the voice speed multiplier
     * @return the voice speed multiplier of type double
     */
    public double getMultiplier() {
        return speedMultiplier;
    }

    /**
     * Generates the scm file for festival
     * @param input the text that festival will speak
     */
    private void strToText(String input) {
        try {
            FileWriter writer = new FileWriter("./.save/voice-settings/settings.scm");
            writer.write(accent + "\n");
            writer.write("(Parameter.set 'Duration_Stretch " + (1 / speedMultiplier) + ")" + "\n");
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
        this.accent = accent;
        strToText("Updated voice");
    }

    /**
     * Gets the user specified accent
     * @return the accent name
     */
    public String getAccent() {
        return accent;
    }

}
