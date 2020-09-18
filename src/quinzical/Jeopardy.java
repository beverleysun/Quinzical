package quinzical;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class Jeopardy extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        // Load assets
        Font.loadFont(getClass().getResourceAsStream("/assets/slkscr.ttf"), 16);
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/assets/logo.png")));

        // Set up stage area
        Scene scene = new Scene(new Pane(), 900, 600);
        SceneGenerator sceneGenerator = SceneGenerator.getInstance();
        Scene startScene = sceneGenerator.getStartScene(stage, scene);
        stage.setTitle("Jeopardy!");
        stage.setMinHeight(600);
        stage.setMinWidth(Controller.getInstance().getNumCats()*200);

        // Show
        Controller.getInstance().showScene(stage, startScene);
    }
}