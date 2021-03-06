package quinzical.model;

import javafx.event.Event;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Enables changing of scenes in the stage area. Preserves scene width and height
 *
 * @author Beverley Sun, Jinkai Zhang
 */
public class SceneChanger {

    /**
     * Changes the scene of the stage. Maintains the size of the stage even if the user has resized the window
     * @param e the event that triggered the change
     * @param parent the parent of the scene to be changed to
     */
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
