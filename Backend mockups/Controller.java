package jeopardy;

import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Controller {
	private final File _saveFolder = new File("./.save");
	private final File _answeredFolder = new File("./.save/answered");
	private final File _winningsFolder = new File("./.save/winnings");
	private final File _categoriesFolder = new File("./categories");

	//Pre-selected to created the categoryFiles for the game mode.Add the random select method.  
	//This is the 5 random index whthin the range.
	private int[] _FiveQuestionsIndex;
	//This is all the files under the category folder. 
	private final File[] _allCategoryFiles = _categoriesFolder.listFiles();
	//This is the 5 files that matches the index.
	private File[] _categoryFiles = new File[5];


	private final List<Category> _questionData = new ArrayList<>();


	private final int _numCats;

	private static Controller _controller;

	private Controller() {
		loadQuestions();
		_numCats = _questionData.size();
	}

	//*******************************************************************************
	//Add the randomly select method. 
	private File[] getFiveCategories() {
		_FiveQuestionsIndex = this.getFiveRandomNumbers();
		for (int i=0; i<5; i++) {
			_categoryFiles[i] = _allCategoryFiles[_FiveQuestionsIndex[i]];
		}	
		return _categoryFiles;
	}
	
	//Select five lines randomly from a file. 
	//private File getFiveLines(File category) {
		
		
		//return 
	//}

	private int[] getFiveRandomNumbers() {
		int max = _categoriesFolder.listFiles().length - 1;
		int min = 0;
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
		_FiveQuestionsIndex = result;
		return _FiveQuestionsIndex; 
 
	}


	//*******************************************************************************


	public static Controller getInstance() {
		if (_controller == null ) {
			_controller = new Controller();
		}

		return _controller;
	}

	private void loadQuestions() {

		try {
			_categoryFiles = this.getFiveCategories();
			createFileStructure();
			
			for(File categoryFile : _categoryFiles) {
				// Read every line of category file
				BufferedReader reader = new BufferedReader(new FileReader(categoryFile));
				Category category = new Category(categoryFile.getName());
				
				// Pick five questions.
				
				String questionLine;

				while((questionLine = reader.readLine()) != null) {
					String[] questionData = questionLine.split(",");

					// Extract information from the question line
					int value = Integer.parseInt(questionData[0].trim());
					String question = questionData[1].trim();
					String answer = questionData[2].trim();

					// Check if question has been answered
					boolean answered = isAnswered(categoryFile.getName(), value);

					category.addQuestion(new Question(question, answer, value, answered));
				}

				_questionData.add(category);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void createFileStructure() throws IOException {
		if(!_saveFolder.exists()) {
			_answeredFolder.mkdirs();
			_winningsFolder.mkdir();
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