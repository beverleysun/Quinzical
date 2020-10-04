package quinzical;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Database {
	private final File _saveFolder = new File("./.save");
	private final File _categoryIndexFolder = new File("./.save/category-index");
	private final File _questionsIndexFolder = new File("./.save/questions-index/");
	private final File _answeredFolder = new File("./.save/answered");
	private final File _winningsFolder = new File("./.save/winnings");
	private final File _categoriesFolder = new File("./categories");
	private final File _voiceSettingsFolder = new File("./.save/voice-settings");

	// Represents all categories
	private final File[] _allCategoryFiles = _categoriesFolder.listFiles();

	// Represents 5 random categories
	private final File[] _categoryFiles5 = new File[5];

	// Question data for the 5 random categories
	private final List<Category> _questionData = new ArrayList<Category>();

	//This arraylist is for store all questions in practice mode.
	private final List<Category> _practiceQuestionData = new ArrayList<>();

	private static Database _database;

	/**
	 * Initiate the database. Can only be instantiated within the scope of this class
	 */
	private Database(){
		createFileStructure();
		loadQuestions();
		loadPracticeQuestions();
		loadVoice();
	}

	/**
	 * We only want one database object so is a singleton class
	 * @return the singleton Database object
	 */
	public static Database getInstance() {
		if (_database == null ) {
			_database = new Database();
		}
		return _database;
	}

	/**
	 * Load in all questions from the categories folder as they are all able to be answered in the practice module
	 */
	public void loadPracticeQuestions() {
		try {
			for(File categoryFile : _allCategoryFiles) {

				//Read every line of category file
				Category category = new Category(categoryFile.getName());
				BufferedReader reader = new BufferedReader(new FileReader(categoryFile));

				// Load in all questions
				String questionLine;
				while ((questionLine = reader.readLine()) != null) {
					String[] questionData = parseQuestionLine(questionLine);
					String question = questionData[0].trim();
					String answer = questionData[2].trim();
					String[] answers = answer.split("/");

					category.addQuestion(new Question(question,answers));
				}
				_practiceQuestionData.add(category);
			}
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Get all the questions for practice module
	 * @return the list of all practice questions
	 */
	public List<Category> getPracticeQuestionData() {
		return _practiceQuestionData;
	}

	/**
	 * Questions are randomly selected in the practice module. Select a random one from a given category
	 * @param categoryStr the category we want the question for
	 * @return the random question
	 */
	public Question findRandomPracticeQuestionByCategory(String categoryStr) {
		for (Category category : _practiceQuestionData) {
			if (category.getCategoryName().equals(categoryStr)) {
				Random rand = new Random();
				List<Question> questions = category.getQuestions();
				return (questions.get(rand.nextInt(questions.size())));
			}
		}
		return null;
	}

	/**
	 * Load in all the questions to be used in the game module. Should load 5 categories with 5 questions each.
	 */
	private void loadQuestions() {
		try {
			// Load in the saved categories
			loadCategories();

			// Loop through each category
			for(File categoryFile : _categoryFiles5) {
				Category category = new Category(categoryFile.getName());

				// Get the total number of lines of the file
				LineNumberReader lineNum = new LineNumberReader(new FileReader(categoryFile));
				lineNum.skip(Long.MAX_VALUE);

				// Get five random line numbers and store it into a file.
				if (!checkQuestionIndexFile(categoryFile.getName())) {
					createRandomNumFile(lineNum.getLineNumber() + 1, 1, _questionsIndexFolder +"/"+ categoryFile.getName());
				}

				// Load the question line numbers from the category file.
				int[] fiveQuestionsIndices = getFiveQuestionsIndices(categoryFile.getName());

				// Select 5 questions for each category
				for (int i = 0; i < 5; i ++) {
					// This will return the line that match the random number array.
					String questionLine = readAppointedLineNumber(categoryFile, fiveQuestionsIndices[i] ,lineNum.getLineNumber());

					// Parse the question
					String [] questionData = parseQuestionLine(questionLine);
					int value = 500 - 100*i;
					String question = questionData[0];
					String answer = questionData[2];
					String[] answers = answer.split("/");

					boolean answered = isAnswered(categoryFile.getName(), value);
					boolean available = isAvailable(categoryFile.getName(),value);

					category.addQuestion(new Question(question, answers, value, answered, available));
				}

				_questionData.add(category);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Get the game module question data
	 * @return the game module question data
	 */
	public List<Category> getQuestionData() {
		return _questionData;
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
				_categoryFiles5[i] = _allCategoryFiles[Integer.parseInt(temp[i])];
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
			int[] Variable = getFiveRandomNumbers(max, min);
			// Create the file to store the category index and read it.
			new File(path).createNewFile();
			Writer wr = new FileWriter(path);
			for (int i = 0; i < 5; i++) {
				wr.write(Variable[i] + ",");
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
	 * @return
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
		String[] splitQuestion = str.split("[\\(\\)]");

		String question = splitQuestion[0].trim();
		String hint = splitQuestion[1].trim();
		String answer = splitQuestion[2].trim();

		splitQuestion[0] = question;
		splitQuestion[1] = hint;
		splitQuestion[2] = answer;

		return splitQuestion;
	}

	/**
	 * If the save folder doesn't exist, generate its file structure and attach default values
	 */
	private void createFileStructure() {
		try {
			if (!_saveFolder.exists()) {
				// Make all the required directories
				_answeredFolder.mkdirs();
				_winningsFolder.mkdir();
				_categoryIndexFolder.mkdir();
				_questionsIndexFolder.mkdir();
				_voiceSettingsFolder.mkdir();
				createRandomNumFile(_categoriesFolder.listFiles().length, 0, "./.save/category-index/category-index");

				// Set winnings to $0
				new File("./.save/winnings/0").createNewFile();

				// Set default voice speed to 1x
				new File("./.save/voice-settings/1").createNewFile();
				new File("./.save/voice-settings/settings.scm").createNewFile();
				FileWriter writer = new FileWriter("./.save/voice-settings/settings.scm");
				writer.write("(voice_akl_nz_jdt_diphone)");
				writer.close();

				// Create folders for each category in the save folder
				for (String name : _categoriesFolder.list()) {
					new File("./.save/answered/" + name).mkdir();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Load all the voice settings i.e. speed and accent
	 */
	private void loadVoice() {
		File[] voiceSettings = _voiceSettingsFolder.listFiles();
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
	 * Check if a question value has been answered
	 * @param category the category to check
	 * @param value the value to check
	 * @return true is answered, false otherwise
	 */
	private boolean isAnswered(String category, int value) {
		return new File("./.save/answered/" + category + "/" + value).exists();
	}

	/**
	 * Record a completed question in the save folder
	 * @param category the category it was completed in
	 * @param value the value of the question that was completed
	 */
	public void addCompletedFile(String category, String value) {
		try {
			new File("./.save/answered/" + category + "/" + value).createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Get the current winnings
	 * @return the current winnings
	 */
	public int getWinnings() {
		String[] winnings = _winningsFolder.list();
		return Integer.parseInt(winnings[0]);
	}

	/**
	 * Add winnings to the save folder
	 * @param value the value to increment the current winnings by
	 */
	public void addWinnings(int value){
		String[] winningsStr = _winningsFolder.list();
		int winningsInt = Integer.parseInt(winningsStr[0]);
		int newWinnings = winningsInt + value;
		new File("./.save/winnings/"+ winningsInt).renameTo(new File("./.save/winnings/"+ newWinnings));
	}

	/**
	 * Check if the game has been completed i.e. if all questions have been answered in the game module
	 * @return true if completed, false otherwise
	 */
	public boolean gameCompleted() {
		// Loop through all questions
		for (Category category : _questionData) {
			for (Question question : category.getQuestions()) {
				if (!question.isCompleted()) {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * Save the voice speed settings in the save folder
	 * @param speed the speed to save it to
	 */
	public void updateSpeed(double speed) {
		File[] voiceSettings = _voiceSettingsFolder.listFiles();
		Arrays.sort(voiceSettings);
		voiceSettings[0].renameTo(new File("./.save/voice-settings/" + speed));
	}

	/**
	 * Helper function to reset. Recursively delete a directory
	 * @param directoryToBeDeleted the directory to be deleted
	 */
	public void deleteDirectory(File directoryToBeDeleted) {
		File[] allContents = directoryToBeDeleted.listFiles();

		// Recursively delete
		if (allContents != null) {
			for (File file : allContents) {
				deleteDirectory(file);
			}
		}
		directoryToBeDeleted.delete();
	}

	/**
	 * Reset the game. Just deletes the save folder
	 */
	public void reset() {
		deleteDirectory(_saveFolder);
		_database = new Database();
	}
}