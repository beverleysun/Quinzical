package quinzical;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Database {
	private final File _saveFolder = new File("./.save");
	private final File _categoryIndexFolder = new File("./.save/category-index");
	private final File _questionsIndexFolder = new File("./.save/questions-index/");
	private final File _answeredFolder = new File("./.save/answered");
	private final File _winningsFolder = new File("./.save/winnings");
	private final File _categoriesFolder = new File("./categories");
	private final File _voiceSpeedFolder = new File("./.save/voice-speed");
	private final File _practiceQuestionsFolder = new File("./.save/practice-questions/");
	private final File _practiceQuestionIndex = new File("./.save/practice-questions-index/");

	// Represents all categories
	private final File[] _allCategoryFiles = _categoriesFolder.listFiles();

	// Represents 5 random categories
	private final File[] _categoryFiles5 = new File[5];

	// Question data for the 5 random categories
	private final List<Category> _questionData = new ArrayList<Category>();

	//This arraylist is for store all questions in practice mode.
	private final List<Category> _practiceQuestionData = new ArrayList<>();

	//This is the attempted time for practice question.
	private int _attempted = 0;

	private int _practiceQuestionsIndex;

	private static Database _database;

	private Database(){
		loadQuestions();
		loadPracticeQuestions();
		loadVoiceSpeed();
	}

	public static Database getInstance() {
		if (_database == null ) {
			_database = new Database();
		}
		return _database;
	}

	public void savePracticeIndex(String category, int Index) {
		try {
			if (!new File("./.save/practice-questions-index/" + category).exists()) {
				new File("./.save/practice-questions-index/" + category).createNewFile();
				FileWriter writer = new FileWriter(("./.save/practice-questions-index/" + category));
				writer.write(Integer.toString(Index));
				writer.close();
			}
		} catch (IOException e){
			e.printStackTrace();
		}
	}

	private int getPracticeQuestionsIndex(String fileName) {
		try {
			BufferedReader in = new BufferedReader(new FileReader("./.save/practice-questions-index/" + fileName));
			String s = in.readLine();
			int PracticeQuestionsIndex = Integer.parseInt(s);
			in.close();
			return PracticeQuestionsIndex;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return -1;
	}

	public Question findQuestion(String categoryToFind) {
		for (Category category : _practiceQuestionData) {
			if (category.getCategoryName().equals(categoryToFind)) {
				Question question = category.getQuestions().get(0);

				return question;
			}
		}
		return null;
	}

	//Read the file and get the attempted times of the question.
	public int getAttemptedTimes(String fileName) {
		try {
			String s = Files.readString(Path.of("./.save/practice-questions/" + fileName), StandardCharsets.UTF_8);
			int attempted = Integer.parseInt(s);
			_attempted = attempted;
			return _attempted;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return 0;
	}
	//If the practice question is attempted, create a file and store the attempted times.
	public void isAttempted (String category) {
		try {
			if (!new File("./.save/practice-questions/" + category).exists()) {
				new File("./.save/practice-questions/" + category).createNewFile();
				FileWriter writer = new FileWriter(("./.save/practice-questions/" + category));
				writer.write("1");
				writer.close();
			} else {

				int t = getAttemptedTimes(category);
				FileWriter writer = new FileWriter(("./.save/practice-questions/" + category));
				writer.write(Integer.toString(1 + t));
				writer.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public List<Category> getPracticeQuestionData() {
		return _practiceQuestionData;
	}

	public void loadPracticeQuestions() {
		try {
			createFileStructure();
			for(File categoryFile : _allCategoryFiles) {
				//Read every line of category file
				Category category = new Category(categoryFile.getName());

				//get the total length of the file.
				LineNumberReader lineNum = new LineNumberReader(new FileReader(categoryFile));
				lineNum.skip(Long.MAX_VALUE);

				// create a random number between 1 to lineNum.getLineNumber().
				// Save the number to PracticeQuestionIndex folder.
				if(!new File("./.save/practice-questions/"+ categoryFile.getName()).exists() || !new File("./.save/practice-questions-index/"+ categoryFile.getName()).exists()) {
					_practiceQuestionsIndex = (int) (Math.random() * (lineNum.getLineNumber() - 1 + 1) + 1);
					savePracticeIndex(categoryFile.getName(), _practiceQuestionsIndex);
				}

				if(new File("./.save/practice-questions/"+ categoryFile.getName()).exists()){
					_practiceQuestionsIndex = getPracticeQuestionsIndex(categoryFile.getName());
				}

				//This will return the line that match the random number array.
				String questionLine = this.readAppointedLineNumber(categoryFile, _practiceQuestionsIndex,lineNum.getLineNumber());

				String[] questionData = parseQuestionLine(questionLine);

				// Extract information from the question line

				String question = questionData[0].trim();
				String hint = questionData[1].trim();
				String answer = questionData[2].trim();
				String[] answers = answer.split("/");


				if (new File("./.save/practice-questions/"+ categoryFile.getName()).exists()) {
					_attempted = getAttemptedTimes(categoryFile.getName());

					if (_attempted == 2) {
						new File("./.save/practice-questions-index/"+ categoryFile.getName()).delete();
					}

					if(_attempted >= 3) {
						new File("./.save/practice-questions/"+ categoryFile.getName()).delete();
					}
				}

				category.addQuestion(new Question(question,answers,hint,_attempted));
				_practiceQuestionData.add(category);
			}
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	// Check if the question is available
	public boolean isAvailable(String category, int value) {
		if (value == 100 && !isAnswered(category, value)) {
			return true;
		}
		// Check that the question value below is answered and that this question is not answered
		return isAnswered(category, value - 100) && !isAnswered(category, value);
	}

	// Check if question index file exists
	private boolean checkQuestionIndexFile(String fileName) {
		return new File("./.save/questions-index/" + fileName ).exists();
	}

	// Read the file and put the numbers into _categoryFile.
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

	// Loads in the 5 chosen categories
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

	// Get five random numbers and write it into a file
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

	// Read the appointed line number of a file.
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

	private int[] getFiveRandomNumbers(int max, int min) {
		max = max - 1;
		int len = max-min+1;

		int[] source = new int[len];
		for (int i = min; i < min+len; i++){
			source[i-min] = i;
		}

		int[] result = new int[5];
		Random rd = new Random();
		int index = 0;
		for (int i = 0; i < result.length; i++) {
			index = Math.abs(rd.nextInt() % len--);
			result[i] = source[index];
			source[index] = source[len];
		}
		return result;
	}

	private void loadQuestions() {
		try {
			createFileStructure();
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
					//This will return the line that match the random number array.
					String questionLine = readAppointedLineNumber(categoryFile, fiveQuestionsIndices[i] ,lineNum.getLineNumber());

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

	// change to separate the line into 3 parts by "(" and ")".
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

	public List<Category> getQuestionData() {
		return _questionData;
	}

	private void createFileStructure() {
		try {
			if (!_saveFolder.exists()) {
				_answeredFolder.mkdirs();
				_winningsFolder.mkdir();
				_categoryIndexFolder.mkdir();
				_questionsIndexFolder.mkdir();

				_practiceQuestionsFolder.mkdir();
				_practiceQuestionIndex.mkdir();

				_voiceSpeedFolder.mkdir();
				createRandomNumFile(_categoriesFolder.listFiles().length, 0, "./.save/category-index/category-index");

				// Set winnings to $0
				new File("./.save/winnings/0").createNewFile();

				// Set default voice speed to 130
				new File("./.save/voice-speed/1").createNewFile();

				// Create folders for each category in the save folder
				for (String name : _categoriesFolder.list()) {
					new File("./.save/answered/" + name).mkdir();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void loadVoiceSpeed() {
		String[] voiceSpeed = _voiceSpeedFolder.list();
		TTS.getInstance().initMultiplier(Double.parseDouble(voiceSpeed[0]));
	}

	private boolean isAnswered(String category, int value) {
		return new File("./.save/answered/" + category + "/" + value).exists();
	}



	public void addCompletedFile(String category, String value) {
		try {
			new File("./.save/answered/" + category + "/" + value).createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public int getWinnings() {
		String[] winnings = _winningsFolder.list();
		return Integer.parseInt(winnings[0]);
	}

	public void addWinnings(int value){
		String[] winningsStr = _winningsFolder.list();
		int winningsInt = Integer.parseInt(winningsStr[0]);
		int newWinnings = winningsInt + value;
		new File("./.save/winnings/"+ winningsInt).renameTo(new File("./.save/winnings/"+ newWinnings));
	}

	public boolean gameCompleted() {
		for (Category category : _questionData) {
			for (Question question : category.getQuestions()) {
				if (!question.isCompleted()) {
					return false;
				}
			}
		}
		return true;
	}

	public void updateSpeed(double speed) {
		File[] voiceSpeedFile = _voiceSpeedFolder.listFiles();
		voiceSpeedFile[0].renameTo(new File("./.save/voice-speed/" + speed));
	}

	public void deleteDirectory(File directoryToBeDeleted) {
		File[] allContents = directoryToBeDeleted.listFiles();
		if (allContents != null) {
			for (File file : allContents) {
				deleteDirectory(file);
			}
		}
		directoryToBeDeleted.delete();
	}

	public void reset() {
		deleteDirectory(_saveFolder);
		_questionData.clear();
		_practiceQuestionData.clear();
		loadQuestions();
		loadPracticeQuestions();
	}
}