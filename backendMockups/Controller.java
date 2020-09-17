package jeopardy;

import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Controller {
	private final File _saveFolder = new File("./.save");
	//new folders to store the random numbers. 
	private final File _CategoryIndexFolder = new File("./.save/CategoryIndex");
	private final File _QuestionsIndexFolder = new File("./.save/QuestionsIndex/");

	private final File _answeredFolder = new File("./.save/answered");
	private final File _winningsFolder = new File("./.save/winnings");
	private final File _categoriesFolder = new File("./categories");

	//Pre-selected to created the categoryFiles for the game mode.Add the random select method.  
	//This is the 5 random index whthin the range.
	private int[] _FiveCategoriesIndex;
	private int[] _FiveQuestionsIndex = new int[5];
	//This is all the files under the category folder. 
	private final File[] _allCategoryFiles = _categoriesFolder.listFiles();
	//This is the 5 files that matches the index.
	private final File[] _categoryFiles = new File[5];

	private final List<Category> _questionData = new ArrayList<Category>();

	private final int _numCats;

	private static Controller _controller;


	private Controller(){
		loadQuestions();
		_numCats = _questionData.size();
	}

	//*******************************************************************************
	//Check the question is avaliable or not 

	public boolean isAvailable(String category, int value) {

		if(value == 100 && !isAnswered(category, value)) 

		{return true;}


		else if (this.isAnswered(category, (value-100)) && value != 100 ) {
			return true;
		}
		else
			return false;	
	}
	//check the question index file
	private boolean checkQuestionIndexFile(String fileName) {
		return new File("./.save/QuestionsIndex/" + fileName ).exists();
	}
	//Read the file and put the numbers into _categoryFile.
	private int[] getQuestions(String fileName) throws IOException {
		BufferedReader in = new BufferedReader(new FileReader("./.save/QuestionsIndex/" + fileName));
		String s = in.readLine(); 
		String[] temp = s.split(",");
		
		for (int i=0; i<5; i++) {
			_FiveQuestionsIndex[i] = Integer.parseInt(temp[i]);
		}	
		in.close();
		return _FiveQuestionsIndex;
	}

	//Read the file and put the numbers into _categoryFile.
	private File[] getCategories() throws IOException {
		BufferedReader in = new BufferedReader(new FileReader("./.save/CategoryIndex/categoryIndex"));
		String s = in.readLine(); 
		String[] temp = s.split(",");

		for (int i=0; i<5; i++) {
			_categoryFiles[i] = _allCategoryFiles[Integer.parseInt(temp[i])];
		}	
		in.close();
		return _categoryFiles;
	}
	//get five random numbers and write it into file.
	private void createRandomNumFile(int Max, int Min, String Path, int[]Variable) throws IOException {
		Variable = this.getFiveRandomNumbers(Max,Min);
		//create the file to store  the category index and read it. 
		new File(Path).createNewFile();
		Writer wr = new FileWriter(Path);
		for (int i=0; i<5; i++) {
			wr.write(String.valueOf(Variable[i]) + ",");
		}
		wr.close();
	}

	// read the appointed line number of a file. 
	private String readAppointedLineNumber(File categoryFile, int lineNumber, int totalLines) throws IOException {

		FileReader in = new FileReader(categoryFile);  
		LineNumberReader reader = new LineNumberReader(in);  

		for(int i = 0; i < totalLines; i++) {
			String s = reader.readLine();	
			if (reader.getLineNumber() == lineNumber) {  
				return s;
			} 
		}

		in.close();
		return null;
	}

	private int[] getFiveRandomNumbers(int Max, int Min) {
		int max = Max - 1;
		int min = Min;
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


	public static Controller getInstance() {
		if (_controller == null ) {
			_controller = new Controller();
		}

		return _controller;
	}

	private void loadQuestions() {


		try {
			createFileStructure();
			getCategories();

			for(File categoryFile : _categoryFiles) {
				//Read every line of category file
				//BufferedReader reader = new BufferedReader(new FileReader(categoryFile));
				Category category = new Category(categoryFile.getName());

				//get the total length of the file.
				LineNumberReader lineNum = new LineNumberReader(new FileReader(categoryFile));
				lineNum.skip(Long.MAX_VALUE);

	
				//get five random line numbers and store it into a file.
				if (!checkQuestionIndexFile(categoryFile.getName())) {
					//_FiveQuestionsIndex = this.getFiveRandomNumbers(lineNum.getLineNumber() + 1,1);
					createRandomNumFile(lineNum.getLineNumber() + 1, 1,_QuestionsIndexFolder +"/"+ categoryFile.getName() ,_FiveQuestionsIndex );
				}
				//load the question index from the file.
					_FiveQuestionsIndex = getQuestions(categoryFile.getName());

					// Use the for loop to select each question.
					int value = 100;
					for (int i = 0; i < 5; i ++) {
						//This will return the line that match the random number array. 
						String questionLine = this.readAppointedLineNumber(categoryFile,_FiveQuestionsIndex[i] ,lineNum.getLineNumber());

						String[] questionData = questionLine.split(",");

						// Extract information from the question line


						//will not use the value from datat, will use the for loop add value form 100 to 500. 
						value = 500 - 100*i;


						String question = questionData[1].trim();
						String answer = questionData[2].trim();

						// Check if question has been answered
						boolean answered = isAnswered(categoryFile.getName(), value);

						//Check the question is avaiable or not 
						boolean available = isAvailable(categoryFile.getName(),value);

						category.addQuestion(new Question(question, answer, value, answered, available));


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
				_CategoryIndexFolder.mkdir();
				_QuestionsIndexFolder.mkdir();
				createRandomNumFile(_categoriesFolder.listFiles().length,0,"./.save/CategoryIndex/categoryIndex",_FiveCategoriesIndex);
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