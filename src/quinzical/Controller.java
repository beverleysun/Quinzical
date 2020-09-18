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
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Controller {
	private final File _saveFolder = new File("./.save");
	private final File _CategoryIndexFolder = new File("./.save/CategoryIndex");
	private final File _QuestionsIndexFolder = new File("./.save/QuestionsIndex/");
	private final File _answeredFolder = new File("./.save/answered");
	private final File _winningsFolder = new File("./.save/winnings");
	private final File _categoriesFolder = new File("./categories");
	private int[] _FiveQuestionsIndex = new int[5];

	// Represents all categories
	private final File[] _allCategoryFiles = _categoriesFolder.listFiles();

	// Represents 5 random categories
	private final File[] _categoryFiles5 = new File[5];

	// Question data for the 5 random categories
	private final List<Category> _questionData = new ArrayList<Category>();

	// Question data for all categories
	private final List<Category> _allQuestionData = new ArrayList<Category>();

	private final int _numCats;

	private static Controller _controller;

	private Controller(){
		loadQuestions();
		_numCats = _questionData.size();
	}

	public static Controller getInstance() {
		if (_controller == null ) {
			_controller = new Controller();
		}
		return _controller;
	}

	// Check if the question is available
	public boolean isAvailable(String category, int value) {
		if (value == 100 && !isAnswered(category, value)){
			return true;
		} else {
			return isAnswered(category, (value - 100)) && value != 100;
		}
	}

	// Check if question index file exists
	private boolean checkQuestionIndexFile(String fileName) {
		return new File("./.save/QuestionsIndex/" + fileName ).exists();
	}

	// Read the file and put the numbers into _categoryFile.
	private int[] getQuestions(String fileName) {
		try {
			BufferedReader in = new BufferedReader(new FileReader("./.save/QuestionsIndex/" + fileName));
			String s = in.readLine();
			String[] temp = s.split(",");

			for (int i = 0; i < 5; i++) {
				_FiveQuestionsIndex[i] = Integer.parseInt(temp[i]);
			}
			in.close();
			return _FiveQuestionsIndex;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	//Read the file and put the numbers into _categoryFile.
	private void loadCategories() {
		try {
			BufferedReader in = new BufferedReader(new FileReader("./.save/CategoryIndex/categoryIndex"));
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


	//*******************************************************************************



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
				_FiveQuestionsIndex = getQuestions(categoryFile.getName());

				// Use the for loop to select each question.
				for (int i = 0; i < 5; i ++) {
					//This will return the line that match the random number array.
					String questionLine = readAppointedLineNumber(categoryFile,_FiveQuestionsIndex[i] ,lineNum.getLineNumber());

					String [] questionData = parseQuestionLine(questionLine);
					int value = 500 - 100*i;
					String question = questionData[0];
					String answer = questionData[1];

					boolean answered = isAnswered(categoryFile.getName(), value);
					boolean available = isAvailable(categoryFile.getName(),value);

					category.addQuestion(new Question(question, answer, value, answered, available));
				}

				_questionData.add(category);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private String[] parseQuestionLine(String str) {
		String[] splitQuestion = str.split("\\(\\p{ASCII}*\\)");
		String question = splitQuestion[0].trim();
		String answer = splitQuestion[1].trim();

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
		splitQuestion[1] = answer;

		return splitQuestion;
	}

	private void createFileStructure() throws IOException {
		int[] _fiveCategoriesIndex = new int[5];

		if(!_saveFolder.exists()) {
			_answeredFolder.mkdirs();
			_winningsFolder.mkdir();
			_CategoryIndexFolder.mkdir();
			_QuestionsIndexFolder.mkdir();
			createRandomNumFile(_categoriesFolder.listFiles().length,0,"./.save/CategoryIndex/categoryIndex");

			// Set winnings to $0
			new File("./.save/winnings/0").createNewFile();

			// Create folders for each category in the save folder
			for (String name : _categoriesFolder.list()) {
				new File("./.save/answered/" + name).mkdir();
			}
		}
	}

	private boolean isAnswered(String category, int value) {
		return new File("./.save/answered/" + category + "/" + value).exists();
	}

	public List<Category> getQuestionData() {
		return _questionData;
	}

	public int getNumCats() {
		return _numCats;
	}

	public void showScene(Stage stage, Scene scene) {
		stage.setScene(scene);
		stage.show();
	}

	public int getWinnings() {
		String[] winnings = _winningsFolder.list();
		return Integer.parseInt(winnings[0]);
	}

	public void reset() {
		deleteDirectory(_saveFolder);
		_questionData.clear();
		loadQuestions();
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

	public void speak(String str) {
		String command = "espeak " + "\"" + str + "\"";
		ProcessBuilder pb = new ProcessBuilder("bash", "-c", command);
		try {
			Process process = pb.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}