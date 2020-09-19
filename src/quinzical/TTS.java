package quinzical;

import java.io.IOException;

public class TTS {

    private static TTS _tts;

    private TTS() {}

    // Singleton class
    public static TTS getInstance() {
        if (_tts == null) {
            _tts = new TTS();
        }
        return _tts;
    }

    public void speak (String str, int speedMultiplier) {
        String command = "espeak -" + speedMultiplier + "\"" + str + "\"";
        ProcessBuilder pb = new ProcessBuilder("bash", "-c", command);
        try {
            Process process = pb.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
