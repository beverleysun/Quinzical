package quinzical;

import javafx.scene.Scene;
import javafx.stage.Stage;

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
	private final File _CategoryIndexFolder = new File("./.save/category-index");
	private final File _QuestionsIndexFolder = new File("./.save/questions-index/");
	private final File _answeredFolder = new File("./.save/answered");
	private final File _winningsFolder = new File("./.save/winnings");
	private final File _categoriesFolder = new File("./categories");
	private final File _voiceSpeedFolder = new File("./.save/voice-speed");
  
	private final File _PracticeQuestionsFolder = new File("./.save/PracticeQuestions/");
	private final File 	_PracticeQuestionIndex = new File("./.save/PracticeQuestionsIndex/");

	private int[] _FiveQuestionsIndex = new int[5];

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

	private int _PracticeQuestionsIndex;

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

	public void savePracticeIndex(String category, int Index) throws IOException {
		if (!new File("./.save/PracticeQuestionsIndex/"+ category).exists()) {
			new File("./.save/PracticeQuestionsIndex/"+ category).createNewFile();
			FileWriter writer = new FileWriter(("./.save/PracticeQuestionsIndex/"+ category));
			writer.write(Integer.toString(Index));
			writer.close();
		}
	}

	private int getPracticeQuestionsIndex(String fileName) {
		try {
			BufferedReader in = new BufferedReader(new FileReader("./.save/PracticeQuestionsIndex/" + fileName));
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
	public  int getAttemptedTimes(String fileName) throws IOException {
		String s = Files.readString(Path.of("./.save/PracticeQuestions/" + fileName), StandardCharsets.UTF_8);
		int attempted = Integer.parseInt(s);
		_attempted = attempted;
		return _attempted;

	}
	//If the practice question is attempted, create a file and store the attempted times.
	public void isAttempted (String category) throws IOException {

		if (!new File("./.save/PracticeQuestions/"+ category).exists()) {
			new File("./.save/PracticeQuestions/"+ category).createNewFile();
			FileWriter writer = new FileWriter(("./.save/PracticeQuestions/"+ category));
			writer.write("1");
			writer.close();
		}
		else {

			int t = getAttemptedTimes(category);
			FileWriter writer = new FileWriter(("./.save/PracticeQuestions/"+ category));
			writer.write(Integer.toString(1 + t));
			writer.close();}
	}

	public List<Category> getPracticeQuestionData() {
		return _practiceQuestionData;
	}

	public void loadPracticeQuestions() {

		try {
			createFileStructure();
			for(File categoryFile : _allCategoryFiles) {
				//Read every line of category file
				//BufferedReader reader = new BufferedReader(new FileReader(categoryFile));
				Category category = new Category(categoryFile.getName());

				//get the total length of the file.
				LineNumberReader lineNum = new LineNumberReader(new FileReader(categoryFile));
				lineNum.skip(Long.MAX_VALUE);

				// create a random number between 1 to lineNum.getLineNumber().
				// Save the number to PracticeQuestionIndex folder.
				if(!new File("./.save/PracticeQuestions/"+ categoryFile.getName()).exists() || !new File("./.save/PracticeQuestionsIndex/"+ categoryFile.getName()).exists()) {


					_PracticeQuestionsIndex  = (int) (Math.random() * (lineNum.getLineNumber() - 1 + 1) + 1);
					savePracticeIndex(categoryFile.getName(), _PracticeQuestionsIndex );
				}


				if(new File("./.save/PracticeQuestions/"+ categoryFile.getName()).exists()){
					_PracticeQuestionsIndex = getPracticeQuestionsIndex(categoryFile.getName());
				}

				//This will return the line that match the random number array.
				String questionLine = this.readAppointedLineNumber(categoryFile,_PracticeQuestionsIndex ,lineNum.getLineNumber());

				String[] questionData = parseQuestionLine(questionLine);

				// Extract information from the question line

				String question = questionData[0].trim();
				String hint = questionData[1].trim();
				String answer = questionData[2].trim();
				String[] answers = answer.split("/");


				if (new File("./.save/PracticeQuestions/"+ categoryFile.getName()).exists()) {
					_attempted = getAttemptedTimes(categoryFile.getName());


					if (_attempted == 2) {
						new File("./.save/PracticeQuestionsIndex/"+ categoryFile.getName()).delete();
					}

					if(_attempted >= 3) {
						new File("./.save/PracticeQuestions/"+ categoryFile.getName()).delete();

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
	private void getQuestions(String fileName) {
		try {
			BufferedReader in = new BufferedReader(new FileReader("./.save/questions-index/" + fileName));
			String s = in.readLine();
			String[] temp = s.split(",");

			for (int i = 0; i < 5; i++) {
				_FiveQuestionsIndex[i] = Integer.parseInt(temp[i]);
			}
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	//Read the file and put the numbers into _categoryFile.
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
	private void createRandomNumFile(int Max, int Min, String Path) {
		try {
			int[] Variable = getFiveRandomNumbers(Max, Min);
			// create the file to store the category index and read it.
			new File(Path).createNewFile();
			Writer wr = new FileWriter(Path);
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

			for(File categoryFile : _categoryFiles5) {
				Category category = new Category(categoryFile.getName());

				// Get the total number of lines of the file
				LineNumberReader lineNum = new LineNumberReader(new FileReader(categoryFile));
				lineNum.skip(Long.MAX_VALUE);

				// Get five random line numbers and store it into a file.
				if (!checkQuestionIndexFile(categoryFile.getName())) {
					createRandomNumFile(lineNum.getLineNumber() + 1, 1,_QuestionsIndexFolder +"/"+ categoryFile.getName());
				}

				// Load the question index from the file.
				getQuestions(categoryFile.getName());

				// Use the for loop to select each question.
				for (int i = 0; i < 5; i ++) {
					//This will return the line that match the random number array.
					String questionLine = readAppointedLineNumber(categoryFile,_FiveQuestionsIndex[i] ,lineNum.getLineNumber());

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

		// Get the last character of the question and answer strings
		String lastCharQ = question.substring(question.length() - 1);
		String lastCharA = answer.substring(answer.length() - 1);


		// If the strings end with '.' or ',' then remove it
		if (lastCharQ.equals(".") || lastCharQ.equals(",")) {
			question = question.substring(0, question.length()-1).trim();
		}

		if (lastCharA.equals(".") || lastCharA.equals(",")) {
			answer = answer.substring(0, answer.length()-1).trim();
		}

		splitQuestion[0] = question;
		splitQuestion[1] = hint;
		splitQuestion[2] = answer;


		return splitQuestion;
	}

	private void createFileStructure() throws IOException {
		if(!_saveFolder.exists()) {
			_answeredFolder.mkdirs();
			_winningsFolder.mkdir();
			_CategoryIndexFolder.mkdir();
			_QuestionsIndexFolder.mkdir();

			_PracticeQuestionsFolder.mkdir();
			_PracticeQuestionIndex.mkdir();

			_voiceSpeedFolder.mkdir();
			createRandomNumFile(_categoriesFolder.listFiles().length,0,"./.save/category-index/category-index");


			// Set winnings to $0
			new File("./.save/winnings/0").createNewFile();

			// Set default voice speed to 130
			new File("./.save/voice-speed/1").createNewFile();

			// Create folders for each category in the save folder
			for (String name : _categoriesFolder.list()) {
				new File("./.save/answered/" + name).mkdir();
			}
		}
	}

	private void loadVoiceSpeed() {
		String[] voiceSpeed = _voiceSpeedFolder.list();
		TTS.getInstance().initMultiplier(Double.parseDouble(voiceSpeed[0]));
	}

	private boolean isAnswered(String category, int value) {
		return new File("./.save/answered/" + category + "/" + value).exists();
	}

	public List<Category> getQuestionData() {
		return _questionData;
	}

	public int getWinnings() {
		String[] winnings = _winningsFolder.list();
		return Integer.parseInt(winnings[0]);
	}

	public void reset() {
		deleteDirectory(_saveFolder);
		_questionData.clear();
		_practiceQuestionData.clear();
		loadQuestions();
		loadPracticeQuestions();
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

	public Question findQuestion(String categoryToFind, String valueToFind) {
		for (Category category : _questionData) {
			if (category.getCategoryName().equals(categoryToFind)) {
				for (Question question : category.getQuestions()) {
					if (question.getValueString().equals(valueToFind)) {
						return question;
					}
				}
			}
		}
		return null;
	}

	public void addCompletedFile(String category, String value) {
		try {
			new File("./.save/answered/" + category + "/" + value).createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
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
		voiceSpeedFile[0].renameTo(new File("./.save/voice-speed/"+speed));
	}
}