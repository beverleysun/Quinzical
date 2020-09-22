package quinzical;


import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class SceneChanger implements EventHandler<MouseEvent> {


    private Parent _Parent;
    public SceneChanger(Parent parent){
        _Parent = parent;
    }
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

    @Override
    public void handle(MouseEvent e) {
        changeScene(e, _Parent);
    }
}
