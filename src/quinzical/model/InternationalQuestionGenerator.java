package quinzical.model;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

/**
 * Generates international trivia questions from the opentdb api
 *
 * @author Beverley Sun, Jinkai Zhang
 */
public class InternationalQuestionGenerator {

    private final Map<String, String> charCodes = new HashMap<>()
    {{
        put("&quot;", "\"");
        put("&#039;", "'");
        put("&amp;", "&");
        put("&acute;", "`");
        put("&eacute;", "é");
        put("&oacute;", "ó");
        put("&pound;", "£");
        put("&aacute;", "á");
        put("&Aacute;", "Á");
        put("&ntilde;", "ñ");
        put("&rdquo;", "\"");
        put("&ouml;", "ö");
    }};

    /**
     * Calls the api and gets an international trivia question
     * @return an international trivia question and its answer
     */
    public String[] getInternationQAndA() {
        String[] questionAndAnswer = new String[2];

        Runnable background = new Runnable() {
            @Override
            public void run() {
                try {

                    // Connect to the API
                    URL url = new URL("https://opentdb.com/api.php?amount=1&category=9&type=multiple");
                    URLConnection connection = url.openConnection();
                    connection.connect();

                    // Get question data from JSON
                    JsonElement element = JsonParser.parseReader(new InputStreamReader((InputStream) connection.getContent())); // Convert the input stream to a json element
                    JsonObject json = element.getAsJsonObject();
                    String rawQuestion = json.get("results").getAsJsonArray().get(0).getAsJsonObject().get("question").getAsString();
                    String rawAnswer = json.get("results").getAsJsonArray().get(0).getAsJsonObject().get("correct_answer").getAsString();

                    // Clean up data (replace &amp, &quot, etc. stuff with their actual characters)
                    questionAndAnswer[0] = cleanCharCodes(rawQuestion);
                    questionAndAnswer[1] = cleanCharCodes(rawAnswer);

                } catch (IOException e) {
                    System.out.println("Error getting international question");
                }
            }
        };
        new Thread(background).start();
        return questionAndAnswer;
    }

    /**
     * Cleans up the string. Replaces &amp, &quot, etc. stuff with their actual characters
     * @param str the string to clean
     * @return the cleaned string
     */
    private String cleanCharCodes(String str) {
        for (Map.Entry<String, String> entry : charCodes.entrySet()) {
            // Replace codes with their respective characters
            str = str.replaceAll(entry.getKey(), entry.getValue());
        }
        return str;
    }
}
