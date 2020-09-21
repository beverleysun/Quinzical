package quinzical;


import javafx.event.Event;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SceneChanger {

    public static void changeScene(Event e, Parent parent) {
        // Set scene width and height to the previous scene width and height
        Scene prevScene = ((Node) e.getSource()).getScene();
        Stage window = (Stage) prevScene.getWindow();
        double prevSceneWidth = prevScene.getWidth();
        double prevSceneHeight = prevScene.getHeight();
        Scene scene = new Scene(parent, prevSceneWidth, prevSceneHeight);

        // Show scene
        window.setScene(scene);
    }
}
