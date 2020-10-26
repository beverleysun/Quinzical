package quinzical.model;

import java.io.*;
import java.util.*;

/**
 * Represents the place where all questions and categories are stored. Contains methods that can access
 * questions and also save data to be used later
 *
 * @author Beverley Sun, Jinkai Zhang
 */
public class Database {
	private final File saveFolder = new File("./.save");
	private final File winningsFolder = new File("./.save/winnings");
	private final File voiceSettingsFolder = new File("./.save/voice-settings");

	DataLoader loader = new DataLoader();

	private final List<Category> questionData = loader.getQuestionData();
	private final List<Category> practiceQuestionData = loader.getPracticeQuestionData();
	private final List<User> scores = loader.getScores();

	private static Database database;

	/**
	 * Initiate the database. Can only be instantiated within the scope of this class
	 */
	private Database(){
		// User has already chosen categories, so can load in the questions
		if (new File("./.save/category-index/category-index").exists()) {
			loadQuestionData();
		}
	}

	/**
	 * We only want one database object so is a singleton class
	 * @return the singleton Database object
	 */
	public static Database getInstance() {
		if (database == null ) {
			database = new Database();
		}
		return database;
	}

	public void loadQuestionData() {
		loader.loadQuestions();
	}

	/**
	 * Find the Category object by the category name and return it.
	 */
	public Category getCategoryByName(String name){
		for(Category category : practiceQuestionData){
			if(name.equals(category.getCategoryName())){
				return category;
			}
		}
		return null;
	}

	/**
	 * Saves user selected categories into a file
	 * @param categories the list of categories to save
	 */
	public void saveCategories(ArrayList<Category> categories) {
		loadCategoryIndex(getCategoryIndices(categories));
	}

	/**
	 * Get positions (indices) of categories in the practice questions list given a list of categories
	 * @param category list of categories to check for
	 * @return the positions
	 */
	private int[] getCategoryIndices(ArrayList<Category> category) {
		int[] categoryIndex = new int[5];
		for (int i = 0; i < 5; i++) {
			String name = category.get(i).getCategoryName();
			for (int j = 0; j < practiceQuestionData.size(); j++) {
				if (name.equals(practiceQuestionData.get(j).getCategoryName())) {
					categoryIndex[i] = j;
				}
			}
		}
		return categoryIndex;
	}

	/**
	 * Create the file that stores the category indices to continue from in the next game
	 * @param index the array of indices to store
	 */
	private void loadCategoryIndex(int[] index) {
		try {
			new File("./.save/category-index/category-index").createNewFile();
			Writer wr = new FileWriter("./.save/category-index/category-index");

			for (int k = 0; k < 5; k++) {
				wr.write(index[k] + ",");;
			}
			wr.close();
		} catch (IOException ioException) {
			ioException.printStackTrace();
		}
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
	 * Questions are randomly selected in the practice module. Select a random one from a given category
	 * @param categoryStr the category we want the question for
	 * @return the random question
	 */
	public Question findRandomPracticeQuestionByCategory(String categoryStr) {
		for (Category category : practiceQuestionData) {
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
		String[] winnings = winningsFolder.list();
		return Integer.parseInt(winnings[0]);
	}

	/**
	 * Add winnings to the save folder
	 * @param value the value to increment the current winnings by
	 */
	public void addWinnings(int value){
		String[] winningsStr = winningsFolder.list();
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
		for (Category category : questionData) {
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
		File[] voiceSettings = voiceSettingsFolder.listFiles();

		assert voiceSettings != null;
		Arrays.sort(voiceSettings); // Sort so that we know the speed file is in the 0th index
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
		deleteDirectory(saveFolder);
		database = new Database();
	}

	/**
	 * Saves the winnings under someone's name
	 * @param name the name for the winnings to be saved under
	 */
	public void addScore(String name) {
		scores.add(new User(name, getWinnings()));

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

	/**
	 * Get the list of past users sorted by their scores
	 * @return the sorted list of past users
	 */
	public List<User> getSortedScores() {
		scores.sort(new User.UserComparator());
		return scores;
	}
}