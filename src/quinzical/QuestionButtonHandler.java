package quinzical;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class QuestionButtonHandler extends ButtonHandler {


    public QuestionButtonHandler(Stage stage, Scene scene) {
        super(stage, scene);
    }

    @Override
    public void handle(MouseEvent event) {
        // Extract information
        Button questionButton = (Button) event.getSource();
        String[] questionInfo = questionButton.getId().split(",");
        String category = questionInfo[0];
        String value = questionInfo[1];
        Question questionToAsk = _controller.findQuestion(category, value);
        

        // Save completed data
        questionToAsk.setCompleted(true);
        _controller.addCompletedFile(category, value);
        
        // After the question is answered, set it unavailable and set the question
        // that has a value 100 more available.
        questionToAsk.setAvailable(false);
        int valueInt = Integer.parseInt(value);
        if (valueInt < 500 ) {
        Question questionToSetAvailable = _controller.findQuestion(category,  Integer.toString((valueInt+100)));
        questionToSetAvailable.setAvailable(true);
        }
        

        Scene askQuestionScene = _sceneGenerator.getAskQuestionScene(_stage, _scene, category, questionToAsk);
        _controller.showScene(_stage, askQuestionScene);
        _controller.speak(questionToAsk.getQuestion());
    }
}
