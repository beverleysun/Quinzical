package quinzical;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class TTS {

    private static TTS _tts;

    private double _speedMultiplier;

    private String _accent;

    private TTS() {}

    // Singleton class
    public static TTS getInstance() {
        if (_tts == null) {
            _tts = new TTS();
        }
        return _tts;
    }

    public void speak(String str) {
        strToText(str);
        String command = "festival -b ./.save/voice-settings/settings.scm \n wait ";
        ProcessBuilder pb = new ProcessBuilder("bash", "-c", command);
        try {
            Process process = pb.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setMultiplier(double multiplier) {
        _speedMultiplier = multiplier;
        Database.getInstance().updateSpeed(multiplier);
    }

    public void initMultiplier(double multiplier){
        _speedMultiplier = multiplier;
    }

    public double getMultiplier() {
        return _speedMultiplier;
    }

    //generate the .scm file for festival.
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

    public void setAccent(String accent){
        _accent = accent;
        strToText("Updated voice");
    }

    public String getAccent() {
        return _accent;
    }

}
