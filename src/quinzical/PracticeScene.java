package quinzical;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.util.List;

public class PracticeScene {
	private final Database _database = Database.getInstance();
	private final List<Category> _questionData = _database.getPracticeQuestionData();

	private Label _questionInfo;
	private Label _questionhint;
	private int _unattemptedTime;
	String questionStr;
	
	Button categoryButton;

	Scene _askQuestionSence;
	
	
	private PracticeScene(){}

	private static PracticeScene _practiceScene;

	public static PracticeScene getInstance() {
		if (_practiceScene == null) {
			_practiceScene = new PracticeScene();
		}
		return _practiceScene;
	}


	public Scene getQuestionBoardScene(Stage stage, Scene scene) {
		int margin = 100;

		// Init layout
		StackPane root = new StackPane();
		scene = new Scene(root, scene.getWidth(), scene.getHeight());

		if (!_database.getInstance().gameCompleted(_database.getPracticeQuestionData())) {
			GridPane questionBoard = new GridPane();
			root.getChildren().add(questionBoard);
			Label prompt = new Label("Choose a category");

			// Set gaps between cells
			questionBoard.setHgap(20);
			questionBoard.setVgap(20);
			questionBoard.add(prompt, 0, 0, _questionData.size(), 1);
			questionBoard.setAlignment(Pos.CENTER);

			GridPane.setHalignment(prompt, HPos.CENTER);
			prompt.getStyleClass().add("prompt");
			// Display category
			int rol = 1; 
			for (Category _category : _questionData) {
				categoryButton = new Button(_category.getCategoryName().toUpperCase());

				categoryButton.setOnMouseClicked(new QuestionButtonHandler(stage, scene));

				// Set unique IDs
				categoryButton.setId(_category.getCategoryName() + "," + "practice");
				categoryButton.getStyleClass().add("question-button");

				categoryButton.setOnMouseClicked(new QuestionButtonHandler(stage, scene));

				// Bind sizes of buttons to window size
				categoryButton.prefWidthProperty().bind(scene.widthProperty().subtract(margin).divide(_questionData.size()));
				categoryButton.prefHeightProperty().bind(scene.heightProperty().subtract(margin).divide(_questionData.get(0).getQuestions().size() + 2));
				categoryButton.setMaxWidth(300);
				categoryButton.setMaxHeight(50);
				GridPane.setHalignment(categoryButton, HPos.CENTER);

//				// Disable question button if question is not avaiable.
//				if (((Question) category.getQuestions().get(0)).getAnsweredTimes() > 3 ) {
//					categoryButton.setDisable(true);
//				}
			
				questionBoard.add(categoryButton,1,rol);
				rol++;
			}
		
	} else {
		GridPane grid = new GridPane();

		// Labels
		Label gameCompletedLabel = new Label("You've completed the game!");
		Label pleaseResetLabel = new Label("Please reset to play again");

		grid.addColumn(0, gameCompletedLabel, pleaseResetLabel);

		// Layout
		grid.setAlignment(Pos.CENTER);
		GridPane.setHalignment(gameCompletedLabel, HPos.CENTER);
		GridPane.setHalignment(pleaseResetLabel, HPos.CENTER);
		gameCompletedLabel.getStyleClass().add("game-completed");
		pleaseResetLabel.getStyleClass().add("please-reset");
		grid.setVgap(20);

		root.getChildren().add(grid);
	}


	// Add back button at top right corner
	Button backButton = new Button("Back");
	backButton.setOnMouseClicked(new BackButtonHandler(stage, scene));
	backButton.setId("to-start");
	backButton.getStyleClass().add("back-button");
	StackPane.setAlignment(backButton, Pos.TOP_LEFT);
	StackPane.setMargin(backButton, new Insets(20, 20, 20, 20));
	root.getChildren().add(backButton);

	// Add winnings label at top right corner
	int winnings = _controller.getWinnings();
	Label winningsLabel = new Label("$" + winnings);

	if (winnings > 0) {
		winningsLabel.getStyleClass().add("winnings-pos");
	} else if (winnings < 0){
		winningsLabel.getStyleClass().add("winnings-neg");
	}
	winningsLabel.getStyleClass().add("winnings-label");
	StackPane.setAlignment(winningsLabel, Pos.TOP_RIGHT);
	StackPane.setMargin(winningsLabel, new Insets(20, 20, 20, 20));
	root.getChildren().add(winningsLabel);

	root.getStyleClass().add("background");
	scene.getStylesheets().add("assets/style.css");

	return scene;
}

public Scene getResetScene(Stage stage, Scene scene) {
	// Init layout
	GridPane root = new GridPane();
	scene = new Scene(root, scene.getWidth(), scene.getHeight());

	// Components
	Label prompt = new Label("Are you sure?");
	Button yesButton = new Button("Yes");
	Button noButton = new Button("No");

	// Alignment of content
	root.setAlignment(Pos.CENTER);
	root.setVgap(20);
	root.setHgap(20);
	GridPane.setHalignment(yesButton, HPos.CENTER);
	GridPane.setHalignment(noButton, HPos.CENTER);
	GridPane.setHalignment(prompt, HPos.CENTER);
	root.getColumnConstraints().add(new ColumnConstraints(130));
	root.getColumnConstraints().add(new ColumnConstraints(130));

	// Buttons
	yesButton.setPrefWidth(100);
	yesButton.setOnMouseClicked(new ResetButtonHandler(stage, scene));

	noButton.setPrefWidth(100);
	noButton.setOnMouseClicked(new ResetButtonHandler(stage, scene));

	// Set styles
	scene.getStylesheets().add("assets/style.css");
	prompt.getStyleClass().add("prompt");
	yesButton.getStyleClass().add("yes-button");
	noButton.getStyleClass().add("no-button");
	root.getStyleClass().add("background");

	// Add components to the scene
	root.add(prompt, 0,0,2,1);
	root.add(yesButton, 0, 1, 1, 1);
	root.add(noButton, 1, 1, 1, 1);

	return scene;
}

public Scene getAskQuestionScene(Stage stage, Scene scene, String categoryStr, Question question) {
	// Extract question information
	questionStr = question.getQuestion();
	String answer = question.getAnswer();
	String answerFisrLetter = Character.toString(question.getAnswer().charAt(0));
	String hint = question.getHint();
	
	 _unattemptedTime = 4 - question.getAnsweredTimes();
	int value = question.getValue();

	// Init layout
	GridPane root = new GridPane();
	scene = new Scene(root, scene.getWidth(), scene.getHeight());

	// Components
	Label questionLabel = new Label(questionStr);
	if(_unattemptedTime > 1) {
		_questionInfo = new Label("You have " + _unattemptedTime + " chances to answer the question from " + categoryStr.substring(0,1).toUpperCase() + categoryStr.substring(1));
	}
	else {
		_questionInfo = new Label("This is your last chances to answer the question from " + categoryStr.substring(0,1).toUpperCase() + categoryStr.substring(1));
		 _questionhint = new Label("Here is the hint, " + hint + " " + answerFisrLetter + "......??");
	}
	TextField answerInput = new TextField();
	Button confirmButton = new Button("Confirm");

	Scene finalScene = scene;
	confirmButton.setOnMouseClicked(e-> {
		new ConfirmButtonHandler(stage, finalScene, answer, answerInput.getText(), value).validateAnswer();
	});
	answerInput.setOnKeyPressed(keyEvent -> {
		if (keyEvent.getCode() == KeyCode.ENTER) {
			new ConfirmButtonHandler(stage, finalScene, answer, answerInput.getText(), value).validateAnswer();
		}
	});

	// Add components to grid
	root.add(questionLabel, 0, 0,  2, 1);
	root.add(_questionInfo, 0, 1,  2, 1);
	if(_unattemptedTime == 1) {
	root.add(_questionhint, 0, 2 ,2 ,1);
	}
	root.add(answerInput, 0, 6,  1, 1);
	root.add(confirmButton, 1, 6,  1, 1);

	// Aligning all components to center
	GridPane.setHalignment(questionLabel, HPos.CENTER);
	GridPane.setHalignment(_questionInfo, HPos.CENTER);
	if (_unattemptedTime == 1) {
	GridPane.setHalignment(_questionhint, HPos.CENTER);
	}
	GridPane.setHalignment(answerInput, HPos.RIGHT);
	GridPane.setHalignment(confirmButton, HPos.LEFT);

	// Wrap labels
	questionLabel.setWrapText(true);
	questionLabel.setTextAlignment(TextAlignment.CENTER);
	_questionInfo.setWrapText(true);
	_questionInfo.setTextAlignment(TextAlignment.CENTER);
	if(_unattemptedTime == 1) {
	_questionhint.setWrapText(true);
	_questionhint.setTextAlignment(TextAlignment.CENTER);
	}

	// Set layout on grid
	root.setAlignment(Pos.CENTER);
	root.setVgap(10);
	root.setHgap(20);
	root.setMaxWidth(100);
	ColumnConstraints column1 = new ColumnConstraints(100,100,Double.MAX_VALUE);
	column1.setHgrow(Priority.ALWAYS);
	root.getColumnConstraints().add(column1);
	root.setPadding(new Insets(70, 70,70,70));
	answerInput.setMinHeight(confirmButton.getHeight()+44);

	// Styling
	scene.getStylesheets().add("assets/style.css");
	root.getStyleClass().add("answering-stage");
	questionLabel.getStyleClass().add("prompt");
	_questionInfo.getStyleClass().add("question-info");
	if(_unattemptedTime == 1) {
	_questionhint.getStyleClass().add("prompt");
	}
	answerInput.getStyleClass().add("answer-input");
	confirmButton.getStyleClass().add("confirm-button");
	_askQuestionSence = scene;
	return scene;
}

public Scene getRightWrongScene(Stage stage, Scene scene, String answer, boolean correct) {
	// Init layout
	GridPane root = new GridPane();
	scene = new Scene(root, scene.getWidth(), scene.getHeight());

	// Components
	Label validationLabel;
	if (correct) {
		validationLabel = new Label("Correct!");
		validationLabel.getStyleClass().add("correct-label");
	} else {
		 if(_unattemptedTime > 1) {
			 validationLabel = new Label("Uh oh! The answer is incorrect. \n Please try again");
				validationLabel.getStyleClass().add("incorrect-label");
		 }
		 else {
		validationLabel = new Label( questionStr + " \n The answer was " + answer);
		validationLabel.getStyleClass().add("incorrect-label");
		 }
	}
	validationLabel.getStyleClass().add("validation-label");

	Button backButton = new Button("Back");
	Button backbt = new Button("Back");
	backbt.setId("to-questions");
	backbt.setOnMouseClicked(new BackButtonHandler(stage, scene));
	GridPane.setHalignment(backbt, HPos.CENTER);
	backbt.getStyleClass().add("back-button");
	
	 if(_unattemptedTime > 1 && !correct) {
			Button Tryagain = new Button("Try Again");
			Tryagain.setId(categoryButton.getId());
			Tryagain.setOnMouseClicked(new TryAgainButtonHandler(stage, scene));
			root.addColumn(0, validationLabel,backbt, Tryagain);
			GridPane.setHalignment(Tryagain, HPos.CENTER);
			Tryagain.getStyleClass().add("back-button");
			 }
	 else {
	 
	root.addColumn(0, validationLabel, backButton);
	backButton.setId("to-questions");
	backButton.setOnMouseClicked(new BackButtonHandler(stage, scene));
	GridPane.setHalignment(backButton, HPos.CENTER);
	backButton.getStyleClass().add("back-button");
	 }
	// Alignment
	root.setAlignment(Pos.CENTER);
	root.setVgap(20);


	GridPane.setHalignment(validationLabel, HPos.CENTER);
	


	// Responsive
	validationLabel.setWrapText(true);
	validationLabel.setTextAlignment(TextAlignment.CENTER);

	// Styling
	scene.getStylesheets().add("assets/style.css");
	
	 
	
	root.getStyleClass().add("answering-stage");
	return scene;
}

public Scene getAskQuestionScene(Stage _stage, Scene _scene) {
	
	return _askQuestionSence;
}
}
