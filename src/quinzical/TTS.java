package quinzical;

import java.io.IOException;

public class TTS {

    private static TTS _tts;

    private double _speedMultiplier;
    private double _averageSpeed = 130;

    private TTS() {}

    // Singleton class
    public static TTS getInstance() {
        if (_tts == null) {
            _tts = new TTS();
        }
        return _tts;
    }

    public void speak (String str) {
        int speed = (int) (_speedMultiplier*_averageSpeed);
        String command = "espeak -s " + speed + " \"" + str + "\"";
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
}
