package quinzical;

import java.io.File;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Database {
	private final File _saveFolder = new File("./.save");
	private final File _winningsFolder = new File("./.save/winnings");
	private final File _voiceSettingsFolder = new File("./.save/voice-settings");

	private final List<Category> _questionData;
	private final List<Category> _practiceQuestionData;
	private final List<User> _scores;

	private static Database _database;

	/**
	 * Initiate the database. Can only be instantiated within the scope of this class
	 */
	private Database(){
		DataLoader loader = new DataLoader();
		_questionData = loader.getQuestionData();
		_practiceQuestionData = loader.getPracticeQuestionData();
		_scores = loader.getScores();
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
	 * Get all the questions for practice module
	 * @return the list of all practice questions
	 */
	public List<Category> getPracticeQuestionData() {
		return _practiceQuestionData;
	}

	/**
	 * Get the game module question data
	 * @return the game module question data
	 */
	public List<Category> getQuestionData() {
		return _questionData;
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

	/**
	 * Saves the winnings under someone's name
	 * @param name the name for the winnings to be saved under
	 */
	public void addScore(String name) {
		_scores.add(new User(name, getWinnings()));

		try {
			File file = new File("./.scores");
			FileWriter fr = new FileWriter(file, true);
			BufferedWriter br = new BufferedWriter(fr);
			br.write(name + "," + getWinnings() + "\n");

			br.close();
			fr.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public List<User> getSortedScores() {
		Collections.sort(_scores, new User.UserComparator());
		return _scores;
	}
}