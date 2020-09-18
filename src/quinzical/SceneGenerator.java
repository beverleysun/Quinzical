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

public class SceneGenerator {
    private final Controller _controller = Controller.getInstance();
    private final List<Category> _questionData = _controller.getQuestionData();

    private SceneGenerator(){}

    private static SceneGenerator _sceneGenerator;

    public static SceneGenerator getInstance() {
        if (_sceneGenerator == null) {
            _sceneGenerator = new SceneGenerator();
        }
        return _sceneGenerator;
    }

    public Scene getStartScene(Stage stage, Scene scene) {
        // Init layout
        GridPane root = new GridPane();
        scene = new Scene(root, scene.getWidth(), scene.getHeight());

        // Components
        Label jeopardyLabel = new Label("JEOPARDY");
        Button startButton = new Button("Start");
        Button resetButton = new Button("Reset");

        // Alignment of content
        root.setAlignment(Pos.CENTER);
        root.setVgap(20);
        GridPane.setHalignment(startButton, HPos.CENTER);
        GridPane.setHalignment(resetButton, HPos.CENTER);
        GridPane.setHalignment(jeopardyLabel, HPos.CENTER);

        // Buttons
        startButton.setPrefWidth(100);
        startButton.setOnMouseClicked(new StartButtonHandler(stage, scene));

        resetButton.setPrefWidth(100);
        resetButton.setOnMouseClicked(new ResetButtonHandler(stage, scene));

        // Set styles
        scene.getStylesheets().add("assets/style.css");
        jeopardyLabel.getStyleClass().add("start-jeopardy");
        startButton.getStyleClass().add("start-button");
        resetButton.getStyleClass().add("reset-button");
        root.getStyleClass().add("background");

        // Add components to the scene
        root.addColumn(0, jeopardyLabel, startButton, resetButton);

        return scene;
    }

    public Scene getQuestionBoardScene(Stage stage, Scene scene) {
        int margin = 100;

        // Init layout
        StackPane root = new StackPane();
        scene = new Scene(root, scene.getWidth(), scene.getHeight());

        if (!Controller.getInstance().gameCompleted()) {
            GridPane questionBoard = new GridPane();
            root.getChildren().add(questionBoard);
            Label prompt = new Label("Choose a question");

            // Set gaps between cells
            questionBoard.setHgap(20);
            questionBoard.setVgap(20);
            questionBoard.add(prompt, 0, 0, _questionData.size(), 1);
            questionBoard.setAlignment(Pos.CENTER);

            GridPane.setHalignment(prompt, HPos.CENTER);
            prompt.getStyleClass().add("prompt");

            // Display category
            int colIdx = 0;
            for (Category category : _questionData) {
                Label categoryLabel = new Label(category.getCategoryName().toUpperCase());
                GridPane.setHalignment(categoryLabel, HPos.CENTER);
                categoryLabel.getStyleClass().add("category-label");

                questionBoard.add(categoryLabel, colIdx, 1, 1, 1);

                int rowIdx = 2;

                // Display question values under category name
                for (Question question : category.getQuestions()) {
                    Button questionButton = new Button("$" + question.getValueString());
                    questionButton.setOnMouseClicked(new QuestionButtonHandler(stage, scene));

                    // Set unique IDs
                    questionButton.setId(category.getCategoryName() + "," + question.getValue());
                    questionButton.getStyleClass().add("question-button");

                    questionButton.setOnMouseClicked(new QuestionButtonHandler(stage, scene));

                    // Bind sizes of buttons to window size
                    questionButton.prefWidthProperty().bind(scene.widthProperty().subtract(margin).divide(_questionData.size()));
                    questionButton.prefHeightProperty().bind(scene.heightProperty().subtract(margin).divide(_questionData.get(0).getQuestions().size() + 2));
                    questionButton.setMaxWidth(150);
                    questionButton.setMaxHeight(50);
                    GridPane.setHalignment(questionButton, HPos.CENTER);
                    
                    // Disable question button if question is not avaiable.
                    if (!question.isAvailable()) {
                        questionButton.setDisable(true);
                    }
                    // Disable question button if question already answered
                    if (question.isCompleted()) {
                        questionButton.setDisable(true);
                    }
                    questionBoard.add(questionButton, colIdx, rowIdx, 1, 1);
                    rowIdx++;
                }
                colIdx++;
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
        String questionStr = question.getQuestion();
        String answer = question.getAnswer();
        int value = question.getValue();

        // Init layout
        GridPane root = new GridPane();
        scene = new Scene(root, scene.getWidth(), scene.getHeight());

        // Components
        Label questionLabel = new Label(questionStr);
        Label questionInfo = new Label("Playing " + categoryStr.substring(0,1).toUpperCase() + categoryStr.substring(1) +  " for $" + value);
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
        root.add(questionInfo, 0, 1,  2, 1);
        root.add(answerInput, 0, 6,  1, 1);
        root.add(confirmButton, 1, 6,  1, 1);

        // Aligning all components to center
        GridPane.setHalignment(questionLabel, HPos.CENTER);
        GridPane.setHalignment(questionInfo, HPos.CENTER);
        GridPane.setHalignment(answerInput, HPos.RIGHT);
        GridPane.setHalignment(confirmButton, HPos.LEFT);

        // Wrap labels
        questionLabel.setWrapText(true);
        questionLabel.setTextAlignment(TextAlignment.CENTER);
        questionInfo.setWrapText(true);
        questionInfo.setTextAlignment(TextAlignment.CENTER);

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
        questionInfo.getStyleClass().add("question-info");
        answerInput.getStyleClass().add("answer-input");
        confirmButton.getStyleClass().add("confirm-button");

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
            validationLabel = new Label("Uh oh! The answer was " + answer);
            validationLabel.getStyleClass().add("incorrect-label");
        }
        validationLabel.getStyleClass().add("validation-label");

        Button backButton = new Button("Back");
        backButton.setId("to-questions");
        backButton.setOnMouseClicked(new BackButtonHandler(stage, scene));

        // Alignment
        root.setAlignment(Pos.CENTER);
        root.setVgap(20);
        root.addColumn(0, validationLabel, backButton);

        GridPane.setHalignment(validationLabel, HPos.CENTER);
        GridPane.setHalignment(backButton, HPos.CENTER);

        // Responsive
        validationLabel.setWrapText(true);
        validationLabel.setTextAlignment(TextAlignment.CENTER);

        // Styling
        scene.getStylesheets().add("assets/style.css");
        backButton.getStyleClass().add("back-button");
        root.getStyleClass().add("answering-stage");
        return scene;
    }
}
