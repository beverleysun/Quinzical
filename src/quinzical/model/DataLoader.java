package quinzical.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.Writer;
import java.util.*;

/**
 * Class that loads in all the data from the save files
 *
 * @author Beverley Sun, Jinkai Zhang
 */
public class DataLoader {

    private final File categoriesFolder = new File("./categories");
    private final File saveFolder = new File("./.save");
    private final File scoresFile = new File("./.scores");
    private final File winningsFolder = new File("./.save/winnings");
    private final File voiceSettingsFolder = new File("./.save/voice-settings");
    private final File categoryIndexFolder = new File("./.save/category-index");
    private final File questionsIndexFolder = new File("./.save/questions-index/");
    private final File answeredFolder = new File("./.save/answered");
    private final File[] categoryFiles5 = new File[5];
    private final File[] allCategoryFiles = categoriesFolder.listFiles();

    private final List<Category> questionData = new ArrayList<>();
    private final List<Category> practiceQuestionData = new ArrayList<>();
    private final List<User> scores = new ArrayList<>();

    /**
     * Loads in all data from the save folder
     */
    public DataLoader() {
        createFileStructure();
        loadPracticeQuestions();
        loadVoice();
        loadScores();
    }

    /**
     * If the save folder doesn't exist, generate its file structure and attach default values
     */
    private void createFileStructure() {
        try {
            if (!scoresFile.exists()) {
                scoresFile.createNewFile();
            }

            if (!saveFolder.exists()) {
                // Make all the required directories
                answeredFolder.mkdirs();
                winningsFolder.mkdir();
                categoryIndexFolder.mkdir();
                questionsIndexFolder.mkdir();
                voiceSettingsFolder.mkdir();

                // Set winnings to $0
                new File("./.save/winnings/0").createNewFile();

                // Set default voice speed to 1x
                new File("./.save/voice-settings/1").createNewFile();
                new File("./.save/voice-settings/settings.scm").createNewFile();
                FileWriter writer = new FileWriter("./.save/voice-settings/settings.scm");
                writer.write("(voice_akl_nz_jdt_diphone)");
                writer.close();

                // Create folders for each category in the save folder
                for (String name : Objects.requireNonNull(categoriesFolder.list())) {
                    new File("./.save/answered/" + name).mkdir();
                }
                new File("./.save/answered/International").mkdir();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Get 5 random international question and add to the question data list
     */
    private void loadInternationalQuestions() {
        InternationalQuestionGenerator generator = new InternationalQuestionGenerator();

        Category cat = new Category("International");
        Map<String, String> questionAndAnswers = generator.getFiveInternationQAndAs();
        int questionValue = 500;

        // Iterate through all 5 questions and answers
        for (Map.Entry<String, String> entry : questionAndAnswers.entrySet()) {
            // Get question and answer
            String questionStr = entry.getKey();
            String[] answer = new String[]{entry.getValue()};

            boolean answered = isAnswered("International", questionValue);
            boolean available = isAvailable("International", questionValue);

            Question question = new Question(questionStr, answer, questionValue, answered, available);
            cat.addQuestion(question);

            questionValue = questionValue - 100; // decrease question value for the next question
        }
        questionData.add(cat);
    }

    /**
     * Load in all questions from the categories folder as they are all able to be answered in the practice module
     */
    public void loadPracticeQuestions() {
        try {
            assert allCategoryFiles != null;
            for(File categoryFile : allCategoryFiles) {

                // Read every line of category file
                Category category = new Category(categoryFile.getName());
                BufferedReader reader = new BufferedReader(new FileReader(categoryFile));

                // Load in all questions
                String questionLine;
                while ((questionLine = reader.readLine()) != null) {
                    String[] questionData = parseQuestionLine(questionLine);

                    String question = questionData[0].trim();
                    String answer = questionData[2].trim();
                    String[] answers = answer.split("/");

                    category.addQuestion(new Question(question, answers));
                }
                practiceQuestionData.add(category);
                reader.close();
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Load in all the questions to be used in the game module. Loads 5 categories with 5 questions each.
     */
    public void loadQuestions() {
        try {
            // Load in the saved categories
            loadCategories();

            // Loop through each category
            for(File categoryFile : categoryFiles5) {
                Category category = new Category(categoryFile.getName());

                // Get the total number of lines of the file
                LineNumberReader lineNum = new LineNumberReader(new FileReader(categoryFile));
                lineNum.skip(Long.MAX_VALUE);

                // Get five random line numbers and store it into a file.
                if (!checkQuestionIndexFile(categoryFile.getName())) {
                    createRandomNumFile(lineNum.getLineNumber() + 1, 1, questionsIndexFolder +"/"+ categoryFile.getName());
                }

                // Load the question line numbers from the category file.
                int[] fiveQuestionsIndices = getFiveQuestionsIndices(categoryFile.getName());

                // Select 5 questions for each category
                for (int i = 0; i < 5; i ++) {
                    // This will return the line that match the random number array.
                    assert fiveQuestionsIndices != null;
                    String questionLine = readAppointedLineNumber(categoryFile, fiveQuestionsIndices[i] ,lineNum.getLineNumber());

                    // Parse the question
                    assert questionLine != null;
                    String [] questionData = parseQuestionLine(questionLine);
                    int value = 500 - 100*i;
                    String question = questionData[0];
                    String answer = questionData[2];
                    String[] answers = answer.split("/");

                    boolean answered = isAnswered(categoryFile.getName(), value);
                    boolean available = isAvailable(categoryFile.getName(),value);

                    category.addQuestion(new Question(question, answers, value, answered, available));
                }
                questionData.add(category);
                lineNum.close();
            }
            loadInternationalQuestions();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Load in all past player scores
     */
    public void loadScores() {
        try {
            //Read every line of scores file
            BufferedReader reader = new BufferedReader(new FileReader(scoresFile));

            // Load in all scores
            String scoreLine;
            while ((scoreLine = reader.readLine()) != null) {
                String[] scoreData = scoreLine.split(",");
                String name = scoreData[0].trim();
                int score = Integer.parseInt(scoreData[1].trim());
                scores.add(new User(name, score));
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Load all the voice settings i.e. speed and accent
     */
    private void loadVoice() {
        File[] voiceSettings = voiceSettingsFolder.listFiles();

        assert voiceSettings != null;
        Arrays.sort(voiceSettings);
        TTS.getInstance().initMultiplier(Double.parseDouble(voiceSettings[0].getName()));
        try {
            // Read the first line of the voice settings file. This is where the accent is stored
            BufferedReader reader = new BufferedReader(new FileReader(voiceSettings[1]));
            String accent = reader.readLine();
            TTS.getInstance().setAccent(accent);
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Check if question index file exists
     * @param fileName the file name to check for
     * @return true if it exists, false otherwise
     */
    private boolean checkQuestionIndexFile(String fileName) {
        return new File("./.save/questions-index/" + fileName ).exists();
    }

    /**
     * Get the question line numbers for a category in the save folder
     * @param fileName the category to check for
     * @return an array of line numbers of type int
     */
    private int[] getFiveQuestionsIndices(String fileName) {
        try {
            BufferedReader in = new BufferedReader(new FileReader("./.save/questions-index/" + fileName));
            String s = in.readLine();
            String[] temp = s.split(",");

            int[] fiveQuestionsIndices = new int[5];

            for (int i = 0; i < 5; i++) {
                fiveQuestionsIndices[i] = Integer.parseInt(temp[i]);
            }
            in.close();
            return fiveQuestionsIndices;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Loads in the 5 chosen categories from the save file
     */
    private void loadCategories() {
        try {
            BufferedReader in = new BufferedReader(new FileReader("./.save/category-index/category-index"));
            String s = in.readLine();
            String[] temp = s.split(",");

            for (int i=0; i<5; i++) {
                assert allCategoryFiles != null;
                categoryFiles5[i] = allCategoryFiles[Integer.parseInt(temp[i])];
            }
            in.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Create a file with 5 random numbers within a given range
     * @param max the maximum random number
     * @param min the minimum random number
     * @param path the file path to write into
     */
    private void createRandomNumFile(int max, int min, String path) {
        try {
            int[] variable = getFiveRandomNumbers(max, min);

            // Create the file to store the category index and read it.
            new File(path).createNewFile();
            Writer wr = new FileWriter(path);
            for (int i = 0; i < 5; i++) {
                wr.write(variable[i] + ",");
            }
            wr.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Read the specified line number of a file
     * @param categoryFile the file to be read from
     * @param lineNumber the line line number to be read
     * @param totalLines the total number of lines of the file
     * @return the string at the appointed line number
     */
    private String readAppointedLineNumber(File categoryFile, int lineNumber, int totalLines) {
        try {
            FileReader in = new FileReader(categoryFile);
            LineNumberReader reader = new LineNumberReader(in);

            // Loop through each line of the file
            for(int i = 0; i < totalLines; i++) {
                String s = reader.readLine();
                if (reader.getLineNumber() == lineNumber) {
                    return s;
                }
            }
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Generate 5 non-repeated random numbers in a given range.
     * @param max the maximum number
     * @param min the minimum number
     * @return an array of 5 random numbers
     */
    private int[] getFiveRandomNumbers(int max, int min) {
        max = max - 1;
        int len = max-min+1;

        int[] source = new int[len];
        for (int i = min; i < min+len; i++){
            source[i-min] = i;
        }

        int[] result = new int[5];
        Random rd = new Random();
        int index;
        for (int i = 0; i < result.length; i++) {
            index = Math.abs(rd.nextInt() % len--);
            result[i] = source[index];
            source[index] = source[len];
        }
        return result;
    }

    /**
     * Separate a question line into 3 parts, delimited by "(" and ")"
     * @param str the question to be split
     * @return an array of type String with the split question
     */
    private static String[] parseQuestionLine(String str) {
        String[] splitQuestion = str.split("[()]");

        String question = splitQuestion[0].trim(); // the question itself
        String hint = splitQuestion[1].trim(); // the (what is), (who is) part
        String answer = splitQuestion[2].trim(); // the answer

        splitQuestion[0] = question;
        splitQuestion[1] = hint;
        splitQuestion[2] = answer;

        return splitQuestion;
    }

    /**
     * Only the lowest value not answered in each category should be available. Check for the availability of a question
     * value for a given category
     * @param category the category to check for
     * @param value the value to check for
     * @return true if available, false otherwise
     */
    public boolean isAvailable(String category, int value) {
        if (value == 100 && !isAnswered(category, value)) {
            return true;
        }
        // Check that the question value below is answered and that this question is not answered
        return isAnswered(category, value - 100) && !isAnswered(category, value);
    }

    /**
     * Check if a question value has been answered
     * @param category the category to check
     * @param value the value to check
     * @return true is answered, false otherwise
     */
    private boolean isAnswered(String category, int value) {
        return new File("./.save/answered/" + category + "/" + value).exists();
    }

    /**
     * Get all the questions for practice module
     * @return the list of all practice questions
     */
    public List<Category> getPracticeQuestionData() {
        return practiceQuestionData;
    }

    /**
     * Get the game module question data
     * @return the game module question data
     */
    public List<Category> getQuestionData() {
        return questionData;
    }

    /**
     * Get the list of past users and their scores
     * @return the list of past users
     */
    public List<User> getScores() {
        return scores;
    }
}
